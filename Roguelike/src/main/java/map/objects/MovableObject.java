package map.objects;

import map.shapes.Shape;
import util.Coord;
import util.Direction;
import map.MapOfObjects;

public abstract class MovableObject extends MapObject {
    public MovableObject(MapOfObjects map, Coord coord, Shape shape) {
        super(map, coord, shape);
    }

    public boolean move(Direction direction) {
        synchronized (map) {
            Coord shift = Coord.fromDirection(direction);
            if (shape.canPlace(map, location.shifted(shift))) {
                shift(shift);
                return true;
            }
            return false;
        }
    }
}
