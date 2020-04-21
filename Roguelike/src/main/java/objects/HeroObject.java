package objects;

import gameplayOptions.DirectedOption;
import map.*;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;
import util.Pausable;
import util.Waiter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Direction.LEFT;
import static util.Direction.RIGHT;

public class HeroObject extends PausableObject implements VisualObject, MovableObject, AttackingObject, DamageableObject {
    private int walkDelayX;
    private int walkDelayY;
    private int runDelayX;
    private int runDelayY;
    private int attackDelay;
    private Waiter walker;
    private Waiter waiterAttack;
    private Coord attackingCoord;
    private Coord location;
    public List<DependingObject> dependingObjects = new ArrayList<>();
    public InteractiveObject interactiveObject;

    public Coord getLocation() {
        return location;
    }

    public HeroObject(MapOfObjects map, Coord coord, int speedDelay) {
        super(map);
        location = coord;
        walkDelayX = speedDelay;
        walkDelayY = (int) ((double) speedDelay * 4 / 3);
        runDelayX = walkDelayX / 3;
        runDelayY = walkDelayY / 3;
        attackDelay = 25;
        walker = new Waiter(speedDelay);
        waiterAttack = new Waiter(attackDelay);
    }

    public void makeMovement(DirectedOption option) {
        Direction direction = option.direction;
        int delay = 0;
        switch (option.action) {
            case WALK:
                delay = (direction == LEFT || direction == RIGHT) ? walkDelayX : walkDelayY;
                break;
            case RUN:
                delay = (direction == LEFT || direction == RIGHT) ? runDelayX : runDelayY;
                break;
        }
        if (walker.isReady() || walker.getDelay() != delay && walker.isActive()) {
            if (move(direction)) {
                walker.reset(delay);
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
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixels = new HashMap<>();
        pixels.put(location, VisualPixel.HERO);
        if (!waiterAttack.isReady()) {
            pixels.put(attackingCoord, VisualPixel.ATTACK);
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
        return this;
    }

    @Override
    public void pause() {
        waiterAttack.pause();
        walker.pause();
    }

    @Override
    public void unpause() {
        waiterAttack.unpause();
        walker.unpause();
    }

    @Override
    public void kill() throws InterruptedException {
        walker.kill();
        waiterAttack.kill();
    }

    @Override
    public HeroObject attachToMap() {
        super.attachToMap();
        map.setObject(this, location);
        map.dynamicObjects.add(this);
        return this;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        map.unsetObject(this, location);
        map.dynamicObjects.remove(this);
    }

    @Override
    public boolean move(Direction direction) {
        synchronized (map) {
            Coord shift = Coord.fromDirection(direction);
            Coord nextLocation = location.shifted(shift);
            if (map.inside(nextLocation) && !map.isTaken(nextLocation)) {
                map.unsetObject(this, location);
                location.shift(shift);
                map.setObject(this, location);
                return true;
            }
            return false;
        }
    }

    public void interactWith() {
        if (interactiveObject != null) {
            interactiveObject.interact();
        }
    }
}
