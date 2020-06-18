package objects.items;

import map.MapOfObjects;
import objects.DependingObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import util.Coord;

public abstract class Item extends MapObject implements DependingObject, DynamicVisualObject {
    protected Coord location;

    public Item(MapOfObjects map, Coord location) {
        super(map);
        this.location = location;
    }
}
