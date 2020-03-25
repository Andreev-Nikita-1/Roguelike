package map.objects;

import map.Coord;
import map.Direction;
import map.MapOfObjects;

import java.util.List;

import static map.MapOfObjects.mapLock;

public abstract class MovableObject extends Object {
    public void move(Direction direction) {
        synchronized (mapLock) {
            Coord shift = Coord.fromDirection(direction);
            List<Coord> coords = getCoords();
            for (Coord c : coords) {
                if (!MapOfObjects.inside(c.shifted(shift)) ||
                        MapOfObjects.isTaken(c.shifted(shift))) {
                    return;
                }
            }
            shift(shift);
        }
    }
}
