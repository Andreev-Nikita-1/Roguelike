package objects.creatures;

import map.*;
import objects.DamageableObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import renderer.VisualPixel;
import util.*;

import java.util.Map;

import static util.Util.generate;

public class Swordsman extends OnePixelMob implements DynamicVisualObject {
    private volatile int speedDelayX = 160;
    private volatile int speedDelayY = (int) ((double) 160 * 4 / 3);
    private volatile int attackDelay = 100;

    public Swordsman(MapOfObjects map, Coord coord) {
        super(map, coord);
        health = 30;
        power = 10;
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
        deleteFromMap();
        kill();
    }

    @Override
    public int act() {
        Direction direction = generate(new double[]{0.25, 0.25, 0.25, 0.25}, Direction.values());
        if (move(direction)) {
            return direction.horizontal() ? speedDelayX : speedDelayY;
        } else {
            attack(direction);
            return attackDelay;
        }
    }

    @Override
    public void takeDamage(Damage damage) {
        health -= damage.value;
        if (health <= 0) {
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
