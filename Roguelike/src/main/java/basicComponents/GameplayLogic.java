package basicComponents;

import map.Coord;
import map.Direction;
import map.MapGenerator;
import map.MapOfObjects;
import map.objects.*;
import map.objects.Object;
import map.shapes.CustomStaticShape;
import renderer.Renderer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GameplayLogic {
    public static GameplayState gameplayState = GameplayState.NOT_STARTED;
    public static List<Object> objects = new CopyOnWriteArrayList<>();
    public static HeroObject heroObject;

    private static int xSize = 2000;
    private static int ySize = 1000;

    public static String pauseLock = "PAUSE LOCK";

    public static void createMapLevel1() {
        objects = new CopyOnWriteArrayList<>();
        gameplayState = GameplayState.MAP_GENERATING;
        MapGenerator.forestGenerate(xSize, ySize);
        Renderer.fit();
        gameplayState = GameplayState.PLAYING;
    }

    public static void createMapLevel2() {
        objects = new CopyOnWriteArrayList<>();
        gameplayState = GameplayState.MAP_GENERATING;
        MapGenerator.desertGenerate(xSize, ySize);
        Renderer.fit();
        gameplayState = GameplayState.PLAYING;
    }

    public static void createMapLevel3() {
        objects = new CopyOnWriteArrayList<>();
        gameplayState = GameplayState.MAP_GENERATING;
        MapGenerator.dungeonGenerate(xSize, ySize);
        Renderer.fit();
        gameplayState = GameplayState.PLAYING;
    }

    public static void handleOption(GameplayOption option) {
        switch (option) {
            case RUN_RIGHT:
            case RUN_LEFT:
            case RUN_DOWN:
            case RUN_UP:
            case WALK_RIGHT:
            case WALK_LEFT:
            case WALK_DOWN:
            case WALK_UP:
                heroObject.makeMovement(option);
                break;
            case ATTACK_DOWN:
                heroObject.attack(Direction.DOWN);
                break;
            case ATTACK_UP:
                heroObject.attack(Direction.UP);
                break;
            case ATTACK_LEFT:
                heroObject.attack(Direction.LEFT);
                break;
            case ATTACK_RIGHT:
                heroObject.attack(Direction.RIGHT);
                break;

        }
        Controller.draw();
    }

    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED
    }

    public enum GameplayOption {
        WALK_UP, WALK_DOWN, WALK_LEFT, WALK_RIGHT,
        RUN_UP, RUN_DOWN, RUN_LEFT, RUN_RIGHT,
        ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT
    }

}


