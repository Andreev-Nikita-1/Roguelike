package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import gameplayOptions.DirectedOption;
import menuLogic.RealAction;
import gameplayOptions.GameplayOption;

import static basicComponents.GameplayLogic.pause;
import static basicComponents.GameplayLogic.unpause;
import static menuLogic.Menu.*;

public class AppLogic {
    public static final String MAIN_WINDOW_TITLE = "GAME";

    public static char HERO_SYMBOL = '+';

    private AppLogic() {
    }


    public static void handleKeyStrokeOnMap(KeyStroke keyStroke) {
        if (GameplayLogic.gameplayState != GameplayLogic.GameplayState.PLAYING) {
            return;
        }
        switch (keyStroke.getKeyType()) {
            case Escape:
                pause();
                Controller.drawMenu(mainMenu);
                break;
            default:
                GameplayOption option = getGameplayOption(keyStroke);
                GameplayLogic.handleOption(option);
                break;
        }
    }

    private static GameplayOption getGameplayOption(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                if (alt) return DirectedOption.ATTACK_DOWN;
                if (shift) return DirectedOption.RUN_DOWN;
                else return DirectedOption.WALK_DOWN;
            case ArrowDown:
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

    public static void applyContinueAction() {
        unpause();
    }

    public static void applyExitAction() {
        System.exit(0);
    }

    public static void applyLevel1Action() {
        if (!mainMenu.getActions().contains(RealAction.continueGameAction)) {
            mainMenu.addAction(0, RealAction.continueGameAction);
        }
//        new Thread(() -> GameplayLogic.createMapLevel1()).start();
        GameplayLogic.createMapLevel1();
    }

    public static void applyLevel2Action() {
//        assert (gameState == GameState.LEVEL_SELECTOR);
//        if (!mainMenuActions.contains(MenuAction.continueGame)) {
//            mainMenuActions.add(0, MenuAction.continueGame);
//        }
//        gameState = GameState.ON_MAP;
//        new Thread(() -> GameplayLogic.createMapLevel2()).start();
    }

    public static void applyLevel3Action() {
//        assert (gameState == GameState.LEVEL_SELECTOR);
//        if (!mainMenuActions.contains(MenuAction.continueGame)) {
//            mainMenuActions.add(0, MenuAction.continueGame);
//        }
//        gameState = GameState.ON_MAP;
//        new Thread(() -> GameplayLogic.createMapLevel3()).start();
    }
}



