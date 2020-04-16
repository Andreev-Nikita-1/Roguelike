package map.objects;

import map.LogicPixel;
import map.MapOfObjects;
import map.shapes.CustomStaticShape;
import util.Coord;

import java.util.HashMap;
import java.util.Map;

public class Walls extends StaticObject {
    public Walls(MapOfObjects map, Coord coord, LogicPixel[][] array) {
        super(map, coord, new CustomStaticShape(array, true));
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> shapePixels = ((CustomStaticShape) shape).getPixels();
        Map<Coord, LogicPixel> pixelSet = new HashMap<>();
        for (Coord c : shapePixels.keySet()) {
            pixelSet.put(location.shifted(c), shapePixels.get(c));
        }
        return pixelSet;
    }
}
