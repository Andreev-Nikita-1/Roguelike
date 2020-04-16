package map.objects;

import map.*;
import util.Coord;
import util.Direction;
import util.Waiter;
import util.Mover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static map.shapes.Shape.SINGLE_PIXEL_SHAPE;
import static util.Direction.LEFT;
import static util.Direction.RIGHT;

public class HeroObject extends Creature {
    private Mover walker;
    private Mover runner;
    private Waiter waiterAttack;
    private Coord attackingCoord;
    public List<DependingObject> dependingObjects = new ArrayList<>();

    public HeroObject(MapOfObjects map, Coord coord, int speedDelay) {
        super(map, coord, SINGLE_PIXEL_SHAPE);
        walker = new Mover(speedDelay);
        runner = new Mover((int) ((double) speedDelay / 2.5));
        waiterAttack = new Waiter(25);
    }

    public Coord getLocation() {
        return location;
    }

    public void makeWalkMovement(Direction direction) {
        makeMovement(walker, direction);
    }

    public void makeRunMovement(Direction direction) {
        makeMovement(runner, direction);
    }

    public void makeMovement(Mover mover, Direction direction) {
        Waiter waiter = (direction == LEFT || direction == RIGHT) ? mover.waiterX : mover.waiterY;
        if (waiter.isReady()) {
            if (move(direction)) {
                waiter.reset();
            }
        }
        for (DependingObject o : dependingObjects) {
            o.update();
        }
    }

    @Override
    public void takeDamage(Damage damage) {
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> pixels = new HashMap<>();
        pixels.put(location, LogicPixel.HERO);
        if (!waiterAttack.isReady()) {
            pixels.put(attackingCoord, LogicPixel.ATTACK);
        }
        return pixels;
    }

    @Override
    public void attack(Direction direction) {
        synchronized (map) {
            if (!waiterAttack.isReady()) {
                return;
            }
            Coord c = location.shifted(Coord.fromDirection(direction));
            attackingCoord = c;
            if (!map.inside(c)) return;
            MapObject o = map.getObject(c);
            if (o instanceof DamageableObject) {
                ((DamageableObject) o).takeDamage(new Damage(10));
            }
            waiterAttack.reset();
        }
    }

    @Override
    public HeroObject start() {
        waiterAttack.start();
        walker.start();
        runner.start();
        return this;
    }

    @Override
    public void pause() {
        waiterAttack.pause();
        walker.pause();
        runner.pause();
    }

    @Override
    public void unpause() {
        waiterAttack.unpause();
        walker.unpause();
        runner.unpause();
    }

    @Override
    public void kill() throws InterruptedException {
        walker.kill();
        runner.kill();
        waiterAttack.kill();
    }

    @Override
    public HeroObject attachToMap() {
        return (HeroObject) super.attachToMap();
    }

    @Override
    public void die() {

    }
}
