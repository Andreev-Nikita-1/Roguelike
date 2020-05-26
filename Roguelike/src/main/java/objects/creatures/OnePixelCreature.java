package objects.creatures;

import map.MapOfObjects;
import objects.*;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class OnePixelCreature extends MapObject implements DynamicVisualObject, MovableObject, AttackingObject, DamageableObject {

    protected Coord location;
    protected Map<Coord, Integer> attackingCoords = new HashMap<>();
    protected volatile long lastAttackTime = 0;

    public Coord getLocation() {
        return location;
    }

    public OnePixelCreature(MapOfObjects map, Coord location) {
        super(map);
        this.location = location;
    }

    @Override
    public OnePixelCreature attachToMap() {
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

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        if (System.currentTimeMillis() - lastAttackTime <= attackVisualizationPeriod) {
            for (Coord c : attackingCoords.keySet()) {
                if (c.between(leftUp, rightDown)) {
                    pixelMap.put(c, VisualPixel.attack(attackingCoords.get(c)));
                }
            }
        }
        return pixelMap;
    }
}
