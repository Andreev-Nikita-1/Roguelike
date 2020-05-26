package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import gameplayOptions.DirectedOption;
import gameplayOptions.UseItemOption;
import menuLogic.RealAction;
import gameplayOptions.GameplayOption;

import static menuLogic.Menu.*;

public class AppLogic {
    public static final String MAIN_WINDOW_TITLE = "GAME";
    public static volatile boolean active = true;
    public static Game currentGame = new Game();

    private AppLogic() {
    }


    public static void handleKeyStrokeOnMap(KeyStroke keyStroke) {
        if (currentGame.gameplayState == Game.GameplayState.PLAYING) {
            switch (keyStroke.getKeyType()) {
                case Escape:
                    currentGame.pause();
                    Controller.drawMenu(mainMenu);
                    break;
                case Tab:
                    currentGame.openInventory();
                    break;
                default:
                    GameplayOption option = getGameplayOption(keyStroke);
                    currentGame.handleOption(option, keyStroke.getEventTime());
                    break;
            }
        } else if (currentGame.gameplayState == Game.GameplayState.INVENTORY) {
            switch (keyStroke.getKeyType()) {
                case Escape:
                case Tab:
                    currentGame.closeInventory();
                    break;
                default:
                    currentGame.handleKeyStrokeInInventory(keyStroke);
                    break;
            }
        }
    }

    private static GameplayOption getGameplayOption(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case Character:
                char character = keyStroke.getCharacter();
                if (character == ' ') {
                    return GameplayOption.INTERACT;
                }
                if (character > '0' && character < '4') {
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

    public static void applyContinueAction() {
        currentGame.unpause();
    }

    public static void applyExitAction() {
        System.exit(0);
    }

    public static void applyLevel1Action() {
        if (!mainMenu.getActions().contains(RealAction.continueGameAction)) {
            mainMenu.addAction(0, RealAction.continueGameAction);
        }
        currentGame.createMapLevel1();
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



