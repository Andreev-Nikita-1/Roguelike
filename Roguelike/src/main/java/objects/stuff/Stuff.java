package objects.stuff;

import map.MapOfObjects;
import objects.DependingObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import util.Coord;

public abstract class Stuff extends MapObject implements DependingObject, DynamicVisualObject {
    protected Coord location;

    public Stuff(MapOfObjects map, Coord location) {
        super(map);
        this.location = location;
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
}
