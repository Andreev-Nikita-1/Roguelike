package objects;

import map.MapOfObjects;
import util.Pausable;

public abstract class PausableObject extends MapObject implements Pausable {
    public PausableObject(MapOfObjects map) {
        super(map);
    }
}
