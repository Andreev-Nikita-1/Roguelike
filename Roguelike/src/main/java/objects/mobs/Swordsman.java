package objects.mobs;

import map.*;
import objects.VisualObject;
import objects.mobs.OnePixelMob;
import renderer.VisualPixel;
import util.*;

import java.util.HashMap;
import java.util.Map;

import static util.Direction.LEFT;
import static util.Direction.RIGHT;
import static util.Util.generate;

public class Swordsman extends OnePixelMob implements VisualObject {
    private volatile int health;
    private volatile int speedDelayX = 160;
    private volatile int speedDelayY = (int) ((double) 160 * 4 / 3);

    public Swordsman(MapOfObjects map, Coord coord) {
        super(map, coord);
        health = 10;
    }

    @Override
    public void attack(Direction direction) {

    }

    public void die() throws InterruptedException {
        deleteFromMap();
        kill();
    }

    @Override
    public int act() {
        Direction direction = generate(new double[]{0.25, 0.25, 0.25, 0.25}, Direction.values());
        if (move(direction)) {
            return (direction == LEFT || direction == RIGHT) ? speedDelayX : speedDelayY;
        }
        return 0;
    }

    @Override
    public void takeDamage(Damage damage) {
        health -= damage.value;
        if (health <= 0) {
            try {
                die();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> map = new HashMap<>();
        map.put(location, VisualPixel.SWORDSMAN);
        return map;
    }
}
