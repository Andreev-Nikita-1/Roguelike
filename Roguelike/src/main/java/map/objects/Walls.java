package map.objects;

import map.Coord;
import map.shapes.Shape;
import map.LogicPixel;

import java.util.HashMap;

public class Walls extends StaticObject {
    public Walls(Coord coord, Shape shape, LogicPixel pixel) {
        init(coord, shape);
        pixelSet = new HashMap<>();
        for (Coord c : getCoords()) {
            pixelSet.put(c, pixel);
        }
    }
}
