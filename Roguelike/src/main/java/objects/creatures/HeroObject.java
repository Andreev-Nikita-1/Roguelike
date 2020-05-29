package objects.creatures;

import basicComponents.AppLogic;
import basicComponents.Controller;
import gameplayOptions.DirectedOption;
import hero.Hero;
import hero.stats.AttackVisitor;
import hero.stats.DamageVisitor;
import hero.stats.RunVisitor;
import map.*;
import menuLogic.Menu;
import objects.DamageableObject;
import objects.InteractiveObject;
import objects.MapObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.AccessNeighbourhood;
import util.Coord;
import util.Direction;
import util.Pausable;

import java.awt.*;
import java.util.Map;

public class HeroObject extends OnePixelCreature {
    private int walkDelay = 100;
    private int runDelay = 50;
    private long lastMoveX;
    private long lastMoveY;
    public InteractiveObject interactiveObject;
    public Hero hero;


    public Coord getLocation() {
        return location;
    }

    public synchronized void die() {
        deleteFromMap();
        AppLogic.endGame();
    }

    public HeroObject(Coord coord) {
        super(coord);
        location = coord;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }


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

    public void makeAttack(DirectedOption option, long eventTime) {
        if (eventTime - lastAttackTime >= hero.stats.getAttackDelay()) {
            attack(option.direction);
            lastAttackTime = eventTime;
        }
    }

    @Override
    public void takeDamage(int damage) {
        hero.stats.accept(new DamageVisitor(damage));
        if (hero.stats.getHealth() <= 0) {
            die();
        }
    }

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
