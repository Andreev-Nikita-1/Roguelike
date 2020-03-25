package map.objects;

import basicComponents.GameplayLogic;
import map.*;
import map.shapes.Shape;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Swordsman extends Creature {
    private int speedDelay = 160;
    public Thread thread;

    @Override
    public void attack(Direction direction) {

    }

    @Override
    public void takeDamage(Damage damage) {
        health -= damage.value;
    }

    public Swordsman(int x, int y) {
        init(new Coord(x, y), Shape.SINGLE_PIXEL_SHAPE);
        health = 10;
        thread = new Thread(() -> run());
        thread.start();
    }

    private void run() {
        while (true) {
            if (health < 0) {
                die();
            }
            Direction direction = LogicPixel.generate(new double[]{0.25, 0.25, 0.25, 0.25}, Direction.getDirections());
            move(direction);
            int delay = (direction == Direction.UP || direction == Direction.DOWN) ? ((int) ((double) (speedDelay) * 4 / 3)) : speedDelay;
            try {
                sleep(delay);
            } catch (InterruptedException e) {
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
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.SWORDSMAN);
        return map;
    }

    @Override
    public void pause() {
        thread.interrupt();
    }
}
