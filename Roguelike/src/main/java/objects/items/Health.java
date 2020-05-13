package objects.items;

import map.MapOfObjects;
import objects.MapObject;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.Map;

public class Health extends ItemOnMap {

    private int value;

    public Health(MapOfObjects map, Coord location, int value) {
        super(map, location);
        this.value = value;
    }

    @Override
    public synchronized void update() {
        if (location.equals(map.getHeroLocation())) {
            map.heroObject.hero.health.addAndGet(value);
            deleteFromMap();
        }
    }

    @Override
    public MapObject attachToMap() {
        super.attachToMap();
        map.subscribeOnCoord(this, location);
        return this;
    }

    @Override
    public void deleteFromMap() {
        map.unsubscribeFromCoord(this, location);
        super.deleteFromMap();
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        pixelMap.put(location, VisualPixel.HEART);
        return pixelMap;
    }
}
