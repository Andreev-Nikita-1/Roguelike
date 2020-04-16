package map.objects;

import map.MapOfObjects;
import map.shapes.Shape;
import util.Coord;
import map.LogicPixel;

import java.util.Map;

public abstract class StaticObject extends MapObject {
    public StaticObject(MapOfObjects map, Coord coord, Shape shape) {
        super(map, coord, shape);
    }
}
