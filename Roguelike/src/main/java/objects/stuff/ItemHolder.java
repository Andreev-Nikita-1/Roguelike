package objects.stuff;

import hero.items.Item;
import hero.stats.HealthVisitor;
import map.MapOfObjects;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ItemHolder extends Stuff {
    Item item;
    VisualPixel pixel;

    public ItemHolder(Coord location, Item item) {
        super(location);
        this.item = item;
        pixel = new VisualPixel(new PixelData(
                true, 8, item.getColor(), 1, item.getSymbol()));
    }

    @Override
    public void update() {
        if (location.equals(map.getHeroLocation())) {
            map.heroObject.hero.inventory.take(item);
            deleteFromMap();
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        pixelMap.put(location, pixel);
        return pixelMap;
    }

    @Override
    public JSONObject getSnapshot() {
        return super.getSnapshot()
                .put("item", item.getSnapshot());
    }

    public static ItemHolder restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return new ItemHolder(
                new Coord(jsonObject.getInt("x"),
                        jsonObject.getInt("y")),
                Item.restoreSnapshot(jsonObject.getJSONObject("item"))
        );
    }
}
