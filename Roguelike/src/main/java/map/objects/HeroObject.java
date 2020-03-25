package map.objects;

import basicComponents.GameplayLogic;
import map.*;
import map.shapes.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static map.MapOfObjects.mapLock;

public class HeroObject extends Creature {
    private int speedDelay;
    private Waiter waiterWalkX;
    private Waiter waiterWalkY;
    private Waiter waiterRunX;
    private Waiter waiterRunY;
    private Waiter waiterAttack;
    private Coord attackingCoord;
    public List<DependingObject> dependingObjects = new ArrayList<>();

    public HeroObject(Coord coord, int speedDelay) {
        init(coord, Shape.SINGLE_PIXEL_SHAPE);
        this.speedDelay = speedDelay;
        waiterWalkX = new Waiter(speedDelay);
        waiterWalkY = new Waiter((int) ((double) speedDelay * 4 / 3));
        waiterRunX = new Waiter((int) ((double) speedDelay / 2.5));
        waiterRunY = new Waiter((int) ((double) speedDelay * 4 / 7));
        waiterAttack = new Waiter(25);
        new Thread(waiterWalkX).start();
        new Thread(waiterWalkY).start();
        new Thread(waiterRunX).start();
        new Thread(waiterRunY).start();
        new Thread(waiterAttack).start();
    }

    public Coord getLocation() {
        return location;
    }

    public void makeMovement(GameplayLogic.GameplayOption option) {
        Waiter waiter;
        Direction direction;
        switch (option) {
            case WALK_UP:
                direction = Direction.UP;
                waiter = waiterWalkY;
                break;
            case WALK_DOWN:
                direction = Direction.DOWN;
                waiter = waiterWalkY;
                break;
            case WALK_LEFT:
                direction = Direction.LEFT;
                waiter = waiterWalkX;
                break;
            case WALK_RIGHT:
                direction = Direction.RIGHT;
                waiter = waiterWalkX;
                break;
            case RUN_UP:
                direction = Direction.UP;
                waiter = waiterRunY;
                break;
            case RUN_DOWN:
                direction = Direction.DOWN;
                waiter = waiterRunY;
                break;
            case RUN_LEFT:
                direction = Direction.LEFT;
                waiter = waiterRunX;
                break;
            case RUN_RIGHT:
                direction = Direction.RIGHT;
                waiter = waiterRunX;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + option);
        }
        synchronized (waiter) {
            if (!waiter.ready) return;
            move(direction);
            waiter.ready = false;
            waiter.notify();
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
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.HERO);
        if (!waiterAttack.ready) {
            map.put(attackingCoord, LogicPixel.ATTACK);
        }
        return map;
    }

    @Override
    public void attack(Direction direction) {
        synchronized (mapLock) {
            synchronized (waiterAttack) {
                if (!waiterAttack.ready) return;
                Coord c = location.shifted(Coord.fromDirection(direction));
                attackingCoord = c;
                if (!MapOfObjects.inside(c)) return;
                Object o = MapOfObjects.getObject(c);
                if (o instanceof DamageableObject) {
                    ((DamageableObject) o).takeDamage(new Damage(10));
                }
                waiterAttack.ready = false;
                waiterAttack.notify();
            }
        }
    }

    @Override
    public void pause() {
    }
}
