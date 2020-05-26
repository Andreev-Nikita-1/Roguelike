package objects.creatures;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import map.*;
import map.strategies.CombinedStrategy;
import objects.DamageableObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import objects.stuff.Health;
import objects.stuff.Stuff;
import renderer.VisualPixel;
import util.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;


public class Swordsman extends Mob implements DynamicVisualObject {
    private volatile int speedDelay = 200;
    private volatile int attackDelay = 100;


    public Swordsman(MapOfObjects map, Coord coord) {
        super(map, coord);
        health = new AtomicInteger(60);
        power = 5;
        strategy = new CombinedStrategy(this);
    }

    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(direction);
        attackingCoords.clear();
        attackingCoords.put(c, power);
        lastAttackTime = System.currentTimeMillis();
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            ((DamageableObject) o).takeDamage(power);
        }
    }

    @Override
    public synchronized void die() {
        Lock lock = map.getCoordLock(location);
        lock.lock();
        try {
            deleteFromMap();
            generateItem().attachToMap();
        } finally {
            lock.unlock();
        }
    }

    private Stuff generateItem() {
        return new Health(map, location, (int) (Math.random() * 3 * power));
    }

    @Override
    public int act() {
        GameplayOption action = strategy.getAction();
        if (action == GameplayOption.NOTHING) {
            return 10;
        }
        if (action instanceof DirectedOption) {
            switch (((DirectedOption) action).action) {
                case WALK:
                case RUN:
                    if (move(((DirectedOption) action).direction)) {
                        int d = (((DirectedOption) action).action == DirectedOption.Action.WALK) ? 1 : 2;
                        return speedDelay / d;
                    } else {
                        return 10;
                    }
                case ATTACK:
                    attack(((DirectedOption) action).direction);
                    return attackDelay;
            }
        }
        return 10;
    }

    @Override
    public void takeDamage(int damage) {
        health.addAndGet(-damage);
        if (health.get() <= 0) {
            die();
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = super.getPixels(leftUp, rightDown);
        pixelMap.put(location, VisualPixel.SWORDSMAN);
        return pixelMap;
    }
}
