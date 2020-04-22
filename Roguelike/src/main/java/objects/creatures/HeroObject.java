package objects.creatures;

import gameplayOptions.DirectedOption;
import map.*;
import objects.DamageableObject;
import objects.DependingObject;
import objects.InteractiveObject;
import objects.MapObject;
import objects.creatures.OnePixelMob;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeroObject extends OnePixelMob {
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
        super.deleteFromMap();
        kill();
    }

    public HeroObject(MapOfObjects map, Coord coord) {
        super(map, coord);
        location = coord;
        walkDelayX = 100;
        walkDelayY = (int) ((double) walkDelayX * 4 / 3);
        runDelayX = walkDelayX / 3;
        runDelayY = walkDelayY / 3;
        attackDelay = 100;
        power = 20;
        health = 100;
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
        health -= damage.value;
        if (health <= 0) {
            die();
        }
    }

    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(Coord.fromDirection(direction));
        attackingCoords.clear();
        attackingCoords.add(c);
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            ((DamageableObject) o).takeDamage(new Damage(power));
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
}
