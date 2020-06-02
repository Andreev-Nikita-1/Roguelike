package basicComponents;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import gameplayOptions.UseItemOption;
import menuLogic.Menu;
import menuLogic.MenuAction;
import renderer.Renderer;
import renderer.inventoryWindow.InventoryWindow;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static basicComponents.AppLogic.GameplayState.*;
import static java.lang.Thread.sleep;
import static menuLogic.Menu.*;

/**
 * Implementation of ViewController
 */
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

    private static void initialize() throws IOException, FontFormatException {
        terminal = new SwingTerminalFrame(AppLogic.MAIN_WINDOW_TITLE,
                new TerminalSize(30, 13),
                null,
                new SwingTerminalFontConfiguration(false,
                        AWTTerminalFontConfiguration.BoldMode.NOTHING,
                        Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/FONT.ttf")).deriveFont(fontSize)),
                null,
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);
        var screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        gui = new MultiWindowTextGUI(screen, TextColor.ANSI.BLACK);
        mainWindow = new BasicWindow();
        mainWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FULL_SCREEN));
        gui.addWindow(mainWindow);
        component = new GameplayComponent();
    }

    /**
     * Sets default font size
     */
    public static void zoomDefault() {
        fontSize = fontSizeDefault;
        update();
        drawMenu(optionsMenu);
    }

    /**
     * Increases font size
     */
    public static void zoomIn() {
        fontSize += 5;
        update();
        drawMenu(optionsMenu);
    }

    /**
     * Decreases font size
     */
    public static void zoomOut() {
        if (fontSize > 5)
            fontSize -= 5;
        update();
        drawMenu(optionsMenu);
    }

    /**
     * Updates gui, when app starts, or when font size is changed
     */
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
            e.printStackTrace();
        }
    }

    /**
     * Cycle for updating screen and processing input
     */
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

    private static Menu lastMenu;
    private static Menu currentMenu;


    public static Menu getLastMenu() {
        return lastMenu;
    }

    /**
     * Draws menu
     */
    public static void drawMenu(Menu menu) {
        if (menu != currentMenu) {
            lastMenu = currentMenu;
            currentMenu = menu;
        }
        var builder = new ActionListDialogBuilder();
        builder.setTitle(menu.getTitle());
        builder.setCanCancel(false);
        for (MenuAction action : menu.getActions()) {
            builder.addAction(action.getName(), action.getAction());
        }
        var dialog = builder.build();
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.NO_POST_RENDERING));
        gui.addWindow(dialog);
    }

    /**
     * Draws dialog for inputting save name
     */
    public static void drawSaveGameDialog() {
        var builder = new TextInputDialogBuilder();
        builder.setTitle("SAVE GAME");
        var dialog = builder.build();
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

    /**
     * Draws dialog for choosing save game
     */
    public static void drawSavesDialog() {
        var list = AppLogic.getFiles(new File("src/main/resources/saves"));
        Table<String> table = new Table<>("save", "time");
        drawFileDialog(list, table, AppLogic::loadGame);
    }

    /**
     * Draws dialog for choosing map
     */
    public static void drawMapsDialog() {
        var list = AppLogic.getFiles(new File("src/main/resources/maps"));
        Table<String> table = new Table<>("map", "time");
        drawFileDialog(list, table, AppLogic::loadMap);
    }

    private static void drawFileDialog(List<AppLogic.FileInfo> list, Table<String> table, Consumer<String> action) {
        table.getTableModel().addRow("back", "");
        for (var map : list) {
            table.getTableModel().addRow(map.name, map.date);
        }
        var window = new BasicWindow();
        window.setSize(new TerminalSize((int) (getTerminalSizeX() / 1.5), (int) (getTerminalSizeY() / 1.5)));
        window.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.NO_POST_RENDERING, Window.Hint.FIXED_SIZE));
        window.setComponent(table);
        gui.addWindow(window);
        table.setSelectAction(() -> {
            if (table.getSelectedRow() == 0) {
                drawMenu(currentMenu);
            } else {
                action.accept(list.get(table.getSelectedRow() - 1).name);
            }
            gui.removeWindow(window);
        });
    }


    /**
     * Class for component with map
     */
    static class GameplayComponent extends AbstractInteractableComponent<GameplayComponent> {

        private static final Consumer<KeyStroke> keyStrokeHandlerOnMap = new Consumer<>() {
            @Override
            public void accept(KeyStroke keyStroke) {
                switch (keyStroke.getKeyType()) {
                    case Escape:
                        AppLogic.currentGame.pause();
                        AppLogic.gameplayState = PAUSED;
                        Controller.drawMenu(activeGameMainMenu);
                        break;
                    case Tab:
                        AppLogic.openInventory();
                        break;
                    default:
                        GameplayOption option = getGameplayOption(keyStroke);
                        AppLogic.currentGame.handleOption(option, keyStroke.getEventTime());
                        break;
                }
            }
        };

        private static final Consumer<KeyStroke> keyStrokeHandlerInInventory = new Consumer<>() {
            @Override
            public void accept(KeyStroke keyStroke) {
                switch (keyStroke.getKeyType()) {
                    case Escape:
                    case Tab:
                        AppLogic.closeInventory();
                        break;
                    default:
                        InventoryWindow.handleKeyStroke(keyStroke);
                        break;
                }
            }
        };

        /**
         * Returns gameplay option from key
         */
        private static GameplayOption getGameplayOption(KeyStroke keyStroke) {
            boolean alt = keyStroke.isAltDown();
            boolean shift = keyStroke.isShiftDown();
            switch (keyStroke.getKeyType()) {
                case Character:
                    char character = keyStroke.getCharacter();
                    if (character == ' ') {
                        return GameplayOption.INTERACT;
                    }
                    if (character >= '1' && character <= '4') {
                        return new UseItemOption(character - '1');
                    }
                case ArrowDown:
                    if (alt) return DirectedOption.ATTACK_DOWN;
                    if (shift) return DirectedOption.RUN_DOWN;
                    else return DirectedOption.WALK_DOWN;
                case ArrowUp:
                    if (alt) return DirectedOption.ATTACK_UP;
                    if (shift) return DirectedOption.RUN_UP;
                    else return DirectedOption.WALK_UP;
                case ArrowLeft:
                    if (alt) return DirectedOption.ATTACK_LEFT;
                    if (shift) return DirectedOption.RUN_LEFT;
                    else return DirectedOption.WALK_LEFT;
                case ArrowRight:
                    if (alt) return DirectedOption.ATTACK_RIGHT;
                    if (shift) return DirectedOption.RUN_RIGHT;
                    else return DirectedOption.WALK_RIGHT;
                default:
                    return GameplayOption.NOTHING;
            }
        }


        @Override
        protected InteractableRenderer<GameplayComponent> createDefaultRenderer() {
            return new MapRenderer();
        }

        @Override
        protected Result handleKeyStroke(KeyStroke keyStroke) {
            if (AppLogic.gameplayState == PLAYING) {
                keyStrokeHandlerOnMap.accept(keyStroke);
            } else if (AppLogic.gameplayState == INVENTORY) {
                keyStrokeHandlerInInventory.accept(keyStroke);
            }
            return Result.HANDLED;
        }
    }

    /**
     * Class for rendering previous component
     */
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


