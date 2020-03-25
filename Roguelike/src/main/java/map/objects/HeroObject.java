package map.objects;

import basicComponents.GameplayLogic;
import hero.HeroLogic;
import map.*;
import map.shapes.Shape;

import java.util.HashMap;
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

    public HeroObject(int x, int y, int speedDelay) {
        init(new Coord(x, y), Shape.SINGLE_PIXEL_SHAPE);
        this.speedDelay = speedDelay;
        waiterWalkX = new Waiter(speedDelay);
        waiterWalkY = new Waiter((int) ((double) speedDelay * 4 / 3));
        waiterRunX = new Waiter((int) ((double) speedDelay / 2.5));
        waiterRunY = new Waiter((int) ((double) speedDelay * 4 / 7));
        waiterAttack = new Waiter(15);
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
    }

    @Override
    public void takeDamage(Damage damage) {
        HeroLogic.takeDamage(damage);
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.HERO);

        for (int i = leftUp.x; i < rightDown.x; i++) {
            for (int j = leftUp.y; j < rightDown.y; j++) {
                int d = (int) (Math.pow((location.x - i), 2) + Math.pow(2 * (location.y - j), 2));

                if (d > 300) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_FULL);
                } else if (d > 150) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_3);
                } else if (d > 75) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_2);
                } else if (d > 15) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_1);
                }
            }
        }
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
