package objects.creatures;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import map.*;
import map.strategies.CombinedStrategy;
import objects.DamageableObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import renderer.VisualPixel;
import util.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class Swordsman extends OnePixelMob implements DynamicVisualObject {
    private volatile int speedDelayX = 200;
    private volatile int speedDelayY = (int) ((double) 200 * 4 / 3);
    private volatile int attackDelay = 100;


    public Swordsman(MapOfObjects map, Coord coord) {
        super(map, coord);
        health = new AtomicInteger(30);
        power = 10;
        strategy = new CombinedStrategy(this);
    }

    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(Coord.fromDirection(direction));
        attackingCoords.clear();
        attackingCoords.add(c);
        lastAttackTime = System.currentTimeMillis();
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            ((DamageableObject) o).takeDamage(new Damage(power));
        }
    }

    @Override
    public synchronized void die() {
        map.getCoordLock(location).lock();
        try {
            deleteFromMap();
            kill();
        } finally {
            map.getCoordLock(location).unlock();
        }
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
                        return ((DirectedOption) action).direction.horizontal() ? speedDelayX / d : speedDelayY / d;
                    } else {
                        return 0;
                    }
                case ATTACK:
                    attack(((DirectedOption) action).direction);
                    return attackDelay;
            }
        }
        return 0;
    }

    @Override
    public void takeDamage(Damage damage) {
        health.addAndGet(-damage.value);
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
