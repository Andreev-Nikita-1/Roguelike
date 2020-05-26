package objects.creatures;

import basicComponents.Controller;
import gameplayOptions.DirectedOption;
import hero.Inventory;
import hero.stats.AttackVisitor;
import hero.stats.DamageVisitor;
import hero.stats.RunVisitor;
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

public class HeroObject extends OnePixelCreature {
    private int walkDelay = 100;
    private int runDelay = 50;
    private long lastMoveX;
    private long lastMoveY;
    public InteractiveObject interactiveObject;
    public Inventory inventory;


    public Coord getLocation() {
        return location;
    }

    public synchronized void die() {
        deleteFromMap();
        Pausable.killGame();
        Menu.mainMenu.deleteAction(0);
        Controller.drawMenu(Menu.mainMenu);
    }

    public HeroObject(MapOfObjects map, Coord coord, Inventory inventory) {
        super(map, coord);
        this.inventory = inventory;
        inventory.heroMap = map;
        location = coord;
    }


    public void makeMovement(DirectedOption option, long eventTime) {
        Direction direction = option.direction;
        int delay = 0;
        switch (option.action) {
            case RUN:
                if (inventory.stats.getStamina() > 0) {
                    inventory.stats.accept(new RunVisitor());
                    delay = runDelay;
                } else {
                    delay = walkDelay;
                }
                break;
            case WALK:
                delay = walkDelay;
                break;
        }
        if (direction.horizontal()) {
            if (eventTime - lastMoveX >= delay && move(direction)) {
                lastMoveX = eventTime;
            }
        } else {
            if (eventTime - lastMoveY >= delay && move(direction)) {
                lastMoveY = eventTime;
            }
        }
    }

    public void makeAttack(DirectedOption option, long eventTime) {
        if (eventTime - lastAttackTime >= inventory.stats.getAttackDelay()) {
            attack(option.direction);
            lastAttackTime = eventTime;
        }
    }

    @Override
    public void takeDamage(int damage) {
        inventory.stats.accept(new DamageVisitor(damage));
        if (inventory.stats.getHealth() <= 0) {
            die();
        }
    }

    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(direction);
        attackingCoords.clear();
        attackingCoords.put(c, inventory.stats.getPower());
        lastAttackTime = System.currentTimeMillis();
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            inventory.stats.accept(new AttackVisitor());
            ((DamageableObject) o).takeDamage(inventory.stats.getPower());
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
