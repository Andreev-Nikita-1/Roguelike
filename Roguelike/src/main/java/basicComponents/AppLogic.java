package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import map.objects.DynamicObject;
import map.objects.Swordsman;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AppLogic {
    public static final String MAIN_WINDOW_TITLE = "GAME";
    public static final String MAIN_MENU_TITLE = "MENU";

    public static char HERO_SYMBOL = '+';

    public static List<MenuAction> mainMenuActions = new ArrayList<>(Arrays.asList(
            MenuAction.newGame,
            MenuAction.options,
            MenuAction.exit));

    public static List<MenuAction> levelSelectorActions = new ArrayList<>(Arrays.asList(
            MenuAction.level1,
            MenuAction.level2,
            MenuAction.level3,
            MenuAction.back));

    public static List<MenuAction> optionsActions = new ArrayList<>(Arrays.asList(
            MenuAction.back));

    private static GameState gameState = GameState.MAIN_MENU;


    public static void handleKeyStrokeOnMap(KeyStroke keyStroke) {
        assert (gameState == GameState.ON_MAP);
        switch (keyStroke.getKeyType()) {
            case Escape:
                pause();
                gameState = GameState.MAIN_MENU;
                Controller.drawMenu(AppLogic.mainMenuActions);
                break;
            default:
                GameplayLogic.GameplayOption option = getGameplayOption(keyStroke);
                if (option != null) GameplayLogic.handleOption(option);
                break;
        }
    }

    private static GameplayLogic.GameplayOption getGameplayOption(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                if (alt) return GameplayLogic.GameplayOption.ATTACK_DOWN;
                if (shift) return GameplayLogic.GameplayOption.RUN_DOWN;
                else return GameplayLogic.GameplayOption.WALK_DOWN;
            case ArrowDown:
                if (alt) return GameplayLogic.GameplayOption.ATTACK_UP;
                if (shift) return GameplayLogic.GameplayOption.RUN_UP;
                else return GameplayLogic.GameplayOption.WALK_UP;
            case ArrowLeft:
                if (alt) return GameplayLogic.GameplayOption.ATTACK_LEFT;
                if (shift) return GameplayLogic.GameplayOption.RUN_LEFT;
                else return GameplayLogic.GameplayOption.WALK_LEFT;
            case ArrowRight:
                if (alt) return GameplayLogic.GameplayOption.ATTACK_RIGHT;
                if (shift) return GameplayLogic.GameplayOption.RUN_RIGHT;
                else return GameplayLogic.GameplayOption.WALK_RIGHT;
            default:
                return null;
        }
    }

    public static void pause() {
        for (Object o : GameplayLogic.objects) {
            if (o instanceof DynamicObject) {
                ((DynamicObject) o).pause();
            }
        }
    }

    public static void unpause() {
        synchronized (GameplayLogic.pauseLock) {
            GameplayLogic.pauseLock.notifyAll();
        }
    }

    public static void applyContinueAction() {
        assert (gameState == GameState.MAIN_MENU);
        unpause();
        gameState = GameState.ON_MAP;
    }

    public static void applyNewGameAction() {
        assert (gameState == GameState.MAIN_MENU);
        gameState = GameState.LEVEL_SELECTOR;
        Controller.drawMenu(levelSelectorActions);
    }

    public static void applyOptionsAction() {
        assert (gameState == GameState.MAIN_MENU);
        gameState = GameState.OPTIONS;
        Controller.drawMenu(optionsActions);
    }

    public static void applyExitAction() {
        assert (gameState == GameState.MAIN_MENU);
        System.exit(0);
    }

    public static void applyBackAction() {
        assert (gameState == GameState.LEVEL_SELECTOR || gameState == GameState.OPTIONS);
        gameState = GameState.MAIN_MENU;
        Controller.drawMenu(mainMenuActions);
    }

    public static void applyLevel1Action() {
        assert (gameState == GameState.LEVEL_SELECTOR);
        if (!mainMenuActions.contains(MenuAction.continueGame)) {
            mainMenuActions.add(0, MenuAction.continueGame);
        }
        gameState = GameState.ON_MAP;
        Renderer.reset();
        GameplayLogic.createMapLevel1();
    }

    public static void applyLevel2Action() {
        System.exit(0);
    }

    public static void applyLevel3Action() {
        System.exit(0);
    }

    public static class MenuAction {
        private String name;
        private Runnable action;

        public static MenuAction continueGame = new MenuAction("continue",
                () -> AppLogic.applyContinueAction());
        public static MenuAction newGame = new MenuAction("new game",
                () -> AppLogic.applyNewGameAction());
        public static MenuAction options = new MenuAction("options",
                () -> AppLogic.applyOptionsAction());
        public static MenuAction exit = new MenuAction("exit",
                () -> AppLogic.applyExitAction());

        public static MenuAction back = new MenuAction("back",
                () -> AppLogic.applyBackAction());

        public static MenuAction level1 = new MenuAction("level 1",
                () -> AppLogic.applyLevel1Action());
        public static MenuAction level2 = new MenuAction("level 2",
                () -> AppLogic.applyLevel2Action());
        public static MenuAction level3 = new MenuAction("level 3",
                () -> AppLogic.applyLevel3Action());

        MenuAction(String name, Runnable action) {
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
        MAIN_MENU, LEVEL_SELECTOR, OPTIONS, ON_MAP, IN_INVENTORY
    }

}



