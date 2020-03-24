package basicComponents;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;
import renderer.Renderer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public abstract class Controller {

    private static SwingTerminalFrame terminal;
    private static WindowBasedTextGUI gui;
    private static Window mainWindow;
    private static GameplayComponent component;

    public static int getTerminalSizeX() {
        return gui.getScreen().getTerminalSize().getColumns();
    }

    public static int getTerminalSizeY() {
        return gui.getScreen().getTerminalSize().getRows();
    }

    public static void initialize() throws IOException {
        terminal = new SwingTerminalFrame("GAME",
                new TerminalSize(60, 30),
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


    public static void run() {
        try {
            terminal.setVisible(true);
            gui.getScreen().startScreen();
            mainWindow.setComponent(component);
            drawMenu(AppLogic.mainMenuActions);

            while (true) {
                sleep(10);
                gui.updateScreen();
                gui.processInput();
            }
        } catch (IOException | InterruptedException e) {

        }
    }


    public static void drawMenu(List<AppLogic.MenuAction> menuActions) {
        ActionListDialogBuilder builder = new ActionListDialogBuilder();
        builder.setTitle(AppLogic.MAIN_MENU_TITLE);
        builder.setCanCancel(false);
        for (AppLogic.MenuAction action : menuActions) {
            builder.addAction(action.getName(), action.getAction());
        }
        DialogWindow dialog = builder.build();
        dialog.setHints(Arrays.asList(Window.Hint.CENTERED));
        gui.addWindow(dialog);
    }


}

class GameplayComponent extends AbstractInteractableComponent<GameplayComponent> {

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

class MapRenderer implements InteractableRenderer<GameplayComponent> {

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


class CharInputComponent extends AbstractInteractableComponent<CharInputComponent> {

    @Override
    protected InteractableRenderer<CharInputComponent> createDefaultRenderer() {
        return new MyRenderer();
    }

    @Override
    protected Result handleKeyStroke(KeyStroke keyStroke) {
        if (!(keyStroke.getKeyType() == KeyType.Escape)) {
            char hero = keyStroke.getCharacter();
            AppLogic.HERO_SYMBOL = hero;
            Controller.drawMenu(AppLogic.optionsActions);
        }
        return Result.HANDLED;
    }

    private class MyRenderer implements InteractableRenderer<CharInputComponent> {

        @Override
        public TerminalPosition getCursorLocation(CharInputComponent component) {
            return null;
        }

        @Override
        public TerminalSize getPreferredSize(CharInputComponent component) {
            return null;
        }

        @Override
        public void drawComponent(TextGUIGraphics graphics, CharInputComponent component) {

        }
    }
}