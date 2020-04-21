package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import mapGenerator.TestGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;


import static basicComponents.GameplayLogic.GameplayState.*;

public class GameplayLogic {
    public static GameplayState gameplayState = NOT_STARTED;

    public static MapOfObjects currentMap;
    public static MapRenderer currentMapRenderer;

    private static int xSize = 20;
    private static int ySize = 20;

    private GameplayLogic() {
    }

    public static void createMap(MapGenerator mapGenerator) {
        currentMap = mapGenerator.generateMap();
        currentMapRenderer = new MapRenderer(currentMap).fit();
        currentMap.start();
    }

    public static void pause() {
        currentMap.pause();
        gameplayState = PAUSED;
    }

    public static void unpause() {
        currentMap.unpause();
        gameplayState = PLAYING;
    }


    public static void createMapLevel1() {
        gameplayState = MAP_GENERATING;
        createMap(new TestGenerator(xSize, ySize));
        gameplayState = PLAYING;
    }

    public static void handleOption(GameplayOption option, long eventTine) {
        if (option == GameplayOption.INTERACT) {
            currentMap.heroObject.interactWith();
        }
        if (option instanceof DirectedOption) {
            switch (((DirectedOption) option).action) {
                case WALK:
                case RUN:
                    currentMap.heroObject.makeMovement((DirectedOption) option, eventTine);
                    break;
                case ATTACK:
                    currentMap.heroObject.makeAttack(((DirectedOption) option), eventTine);
                    break;
            }
        }
    }

    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED
    }

}


