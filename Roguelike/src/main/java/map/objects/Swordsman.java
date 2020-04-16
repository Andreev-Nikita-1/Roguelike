package map.objects;

import map.*;
import util.*;

import java.util.HashMap;
import java.util.Map;

import static map.shapes.Shape.SINGLE_PIXEL_SHAPE;
import static util.Direction.*;
import static util.Util.generate;

public class Swordsman extends Creature implements Actor {
    private int speedDelayX = 160;
    private int speedDelayY = (int) ((double) 160 * 4 / 3);
    private Waiter waiterWalk;
    private Waiter waiterAttack;
    private ThreadState threadState;

    public Swordsman(MapOfObjects map, Coord coord) {
        super(map, coord, SINGLE_PIXEL_SHAPE);
        health = 10;
        waiterWalk = new Waiter(speedDelayX);
        threadState = new ThreadState(this);
    }

    @Override
    public void act() throws InterruptedException {
        if (health <= 0) {
            die();
        }
        Direction direction = generate(new double[]{0.25, 0.25, 0.25, 0.25}, Direction.values());
        int delay = (direction == LEFT || direction == RIGHT) ? speedDelayX : speedDelayY;
        waiterWalk.waitUntilReady();
        if (move(direction)) {
            waiterWalk.reset(delay);
        }
    }

    @Override
    public void attack(Direction direction) {

    }

    @Override
    public void die() throws InterruptedException {
        deleteFromMap();
        kill();
    }

    @Override
    public Swordsman start() {
        waiterWalk.start();
        threadState.start();
        return this;
    }

    @Override
    public void pause() {
        threadState.pause();
        waiterWalk.pause();
    }

    @Override
    public void unpause() {
        waiterWalk.unpause();
        threadState.unpause();
    }

    @Override
    public void kill() throws InterruptedException {
        waiterWalk.kill();
        threadState.kill();
    }

    @Override
    public void takeDamage(Damage damage) {
        health -= damage.value;
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.SWORDSMAN);
        return map;
    }
}
