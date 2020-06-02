package objects.stuff;

import items.Item;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class, that contain item, that can be taken by hero
 */
public class ItemHolder extends Stuff {
    private Item item;
    private VisualPixel pixel;

    public ItemHolder(Coord location, Item item) {
        super(location);
        this.item = item;
        pixel = new VisualPixel(new PixelData(
                true, 8, item.getColor(), 1, item.getSymbol()));
    }

    /**
     * Puts item to hero inventory, when hero moves to coordinate of this
     */
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
