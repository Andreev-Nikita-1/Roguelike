package objects.stuff;

import basicComponents.Game;
import hero.stats.ExperienceVisitor;
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

public class Experience extends Stuff implements TimeIntervalActor {

    private static final Color COLOR = new Color(100, 252, 151);
    private static final VisualPixel EXP_1 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x0169));
    private static final VisualPixel EXP_2 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x016A));
    private static final VisualPixel EXP_3 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x016B));
    private static final VisualPixel EXP_4 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x016C));
    private static final VisualPixel EXP_5 = new VisualPixel(
            new PixelData(true, 8, COLOR, 1, (char) 0x016D));

    private int value;

    public Experience(Coord location, int value) {
        super(location);
        this.value = value;
    }

    @Override
    public synchronized void update() {
        if (location.equals(map.getHeroLocation())) {
            map.heroObject.hero.stats.accept(new ExperienceVisitor(value));
            deleteFromMap();
        }
    }

    private VisualPixel getPixel() {
        if (value >= 10) return EXP_1;
        if (value >= 7) return EXP_2;
        if (value >= 5) return EXP_3;
        if (value >= 3) return EXP_4;
        return EXP_5;
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
        shift = 0.1 * Math.sin(phase);
        phase += 0.5 / (1 + value / 10.0);
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

    public static Experience restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return new Experience(
                new Coord(jsonObject.getInt("x"),
                        jsonObject.getInt("y")),
                jsonObject.getInt("value")
        );
    }
}

