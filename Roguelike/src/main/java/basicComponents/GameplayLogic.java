package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import map.Coord;
import map.Direction;
import map.LogicPixel;
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


    public static void createMap() {
        objects = new ArrayList<>();
        MapOfFirmObjects.initialize(100, 100);
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

        for (int i = 0; i < MapOfFirmObjects.xSize-11; i += 11) {
            for (int j = 0; j < MapOfFirmObjects.ySize-11; j += 11) {
                Walls walls = new Walls(new Coord(i, j), new CustomStaticShape(wallsArray), LogicPixel.DUNGEON_WALL);
                objects.add(walls);
                MapOfFirmObjects.placeObject(walls);
            }
        }
        Background background = new Background(Background.BackgroundType.DUNGEON);
        objects.add(background);
        MapOfFirmObjects.placeObject(background);

        heroObject.init(Coord.ZERO, Shape.SINGLE_PIXEL_SHAPE);
        objects.add(heroObject);
        MapOfFirmObjects.placeObject(heroObject);

        gameplayState = GameplayState.PLAYING;
    }

    public static void handleKeyStroke(KeyStroke keyStroke) {
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                heroObject.move(Direction.DOWN);
                break;
            case ArrowDown:
                heroObject.move(Direction.UP);
                break;
            case ArrowLeft:
                heroObject.move(Direction.LEFT);
                break;
            case ArrowRight:
                heroObject.move(Direction.RIGHT);
                break;
        }

    }

    public enum GameplayState {
        NOT_STARTED, PLAYING, PAUSED
    }

}
