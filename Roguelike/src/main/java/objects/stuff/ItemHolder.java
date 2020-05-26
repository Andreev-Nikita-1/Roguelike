package objects.stuff;

import hero.items.Item;
import map.MapOfObjects;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.Map;

public abstract class ItemHolder extends Stuff {
    Item item;
    VisualPixel pixel;

    public ItemHolder(MapOfObjects map, Coord location, Item item) {
        super(map, location);
        this.item = item;
        pixel = new VisualPixel(new PixelData(
                true, 8, item.getColor(), 1, item.getSymbol()));
    }

    @Override
    public void update() {

    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        pixelMap.put(location, pixel);
        return pixelMap;
    }
}
