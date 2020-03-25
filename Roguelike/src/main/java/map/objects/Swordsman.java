package map.objects;

import basicComponents.GameplayLogic;
import map.*;
import map.shapes.Shape;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;
import static map.MapGenerator.generate;

public class Swordsman extends Creature {
    private int speedDelay = 160;
    public Thread thread;

    public Swordsman(Coord coord) {
        init(coord, Shape.SINGLE_PIXEL_SHAPE);
        health = 10;
        thread = new Thread(() -> run());
    }

    public void startAfter(java.lang.Object object) {
        if (object != null) {
            synchronized (object) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        thread.start();
    }

    private void run() {
        while (true) {
            Direction direction = generate(new double[]{0.25, 0.25, 0.25, 0.25}, Direction.values());
            move(direction);
            int delay = (direction == Direction.UP || direction == Direction.DOWN) ? ((int) ((double) (speedDelay) * 4 / 3)) : speedDelay;
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                if (health < 0) {
                    break;
                }
                synchronized (GameplayLogic.pauseLock) {
                    try {
                        GameplayLogic.pauseLock.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
    }

    @Override
    public void attack(Direction direction) {

    }

    @Override
    public void die() {
        thread.interrupt();
        super.die();
    }

    @Override
    public void pause() {
        thread.interrupt();
    }

    @Override
    public void takeDamage(Damage damage) {
        health -= damage.value;
        if (health < 0) {
            die();
        }
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.SWORDSMAN);
        return map;
    }

}
