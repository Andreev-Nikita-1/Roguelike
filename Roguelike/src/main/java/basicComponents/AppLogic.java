package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import gameplayOptions.DirectedOption;
import mapGenerator.DungeonGenerator;
import menuLogic.Menu;
import menuLogic.RealAction;
import gameplayOptions.GameplayOption;
import renderer.Renderer;
import util.Client;
import util.Server;

import java.io.IOException;

import static menuLogic.Menu.*;

public class AppLogic {
    public static final String MAIN_WINDOW_TITLE = "GAME";

    public static char HERO_SYMBOL = (char) 9977;

    public static volatile boolean active = true;

    private AppLogic() {
    }


    public static void handleKeyStrokeOnMap(KeyStroke keyStroke) {
        if (GameplayLogic.gameplayState != GameplayLogic.GameplayState.PLAYING) {
            return;
        }
        switch (keyStroke.getKeyType()) {
//            case Escape:
//                GameplayLogic.pause();
//                Controller.drawMenu(mainMenu);
//                break;
//            case Tab:
//                GameplayLogic.pause();
//                Controller.drawMenu(inventory);
//                break;
            default:
                GameplayOption option = getGameplayOption(keyStroke);
                GameplayLogic.handleOption(0, option, keyStroke.getEventTime());
                break;
        }
    }

    private static GameplayOption getGameplayOption(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case Character:
                if (keyStroke.getCharacter() == ' ') {
                    return GameplayOption.INTERACT;
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
        GameplayLogic.unpause();
    }

    public static void applyExitAction() {
        System.exit(0);
    }

    public static void applyLevel1Action() {
        if (!mainMenu.getActions().contains(RealAction.continueGameAction)) {
            mainMenu.addAction(0, RealAction.continueGameAction);
        }
        Menu.inventory = new Menu("INVENTORY");
        Menu.inventory.addAction(RealAction.continueGameAction);
        GameplayLogic.createMapLevel1();
        GameplayLogic.currentMap.addHero();
    }

    static io.grpc.Server server;

    public static void runServer() {
        try {
            var server = Server.run(6969);
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
        GameplayLogic.createMap(new DungeonGenerator(50, 50));
    }


    public static Client client;
    public static int id;

    public static void joinGame() {
        client = new Client("localhost", 6969);
        id = client.join(Model.JoinMessage.newBuilder().build());
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



