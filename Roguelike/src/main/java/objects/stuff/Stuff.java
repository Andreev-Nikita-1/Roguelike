package objects.stuff;

import map.MapOfObjects;
import objects.DependingObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import objects.SnapshotableFromMap;
import org.json.JSONObject;
import util.Coord;

/**
 * Class, representing stuff, that can be taken by hero
 */
public abstract class Stuff extends MapObject implements DependingObject, DynamicVisualObject, SnapshotableFromMap {
    protected Coord location;

    Stuff(Coord location) {
        super();
        this.location = location;
    }

    @Override
    public MapObject attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        map.subscribeOnCoord(this, location);
        return this;
    }

    @Override
    public void deleteFromMap() {
        map.unsubscribeFromCoord(this, location);
        super.deleteFromMap();
    }

    @Override
    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("x", location.x)
                .put("y", location.y)
                .put("class", this.getClass().getName());
    }
}
