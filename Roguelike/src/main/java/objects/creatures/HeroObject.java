package objects.creatures;

import basicComponents.AppLogic;
import gameplayOptions.DirectedOption;
import hero.Hero;
import hero.stats.AttackVisitor;
import hero.stats.DamageVisitor;
import hero.stats.RunVisitor;
import map.*;
import objects.DamageableObject;
import objects.InteractiveObject;
import objects.MapObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.AccessNeighbourhood;
import util.Coord;
import util.Direction;

import java.awt.*;
import java.util.Map;

/**
 * Class for hero object on map
 */
public class HeroObject extends OnePixelCreature {
    private int walkDelay = 100;
    private int runDelay = 50;
    private long lastMoveX;
    private long lastMoveY;
    public InteractiveObject interactiveObject;
    public Hero hero;

    /**
     * Returns current location
     */
    public Coord getLocation() {
        return location;
    }

    /**
     * Die method
     */
    synchronized void die() {
        deleteFromMap();
        AppLogic.endGame();
    }

    public HeroObject(Coord coord) {
        super(coord);
        location = coord;
    }

    /**
     * Sets hero
     */
    public void setHero(Hero hero) {
        this.hero = hero;
    }


    /**
     * Method, called, when key for moving os pressed
     */
    public void makeMovement(DirectedOption option, long eventTime) {
        Direction direction = option.direction;
        int delay = 0;
        switch (option.action) {
            case RUN:
                if (hero.stats.getStamina() > 0) {
                    hero.stats.accept(new RunVisitor());
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

    /**
     * Method, called, when key for attacking is pressed
     */
    public void makeAttack(DirectedOption option, long eventTime) {
        if (eventTime - lastAttackTime >= hero.stats.getAttackDelay()) {
            attack(option.direction);
            lastAttackTime = eventTime;
        }
    }

    /**
     * Taking damage method
     */
    @Override
    public void takeDamage(int damage) {
        hero.stats.accept(new DamageVisitor(damage));
        if (hero.stats.getHealth() <= 0) {
            die();
        }
    }

    /**
     * Attack method
     */
    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(direction);
        attackingCoords.clear();
        attackingCoords.put(c, hero.stats.getPower());
        lastAttackTime = System.currentTimeMillis();
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            hero.stats.accept(new AttackVisitor());
            ((DamageableObject) o).takeDamage(hero.stats.getPower());
        }
    }

    /**
     * Interact with interactible object. For doors only for now
     */
    public void interactWith() {
        if (interactiveObject != null) {
            interactiveObject.interact();
        }
    }


    private static final Color HERO_COLOR = Color.BLACK;
    private static final VisualPixel HERO_PIXEL = new VisualPixel(
            new PixelData(true, 10, HERO_COLOR, 1, (char) 0x0146));

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = super.getPixels(leftUp, rightDown);
        pixelMap.put(location, HERO_PIXEL);
        return pixelMap;
    }

    @Override
    public HeroObject attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        map.heroAccessNeighbourhood = new AccessNeighbourhood(map, location, 10);
        return this;
    }

    @Override
    public void deleteFromMap() {
        map.heroAccessNeighbourhood.delete();
        super.deleteFromMap();
    }
}
