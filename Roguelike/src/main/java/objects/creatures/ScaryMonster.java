package objects.creatures;

import basicComponents.Game;
import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import map.strategies.CombinedStrategy;
import map.strategies.ConfusedStrategy;
import objects.DamageableObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import objects.stuff.Experience;
import objects.stuff.Health;
import objects.stuff.Stuff;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import static java.awt.Color.BLACK;


public class ScaryMonster extends Mob implements DynamicVisualObject {
    private VisualPixel pixel;

    private Type type;


    private static final VisualPixel GHOST_1 = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x017C));
    private static final VisualPixel GHOST_2 = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x017D));
    private static final VisualPixel GHOST_3 = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x017E));
    private static final VisualPixel GHOST_TRANSPARENT = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x017F));
    private static final VisualPixel GHOST_LITTLE = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0180));
    private static final VisualPixel GRIM_REAPER_1 = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0181));
    private static final VisualPixel GRIM_REAPER_2 = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0182));
    private static final VisualPixel GRIM_REAPER_3 = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0183));
    private static final VisualPixel IMP = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0184));
    private static final VisualPixel DEMENTOR = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0185));


    public enum Type {
        GHOST_1, GHOST_2, GHOST_3, GHOST_TRANSPARENT, GHOST_LITTLE, GRIM_REAPER_1, GRIM_REAPER_2, GRIM_REAPER_3, IMP, DEMENTOR
    }

    /**
     * Return new instance of given type
     */
    public static ScaryMonster newMonster(Coord coord, Type type) {
        ScaryMonster answer = null;
        switch (type) {
            case GHOST_1:
                answer = new ScaryMonster(coord, 300, 200, 50, 10, GHOST_1);
                break;
            case GHOST_2:
                answer = new ScaryMonster(coord, 300, 200, 50, 10, GHOST_2);
                break;
            case GHOST_3:
                answer = new ScaryMonster(coord, 300, 200, 50, 10, GHOST_3);
                break;
            case GHOST_TRANSPARENT:
                answer = new ScaryMonster(coord, 250, 150, 1, 5, GHOST_TRANSPARENT);
                break;
            case GHOST_LITTLE:
                answer = new ScaryMonster(coord, 250, 100, 25, 5, GHOST_LITTLE);
                break;
            case GRIM_REAPER_1:
                answer = new ScaryMonster(coord, 400, 300, 100, 30, GRIM_REAPER_1);
                break;
            case GRIM_REAPER_2:
                answer = new ScaryMonster(coord, 400, 300, 100, 30, GRIM_REAPER_2);
                break;
            case GRIM_REAPER_3:
                answer = new ScaryMonster(coord, 350, 300, 75, 25, GRIM_REAPER_3);
                break;
            case IMP:
                answer = new ScaryMonster(coord, 200, 200, 40, 10, IMP);
                break;
            case DEMENTOR:
                answer = new ScaryMonster(coord, 500, 50, 150, 5, DEMENTOR);
                break;
        }
        answer.type = type;
        return answer;
    }

    private ScaryMonster(Coord coord,
                         int speedDelay,
                         int attackDelay,
                         int maxHealth,
                         int power,
                         VisualPixel pixel) {
        super(coord);
        this.speedDelay = speedDelay;
        this.attackDelay = attackDelay;
        this.maxHealth = maxHealth;
        this.health = new AtomicInteger(maxHealth);
        this.power = power;
        this.pixel = pixel;
        strategy = new CombinedStrategy(this, CombinedStrategy.HeroDependingStrategyType.AGRESSIVE);
    }


    /**
     * Attack method
     */
    @Override
    public void attack(Direction direction) {
        Coord c = location.shifted(direction);
        attackingCoords.clear();
        attackingCoords.put(c, power);
        lastAttackTime = System.currentTimeMillis();
        if (!map.inside(c)) return;
        MapObject o = map.getObject(c);
        if (o instanceof DamageableObject) {
            ((DamageableObject) o).takeDamage(power);
        }
    }


    /**
     * Stuff, that appears when mob is slayed
     */
    @Override
    protected Stuff generateItem() {
        if (Math.random() < 0.5) {
            return new Experience(location, (int) (Math.random() * 3 * power));
        } else {
            return new Health(location, (int) (Math.random() * 3 * power));
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = super.getPixels(leftUp, rightDown);
        pixelMap.put(location, pixel);
        return pixelMap;
    }

    @Override
    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("x", location.x)
                .put("y", location.y)
                .put("health", health)
                .put("type", type.ordinal())
                .put("class", this.getClass().getName());
    }

    public static ScaryMonster restoreFromSnapshot(JSONObject jsonObject) {
        return (ScaryMonster) newMonster(
                new Coord(jsonObject.getInt("x"), jsonObject.getInt("y")),
                Type.values()[jsonObject.getInt("type")]
        ).setHealth(jsonObject.getInt("health"));
    }
}
