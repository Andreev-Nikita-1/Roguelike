package basicComponents;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import menuLogic.Menu;
import menuLogic.MenuAction;
import renderer.Renderer;
import util.TimeIntervalActor;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;
import static menuLogic.Menu.*;

public class Controller {

    private static SwingTerminalFrame terminal;
    private static WindowBasedTextGUI gui;
    private static Window mainWindow;
    private static GameplayComponent component;
    private static final float fontSizeDefault = 40;
    private static volatile float fontSize = fontSizeDefault;

    private Controller() {
    }

    public static int getTerminalSizeX() {
        return gui.getScreen().getTerminalSize().getColumns();
    }

    public static int getTerminalSizeY() {
        return gui.getScreen().getTerminalSize().getRows();
    }

    public static void initialize() throws IOException, FontFormatException {
        terminal = new SwingTerminalFrame(AppLogic.MAIN_WINDOW_TITLE,
                new TerminalSize(30, 13),
                null,
                new SwingTerminalFontConfiguration(false,
                        AWTTerminalFontConfiguration.BoldMode.NOTHING,
                        Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/FONT.ttf")).deriveFont(fontSize)),
                null,
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);
        TerminalScreen screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        gui = new MultiWindowTextGUI(screen, TextColor.ANSI.BLACK);
        mainWindow = new BasicWindow();
        mainWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FULL_SCREEN));
        gui.addWindow(mainWindow);
        component = new GameplayComponent();
    }

    public static void zoomDefault() {
        fontSize = fontSizeDefault;
        update();
        drawMenu(optionsMenu);
    }

    public static void zoomIn() {
        fontSize += 5;
        update();
        drawMenu(optionsMenu);
    }

    public static void zoomOut() {
        if (fontSize > 5)
            fontSize -= 5;
        update();
        drawMenu(optionsMenu);
    }

    public static void update() {
        try {
            AppLogic.active = true;
            if (terminal != null)
                terminal.close();
            initialize();
            terminal.setVisible(true);
            terminal.setExtendedState(terminal.MAXIMIZED_BOTH);
            gui.getScreen().startScreen();
            mainWindow.setComponent(component);
        } catch (IOException | FontFormatException e) {
        }
    }

    public static void run() {
        while (AppLogic.active) {
            AppLogic.active = false;
            try {
                while (true) {
                    sleep(10);
                    gui.updateScreen();
                    gui.processInput();
                }
            } catch (IOException | InterruptedException e) {
            }
        }
    }

    public static Menu lastMenu;
    public static Menu currentMenu;

    public static void drawMenu(Menu menu) {
        if (menu != currentMenu) {
            lastMenu = currentMenu;
            currentMenu = menu;
        }
        ActionListDialogBuilder builder = new ActionListDialogBuilder();
        builder.setTitle(menu.getTitle());
        builder.setCanCancel(false);
        for (MenuAction action : menu.getActions()) {
            builder.addAction(action.getName(), action.getAction());
        }
        DialogWindow dialog = builder.build();
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.NO_POST_RENDERING));
        gui.addWindow(dialog);
    }

    public static void drawSaveGameDialog() {
        TextInputDialogBuilder builder = new TextInputDialogBuilder();
        builder.setTitle("SAVE GAME");
        TextInputDialog dialog = builder.build();
        dialog.setSize(new TerminalSize((int) (getTerminalSizeX() / 1.5), 5));
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.NO_POST_RENDERING, Window.Hint.FIXED_SIZE));
        new Thread(() -> {
            String saveName = dialog.showDialog(gui);
            if (saveName != null) {
                AppLogic.saveGame(saveName);
                drawMenu(success);
            } else {
                drawMenu(activeGameMainMenu);
            }
        }).start();
    }

    public static void drawFileDialog() {
        var list = AppLogic.getSaves();
        Table<String> table = new Table<>("save", "time");
        table.getTableModel().addRow("back", "");
        for (var map : list) {
            table.getTableModel().addRow(map.name, map.date);
        }
        BasicWindow window = new BasicWindow();
        window.setSize(new TerminalSize((int) (getTerminalSizeX() / 1.5), (int) (getTerminalSizeY() / 1.5)));
        window.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.NO_POST_RENDERING, Window.Hint.FIXED_SIZE));
        window.setComponent(table);
        gui.addWindow(window);
        table.setSelectAction(() -> {
            if (table.getSelectedRow() == 0) {
                Controller.drawMenu(currentMenu);
            } else {
                AppLogic.loadGame(list.get(table.getSelectedRow() - 1).name);
            }
            gui.removeWindow(window);
        });
    }

    static class GameplayComponent extends AbstractInteractableComponent<GameplayComponent> {

        @Override
        protected InteractableRenderer<GameplayComponent> createDefaultRenderer() {
            return new MapRenderer();
        }

        @Override
        protected Result handleKeyStroke(KeyStroke keyStroke) {
            AppLogic.handleKeyStrokeOnMap(keyStroke);
            return Result.HANDLED;
        }
    }

    static class MapRenderer implements InteractableRenderer<GameplayComponent> {

        @Override
        public TerminalPosition getCursorLocation(GameplayComponent component) {
            return null;
        }

        @Override
        public TerminalSize getPreferredSize(GameplayComponent component) {
            return null;
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, GameplayComponent component) {
            Renderer.render(graphics);
        }
    }
}


