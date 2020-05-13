package objects.creatures;

import basicComponents.Controller;
import gameplayOptions.DirectedOption;
import inventory.Hero;
import map.*;
import menuLogic.Menu;
import objects.DamageableObject;
import objects.InteractiveObject;
import objects.MapObject;
import renderer.VisualPixel;
import util.AccessNeighbourhood;
import util.Coord;
import util.Direction;
import util.Pausable;

import java.util.Map;

public class HeroObject extends OnePixelCreature implements Pausable {
    private int walkDelayX;
    private int walkDelayY;
    private int runDelayX;
    private int runDelayY;
    private long lastMoveX;
    private long lastMoveY;
    public InteractiveObject interactiveObject;
    public Hero hero;


    public Coord getLocation() {
        return location;
    }

    public synchronized void die() {
        deleteFromMap();
        try {
            map.kill();
        } catch (InterruptedException e) {
        }
        Menu.mainMenu.deleteAction(0);
        Controller.drawMenu(Menu.mainMenu);
    }

    public HeroObject(MapOfObjects map, Coord coord, Hero hero) {
        super(map, coord);
        this.hero = hero;
        hero.heroMap = map;
        location = coord;
        updateSpeed(hero.speed);
    }

    public void updateSpeed(int speed) {
        walkDelayX = 5000 / speed;
        walkDelayY = (int) ((double) walkDelayX * 4 / 3);
        runDelayX = walkDelayX / 2;
        runDelayY = walkDelayY / 2;
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
        if (eventTime - lastAttackTime >= hero.attackDelay) {
            attack(option.direction);
            lastAttackTime = eventTime;
        }
    }

    @Override
    public void takeDamage(Damage damage) {
        hero.health.addAndGet(-(int) ((double) damage.value * (50 / hero.fortitude)));
        if (hero.health.get() <= 0) {
            die();
        }
    }

    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(direction);
        attackingCoords.clear();
        attackingCoords.add(c);
        lastAttackTime = System.currentTimeMillis();
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            ((DamageableObject) o).takeDamage(new Damage(hero.power));
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
    public HeroObject start() {
        hero.start();
        return this;
    }

    @Override
    public void pause() {
        hero.pause();
    }

    @Override
    public void unpause() {
        hero.unpause();
    }

    @Override
    public void kill() {
        hero.kill();
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
