package map.objects;

import map.shapes.CustomStaticShape;
import mapGenerator.MapGenerator1;
import map.MapOfObjects;
import util.Coord;
import map.LogicPixel;
import renderer.Renderer;

import java.util.HashMap;
import java.util.Map;

import static map.shapes.Shape.EMPTY_SHAPE;
import static util.Coord.ZERO;

public class Background extends StaticObject {

//    public static LogicPixel generate(MapGenerator1.SettingType type) {
//        switch (type) {
//            case SAND:
//                return MapGenerator1.generate(new double[]{0.6, 0.001, 0.001, 0.398}, new LogicPixel[]{
//                        LogicPixel.SAND_BACKGROUND_EMPTY, LogicPixel.SAND_BACKGROUND_1, LogicPixel.SAND_BACKGROUND_2, LogicPixel.SAND_BACKGROUND_3});
//            case GRASS:
//                return MapGenerator1.generate(new double[]{0.6, 0.2, 0.2}, new LogicPixel[]{
//                        LogicPixel.GRASS_BACKGROUND_EMPTY, LogicPixel.GRASS_BACKGROUND_1, LogicPixel.GRASS_BACKGROUND_2});
//            case WATER:
//                return MapGenerator1.generate(new double[]{0.6, 0.2, 0.2}, new LogicPixel[]{
//                        LogicPixel.WATER_BACKGROUND_EMPTY, LogicPixel.WATER_BACKGROUND_1, LogicPixel.WATER_BACKGROUND_2});
//            case DUNGEON:
//                return MapGenerator1.generate(new double[]{0.6, 0.2, 0.2}, new LogicPixel[]{
//                        LogicPixel.DUNGEON_BACKGROUND_EMPTY, LogicPixel.DUNGEON_BACKGROUND_1, LogicPixel.DUNGEON_BACKGROUND_2});
//        }
//        return null;
//    }

    public Background(MapOfObjects map, LogicPixel[][] array) {
        super(map, ZERO, new CustomStaticShape(array, false));
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        return ((CustomStaticShape) shape).getPixels();
    }
}
