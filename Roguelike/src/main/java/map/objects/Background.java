package map.objects;

import basicComponents.MapOfFirmObjects;
import map.Coord;
import map.LogicPixel;
import map.shapes.Shape;

import java.util.HashMap;

public class Background extends StaticObject {

    private static int random(double[] weights) {
        double t = Math.random();
        for (int i = 0; i < weights.length; i++) {
            if (t < weights[i]) {
                return i;
            } else {
                t -= weights[i];
            }
        }
        return 0;
    }

    public static LogicPixel generatePixel(BackgroundType type) {

        switch (type) {
            case SAND:
                switch (random(new double[]{0.6, 0.01, 0.01, 0.38})) {
                    case 0:
                        return LogicPixel.SAND_BACKGROUND_EMPTY;
                    case 1:
                        return LogicPixel.SAND_BACKGROUND_1;
                    case 2:
                        return LogicPixel.SAND_BACKGROUND_2;
                    case 3:
                        return LogicPixel.SAND_BACKGROUND_3;
                }
            case GRASS:
                switch (random(new double[]{0.6, 0.2, 0.2})) {
                    case 0:
                        return LogicPixel.GRASS_BACKGROUND_EMPTY;
                    case 1:
                        return LogicPixel.GRASS_BACKGROUND_1;
                    case 2:
                        return LogicPixel.GRASS_BACKGROUND_2;
                }
            case WATER:
                switch (random(new double[]{0.6, 0.2, 0.2})) {
                    case 0:
                        return LogicPixel.WATER_BACKGROUND_EMPTY;
                    case 1:
                        return LogicPixel.WATER_BACKGROUND_1;
                    case 2:
                        return LogicPixel.WATER_BACKGROUND_2;
                }
            case DUNGEON:
                switch (random(new double[]{0.6, 0.2, 0.2})) {
                    case 0:
                        return LogicPixel.DUNGEON_BACKGROUND_EMPTY;
                    case 1:
                        return LogicPixel.DUNGEON_BACKGROUND_1;
                    case 2:
                        return LogicPixel.DUNGEON_BACKGROUND_2;
                }
        }
        return null;
    }

    public Background(BackgroundType type) {
        init(new Coord(Coord.ZERO), Shape.EMPTY_SHAPE);
        pixelSet = new HashMap<>();
        for (int i = 0; i < MapOfFirmObjects.xSize; i++) {
            for (int j = 0; j < MapOfFirmObjects.ySize; j++) {
                pixelSet.put(new Coord(i, j), generatePixel(type));
            }
        }
    }

    public enum BackgroundType {
        GRASS, SAND, WATER, DUNGEON
    }

}
