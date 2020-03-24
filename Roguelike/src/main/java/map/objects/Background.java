package map.objects;

import map.MapOfObjects;
import map.Coord;
import map.LogicPixel;
import map.shapes.Shape;

import java.util.HashMap;

public class Background extends StaticObject {

    public static LogicPixel generate(BackgroundType type) {
        switch (type) {
            case SAND:
                return LogicPixel.generatePixel(new double[]{0.6, 0.01, 0.01, 0.38}, new LogicPixel[]{
                        LogicPixel.SAND_BACKGROUND_EMPTY, LogicPixel.SAND_BACKGROUND_1, LogicPixel.SAND_BACKGROUND_2, LogicPixel.SAND_BACKGROUND_3});
            case GRASS:
                return LogicPixel.generatePixel(new double[]{0.6, 0.2, 0.2}, new LogicPixel[]{
                        LogicPixel.GRASS_BACKGROUND_EMPTY, LogicPixel.GRASS_BACKGROUND_1, LogicPixel.GRASS_BACKGROUND_2});
            case WATER:
                return LogicPixel.generatePixel(new double[]{0.6, 0.2, 0.2}, new LogicPixel[]{
                        LogicPixel.WATER_BACKGROUND_EMPTY, LogicPixel.WATER_BACKGROUND_1, LogicPixel.WATER_BACKGROUND_2});
            case DUNGEON:
                return LogicPixel.generatePixel(new double[]{0.6, 0.2, 0.2}, new LogicPixel[]{
                        LogicPixel.DUNGEON_BACKGROUND_EMPTY, LogicPixel.DUNGEON_BACKGROUND_1, LogicPixel.DUNGEON_BACKGROUND_2});
        }
        return null;
    }

    public Background(BackgroundType type) {
        init(new Coord(Coord.ZERO), Shape.EMPTY_SHAPE);
        pixelSet = new HashMap<>();
        for (int i = 0; i < MapOfObjects.xSize; i++) {
            for (int j = 0; j < MapOfObjects.ySize; j++) {
                pixelSet.put(new Coord(i, j), generate(type));
            }
        }
    }

    public enum BackgroundType {
        GRASS, SAND, WATER, DUNGEON
    }

}
