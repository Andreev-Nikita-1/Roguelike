import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.io.IOException;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public abstract class Controller {

    public static int x = 0;
    public static int y = 0;

    private static SwingTerminalFrame terminal;
    private static WindowBasedTextGUI gui;
    private static Window mainWindow;
    private static GameplayComponent component;

    static void initialize() throws IOException {
        terminal = new SwingTerminalFrame("GAME",
                new TerminalSize(100, 50),
                null, null, null,
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode);

        TerminalScreen screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        gui = new MultiWindowTextGUI(screen, TextColor.ANSI.BLACK);
        mainWindow = new BasicWindow();
        mainWindow.setHints(Arrays.asList(Window.Hint.NO_DECORATIONS, Window.Hint.FULL_SCREEN));
        gui.addWindow(mainWindow);
        component = new GameplayComponent();
    }


    public static void run() throws IOException {

        terminal.setVisible(true);
        gui.getScreen().startScreen();
        drawMainMenu();

        while (true) {
            try {
                sleep(10);
                gui.updateScreen();
                gui.processInput();
            } catch (IOException | InterruptedException e) {
                break;
            }
        }
    }

    public static void drawMap() {
        mainWindow.setComponent(component);
    }

    public static void drawMainMenu() {
        ActionListDialogBuilder builder = new ActionListDialogBuilder();
        builder.setTitle(AppLogic.MAIN_MENU_TITLE);
        builder.setCanCancel(false);
        for (AppLogic.MainMenuAction action : AppLogic.mainMenuActions) {
            builder.addAction(action.getName(), action.getAction());
        }
        DialogWindow dialog = builder.build();
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED));
        gui.addWindow(dialog);
    }

}
