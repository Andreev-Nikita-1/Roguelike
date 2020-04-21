package objects;

import map.MapOfObjects;
import util.Pausable;

public abstract class PausableObject extends MapObject implements Pausable {
    public PausableObject(MapOfObjects map) {
        super(map);
    }

    @Override
    public PausableObject attachToMap() {
        map.pausableObjects.add(this);
        return this;
    }

    @Override
    public void deleteFromMap() {
        map.pausableObjects.remove(this);
    }
}
