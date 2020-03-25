package map.objects;

import map.Coord;
import map.MapGenerator;
import map.shapes.Shape;
import map.LogicPixel;

import java.util.HashMap;

public class Walls extends StaticObject {
    public Walls(Coord coord, Shape shape, MapGenerator.SettingType type) {
        init(coord, shape);
        pixelSet = new HashMap<>();
        double[] weights = new double[0];
        LogicPixel[] options = new LogicPixel[0];
        switch (type) {
            case GRASS:
                weights = new double[]{0.6, 0.4};
                options = new LogicPixel[]{LogicPixel.GRASS_WALL_1, LogicPixel.GRASS_WALL_2};
                break;
            case DUNGEON:
                weights = new double[]{1};
                options = new LogicPixel[]{LogicPixel.DUNGEON_WALL};
                break;
        }
        for (Coord c : getCoords()) {
            pixelSet.put(c, MapGenerator.generate(weights, options));
        }
    }
}
