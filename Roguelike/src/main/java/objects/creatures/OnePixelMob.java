package objects.creatures;

import map.MapOfObjects;
import util.Coord;
import util.Direction;

public abstract class OnePixelMob extends Creature {

    public OnePixelMob(MapOfObjects map, Coord location) {
        super(map, location);
    }

    @Override
    public OnePixelMob attachToMap() {
        super.attachToMap();
        map.setObject(this, location);
        return this;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        map.unsetObject(this, location);
    }

    @Override
    public synchronized boolean move(Direction direction) {
        if (direction == null) {
            return false;
        }
        Coord shift = Coord.fromDirection(direction);
        Coord nextLocation = location.shifted(shift);
        if (map.accesible(nextLocation)) {
            map.getCoordLock(nextLocation).lock();
            try {
                if (map.accesible(nextLocation)) {
                    map.unsetObject(this, location);
                    location.shift(shift);
                    map.setObject(this, location);
                    return true;
                }
            } finally {
                map.getCoordLock(nextLocation).unlock();
            }
        }
        return false;
    }
}
