package objects.stuff;

import hero.stats.HealthVisitor;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Candle extends Stuff {
    public static final VisualPixel SYMBOL = new VisualPixel(new PixelData(true,
            8, new Color(255, 215, 131), 1, (char) 0x00F4));

    public Candle(Coord location) {
        super(location);
    }

    @Override
    public void update() {
        if (location.equals(map.getHeroLocation())) {
            map.heroObject.hero.inventory.takeCandle();
            deleteFromMap();
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        pixelMap.put(location, SYMBOL);
        return pixelMap;
    }

    public static Candle restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return new Candle(
                new Coord(jsonObject.getInt("x"),
                        jsonObject.getInt("y"))
        );
    }
}
