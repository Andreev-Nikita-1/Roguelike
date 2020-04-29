package objects.items;

import basicComponents.GameplayLogic;
import inventory.InventoryItem;
import map.MapOfObjects;
import objects.MapObject;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.Map;

public class InventoryItemOnMap extends Item {
    protected InventoryItem inventoryItem;

    public InventoryItemOnMap(MapOfObjects map, Coord location, InventoryItem inventoryItem) {
        super(map, location);
        this.inventoryItem = inventoryItem;
    }

    @Override
    public synchronized void update() {
        if (location.equals(map.getHeroLocation())) {
            GameplayLogic.inventory.addItem(inventoryItem);
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
        pixelMap.put(location, VisualPixel.STUFF);
        return pixelMap;
    }
}
