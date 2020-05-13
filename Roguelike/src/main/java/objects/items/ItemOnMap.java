package objects.items;

import map.MapOfObjects;
import objects.DependingObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import util.Coord;

public abstract class ItemOnMap extends MapObject implements DependingObject, DynamicVisualObject {
    protected Coord location;

    public ItemOnMap(MapOfObjects map, Coord location) {
        super(map);
        this.location = location;
    }
}
