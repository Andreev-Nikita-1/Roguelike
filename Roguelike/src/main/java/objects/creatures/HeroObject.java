package objects.creatures;

import basicComponents.Controller;
import gameplayOptions.DirectedOption;
import map.*;
import menuLogic.Menu;
import menuLogic.MenuAction;
import objects.DamageableObject;
import objects.InteractiveObject;
import objects.MapObject;
import renderer.VisualPixel;
import util.AccessNeighbourhood;
import util.Coord;
import util.Direction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HeroObject extends OnePixelMob {
    public int speed;
    public int fortitude;
    private int walkDelayX;
    private int walkDelayY;
    private int runDelayX;
    private int runDelayY;
    private int attackDelay;
    private long lastMoveX;
    private long lastMoveY;
    public InteractiveObject interactiveObject;

    public Coord getLocation() {
        return location;
    }

    @Override
    public synchronized void die() {
        deleteFromMap();
        try {
            map.kill();
        } catch (InterruptedException e) {
        }
        Menu.mainMenu.deleteAction(0);
        Controller.drawMenu(Menu.mainMenu);
    }

    public HeroObject(MapOfObjects map, Coord coord) {
        super(map, coord);
        location = coord;
        updateSpeed(50);
        fortitude = 50;
        attackDelay = 100;
        power = 20;
        health = new AtomicInteger(100);
    }

    public void updateSpeed(int newSpeed) {
        speed = newSpeed;
        walkDelayX = 5000 / speed;
        walkDelayY = (int) ((double) walkDelayX * 4 / 3);
        runDelayX = walkDelayX / 3;
        runDelayY = walkDelayY / 3;
    }

    public void makeMovement(DirectedOption option, long eventTime) {
        Direction direction = option.direction;
        int delay = 0;
        if (direction.horizontal()) {
            switch (option.action) {
                case RUN:
                    delay = runDelayX;
                    break;
                case WALK:
                    delay = walkDelayX;
                    break;
            }
            if (eventTime - lastMoveX >= delay && move(direction)) {
                lastMoveX = eventTime;
            }
        } else {
            switch (option.action) {
                case RUN:
                    delay = runDelayY;
                    break;
                case WALK:
                    delay = walkDelayY;
                    break;
            }
            if (eventTime - lastMoveY >= delay && move(direction)) {
                lastMoveY = eventTime;
            }
        }
    }

    public void makeAttack(DirectedOption option, long eventTime) {
        if (eventTime - lastAttackTime >= attackDelay) {
            attack(option.direction);
            lastAttackTime = eventTime;
        }
    }

    @Override
    public void takeDamage(Damage damage) {
        health.addAndGet(-(int) ((double) damage.value * (50 / fortitude)));
        if (health.get() <= 0) {
            die();
        }
    }


    public int enemyHealth = 0;
    public long lastEnemyAttack = 0;

    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(Coord.fromDirection(direction));
        attackingCoords.clear();
        attackingCoords.add(c);
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            ((DamageableObject) o).takeDamage(new Damage(power));
            enemyHealth = ((Creature) o).health.get();
            lastEnemyAttack = System.currentTimeMillis();
        }
    }


    public void interactWith() {
        if (interactiveObject != null) {
            interactiveObject.interact();
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = super.getPixels(leftUp, rightDown);
        pixelMap.put(location, VisualPixel.HERO);
        return pixelMap;
    }

    @Override
    public int act() {
        return 1000000000;
    }

    @Override
    public HeroObject attachToMap() {
        super.attachToMap();
        map.heroAccessNeighbourhood = new AccessNeighbourhood(map, location, 10);
        return this;
    }

    @Override
    public void deleteFromMap() {
        map.heroAccessNeighbourhood.delete();
        super.deleteFromMap();
    }
}
