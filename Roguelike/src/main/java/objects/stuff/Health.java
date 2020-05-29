package objects.stuff;

import basicComponents.Game;
import hero.items.Item;
import hero.stats.HealthVisitor;
import map.MapOfObjects;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;
import util.TimeIntervalActor;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Health extends Stuff implements TimeIntervalActor {


    private static final Color COLOR = new Color(175, 0, 0);
    private static final VisualPixel HEART_1 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x0156));
    private static final VisualPixel HEART_2 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x0157));
    private static final VisualPixel HEART_3 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x0158));
    private static final VisualPixel HEART_4 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x0159));
    private static final VisualPixel HEART_5 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x015A));

    private int value;

    public Health(Coord location, int value) {
        super(location);
        this.value = value;
    }

    @Override
    public synchronized void update() {
        if (location.equals(map.getHeroLocation())) {
            map.heroObject.hero.stats.accept(new HealthVisitor(value));
            deleteFromMap();
        }
    }

    private VisualPixel getPixel() {
        if (value >= 10) return HEART_1;
        if (value >= 7) return HEART_2;
        if (value >= 5) return HEART_3;
        if (value >= 3) return HEART_4;
        return HEART_5;
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        pixelMap.put(location, getPixel().brighter(1 + shift));
        return pixelMap;
    }


    private double phase = 0;
    private double shift = 0;

    @Override
    public int act() {
        shift = 0.1 * (1 + (Math.floor(phase) - phase));
        phase += 0.1 / (1 + value / 10.0);
        return 50;
    }

    @Override
    public void setActive(boolean active) {
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Game getGame() {
        return map.game;
    }


    @Override
    public JSONObject getSnapshot() {
        return super.getSnapshot()
                .put("value", value);
    }

    public static Health restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return new Health(
                new Coord(jsonObject.getInt("x"),
                        jsonObject.getInt("y")),
                jsonObject.getInt("value")
        );
    }
}
