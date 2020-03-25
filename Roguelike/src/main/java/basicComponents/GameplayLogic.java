package basicComponents;

import map.Coord;
import map.Direction;
import map.MapOfObjects;
import map.objects.*;
import map.objects.Object;
import map.shapes.CustomStaticShape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class GameplayLogic {
    public static GameplayState gameplayState = GameplayState.NOT_STARTED;
    public static List<Object> objects = new CopyOnWriteArrayList<>();
    public static HeroObject heroObject;

    public static String pauseLock = "PAUSE LOCK";

    public static void createMapLevel1() {

        objects = new CopyOnWriteArrayList<>();
        MapOfObjects.initialize(500, 200);
        int[][] wallsArray = new int[11][7];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(wallsArray[i], 0);
        }

        wallsArray[5][4] = 1;
        wallsArray[4][4] = 1;
        wallsArray[3][4] = 1;
        wallsArray[2][4] = 1;
        wallsArray[6][4] = 1;
        wallsArray[7][4] = 1;
        wallsArray[8][4] = 1;
        wallsArray[5][4] = 1;
        wallsArray[5][3] = 1;
        wallsArray[5][2] = 1;
        wallsArray[5][5] = 1;
        wallsArray[5][6] = 1;

        Background.BackgroundType backgroundType = Background.BackgroundType.DUNGEON;

        for (int i = 0; i < MapOfObjects.xSize - 11; i += 11) {
            for (int j = 0; j < MapOfObjects.ySize - 7; j += 7) {
                Walls walls = new Walls(new Coord(i, j), new CustomStaticShape(wallsArray), backgroundType);
//                if ((i / 11) %  == 2 && (j / 11) % 3 == 2) {
                Swordsman swordsman1 = new Swordsman(i, j);
//                }
            }
        }
        Background background = new Background(backgroundType);
        heroObject = new HeroObject(14, 14, 45);


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
        NOT_STARTED, PLAYING, PAUSED
    }

    public enum GameplayOption {
        WALK_UP, WALK_DOWN, WALK_LEFT, WALK_RIGHT,
        RUN_UP, RUN_DOWN, RUN_LEFT, RUN_RIGHT,
        ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT
    }

}


