package objects.creatures;

import map.MapOfObjects;
import map.strategies.Strategy;
import objects.*;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Creature extends RunnableObject implements DynamicVisualObject, MovableObject, AttackingObject, DamageableObject {

    public Coord getLocation() {
        return location;
    }

    protected Coord location;
    protected Set<Coord> attackingCoords = new HashSet<>();
    protected volatile long lastAttackTime = 0;
    public AtomicInteger health;
    public volatile int power;

    public Strategy strategy;

    public abstract void die();

    public Creature(MapOfObjects map, Coord location) {
        super(map);
        this.location = location;
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        if (System.currentTimeMillis() - lastAttackTime <= attackVisualizationPeriod) {
            for (Coord c : attackingCoords) {
                if (c.between(leftUp, rightDown)) {
                    pixelMap.put(c, VisualPixel.ATTACK);
                }
            }
        }
        return pixelMap;
    }
}
