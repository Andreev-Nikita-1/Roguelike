package basicComponents;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import menuLogic.Menu;
import menuLogic.MenuAction;
import renderer.Renderer;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Thread.sleep;
import static menuLogic.Menu.optionsMenu;

public class Controller {

    private static SwingTerminalFrame terminal;
    private static WindowBasedTextGUI gui;
    private static Window mainWindow;
    private static GameplayComponent component;

    public static volatile int fontSize = 35;

    private Controller() {
    }

    public static int getTerminalSizeX() {
        return gui.getScreen().getTerminalSize().getColumns();
    }

    public static int getTerminalSizeY() {
        return gui.getScreen().getTerminalSize().getRows();
    }

    public static void initialize() throws IOException {
        terminal = new SwingTerminalFrame(AppLogic.MAIN_WINDOW_TITLE,
                new TerminalSize(30, 13),
                null,
                new SwingTerminalFontConfiguration(false, AWTTerminalFontConfiguration.BoldMode.NOTHING, new Font("VL Gothic Regular", Font.PLAIN, fontSize)),
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
        fontSize = 35;
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
            terminal.close();
            initialize();
            terminal.setVisible(true);
            terminal.setExtendedState(terminal.MAXIMIZED_BOTH);
            gui.getScreen().startScreen();
            mainWindow.setComponent(component);
        } catch (IOException e) {
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

    public static void drawMenu(Menu menu) {
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


