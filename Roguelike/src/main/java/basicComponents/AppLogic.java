package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AppLogic {
    public static final String MAIN_WINDOW_TITLE = "GAME";
    public static final String MAIN_MENU_TITLE = "MENU";


    public static List<MainMenuAction> mainMenuActions;
    private static GameState gameState;

    public static void initialize() {
        mainMenuActions = new ArrayList<>(Arrays.asList(
                MainMenuAction.newGame,
                MainMenuAction.exit));
    }

    public static void handleKeyStrokeOnMap(KeyStroke keyStroke) {
        assert (gameState == GameState.ON_MAP);
        switch (keyStroke.getKeyType()) {
            case Escape:
                gameState = GameState.IN_MENU;
                Controller.drawMainMenu();
                break;
            default:
                GameplayLogic.handleKeyStroke(keyStroke);
                break;
        }
    }

    public static void applyContinueAction() {
        assert (gameState == GameState.IN_MENU);
        gameState = GameState.ON_MAP;
    }

    public static void applyNewGameAction() {
        assert (gameState == GameState.IN_MENU);
        if (!mainMenuActions.contains(MainMenuAction.continueGame)) {
            mainMenuActions.add(0, MainMenuAction.continueGame);
        }
        gameState = GameState.ON_MAP;
        Renderer.reset();
        GameplayLogic.createMap();
    }

    public static void applyExitAction() {
        System.exit(0);
    }

    public static class MainMenuAction {
        private String name;
        private Runnable action;

        public static MainMenuAction continueGame = new MainMenuAction("continue",
                () -> AppLogic.applyContinueAction());
        public static MainMenuAction newGame = new MainMenuAction("new game",
                () -> AppLogic.applyNewGameAction());
        public static MainMenuAction exit = new MainMenuAction("exit",
                () -> AppLogic.applyExitAction());

        MainMenuAction(String name, Runnable action) {
            this.name = name;
            this.action = action;
        }

        public String getName() {
            return name;
        }

        public Runnable getAction() {
            return action;
        }
    }

    enum GameState {
        IN_MENU, ON_MAP, IN_INVENTORY
    }

}



