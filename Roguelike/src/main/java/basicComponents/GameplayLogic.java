package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import mapGenerator.ForestGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;


import static basicComponents.GameplayLogic.GameplayState.*;

public class GameplayLogic {
    public static GameplayState gameplayState = NOT_STARTED;

    public static MapOfObjects currentMap;
    public static MapRenderer currentMapRenderer;

    private static int xSize = 480;
    private static int ySize = 240;

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
        createMap(new ForestGenerator(xSize, ySize));
        gameplayState = PLAYING;
    }

//    public static void createMapLevel2() {
//        gameplayState = GameplayState.MAP_GENERATING;
//        MapGenerator1.desertGenerate(xSize, ySize);
//        Renderer.fit();
//        gameplayState = GameplayState.PLAYING;
//    }
//
//    public static void createMapLevel3() {
//        gameplayState = GameplayState.MAP_GENERATING;
//        MapGenerator1.dungeonGenerate(xSize, ySize);
//        Renderer.fit();
//        gameplayState = GameplayState.PLAYING;
//    }

    public static void handleOption(GameplayOption option) {
//        System.out.println(currentMap.getHeroLocation().x);
        if (option instanceof DirectedOption) {
            switch (((DirectedOption) option).action) {
                case WALK:
                    currentMap.heroObject.makeWalkMovement(((DirectedOption) option).direction);
                    break;
                case RUN:
                    currentMap.heroObject.makeRunMovement(((DirectedOption) option).direction);
                    break;
                case ATTACK:
                    currentMap.heroObject.attack(((DirectedOption) option).direction);
                    break;
            }
        }
//        Controller.draw();
    }

    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED
    }

}


