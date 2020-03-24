package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import map.Coord;
import map.Direction;
import map.MapOfObjects;
import map.objects.Background;
import map.objects.HeroObject;
import map.objects.Object;
import map.objects.Walls;
import map.shapes.CustomStaticShape;
import map.shapes.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GameplayLogic {
    public static GameplayState gameplayState = GameplayState.NOT_STARTED;
    public static List<Object> objects = new ArrayList<>();
    public static HeroObject heroObject = new HeroObject();


    public static void createMapLevel1() {
        objects = new ArrayList<>();
        MapOfObjects.initialize(1000, 1000);
        int[][] wallsArray = new int[11][11];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(wallsArray[i], 0);
        }

        wallsArray[5][5] = 1;
        wallsArray[4][5] = 1;
        wallsArray[3][5] = 1;
        wallsArray[2][5] = 1;
        wallsArray[6][5] = 1;
        wallsArray[7][5] = 1;
        wallsArray[8][5] = 1;
        wallsArray[5][4] = 1;
        wallsArray[5][3] = 1;
        wallsArray[5][2] = 1;
        wallsArray[5][6] = 1;
        wallsArray[5][7] = 1;
        wallsArray[5][8] = 1;

        Background.BackgroundType backgroundType = Background.BackgroundType.DUNGEON;

        for (int i = 0; i < MapOfObjects.xSize - 11; i += 11) {
            for (int j = 0; j < MapOfObjects.ySize - 11; j += 11) {
                Walls walls = new Walls(new Coord(i, j), new CustomStaticShape(wallsArray), backgroundType);
                objects.add(walls);
                MapOfObjects.placeObject(walls);
            }
        }
        Background background = new Background(backgroundType);
        objects.add(background);
        MapOfObjects.placeObject(background);

        heroObject.init(Coord.ZERO, Shape.SINGLE_PIXEL_SHAPE);
        objects.add(heroObject);
        MapOfObjects.placeObject(heroObject);

        gameplayState = GameplayState.PLAYING;
    }

    public static void handleOption(GameplayOption option) {
        switch (option) {
            case MOVE_DOWN:
                heroObject.move(Direction.DOWN);
                break;
            case MOVE_UP:
                heroObject.move(Direction.UP);
                break;
            case MOVE_LEFT:
                heroObject.move(Direction.LEFT);
                break;
            case MOVE_RIGHT:
                heroObject.move(Direction.RIGHT);
                break;
        }

    }

    public enum GameplayState {
        NOT_STARTED, PLAYING, PAUSED
    }

    public enum GameplayOption {
        MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT,
        ATTACK_UP, ATTACK_DOWN, ATTACK_LEFT, ATTACK_RIGHT
    }

}
