package objects.creatures;

import map.strategies.CombinedStrategy;
import objects.DynamicVisualObject;
import objects.stuff.Experience;
import objects.stuff.Health;
import objects.stuff.Stuff;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.awt.Color.BLACK;

public class CowardImp extends Mob implements DynamicVisualObject {


    private static final VisualPixel FIRST = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0197));
    private static final VisualPixel SECOND = new VisualPixel(
            new PixelData(true, 10, BLACK, 1, (char) 0x0198));
    private VisualPixel pixel;

    public CowardImp(Coord location) {
        this(location, 150, 50, (Math.random() < 0.5) ? FIRST : SECOND);
    }

    private CowardImp(Coord coord,
                      int speedDelay,
                      int maxHealth,
                      VisualPixel pixel) {
        super(coord);
        this.speedDelay = speedDelay;
        this.maxHealth = maxHealth;
        this.health = new AtomicInteger(maxHealth);
        this.pixel = pixel;
        strategy = new CombinedStrategy(this, CombinedStrategy.HeroDependingStrategyType.COWARD);
    }

    /**
     * Stuff, that appears when mob is murdered
     */
    protected Stuff generateItem() {
        if (Math.random() < 0.5) {
            return new Experience(location, (int) (Math.random() * 10));
        } else {
            return new Health(location, (int) (Math.random() * 10));
        }
    }

    /**
     * Doesnt attack
     */
    @Override
    public void attack(Direction direction) {
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
                .put("maxHealth", maxHealth)
                .put("speedDelay", speedDelay)
                .put("type", (pixel == FIRST) ? 0 : 1)
                .put("class", this.getClass().getName());
    }

    public static CowardImp restoreFromSnapshot(JSONObject jsonObject) {
        return (CowardImp) new CowardImp(new Coord(jsonObject.getInt("x"), jsonObject.getInt("y")),
                jsonObject.getInt("speedDelay"),
                jsonObject.getInt("maxHealth"),
                (jsonObject.getInt("type") == 0) ? FIRST : SECOND
        ).setHealth(jsonObject.getInt("health"));
    }

}
