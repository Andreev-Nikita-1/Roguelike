package objects;

import map.MapOfObjects;
import util.Pausable;

public abstract class MapObject {
    public final MapOfObjects map;

    public MapObject(MapOfObjects map) {
        this.map = map;
    }

    public MapObject attachToMap() {
        if (this instanceof Pausable) {
            ((Pausable) this).includeToGame();
        }
        if (this instanceof DynamicVisualObject) {
            map.dynamicObjects.add((DynamicVisualObject) this);
        }
        if (this instanceof StaticVisualObject) {
            map.staticObjects.add((StaticVisualObject) this);
        }
        return this;
    }

    public void deleteFromMap() {
        if (this instanceof Pausable) {
            ((Pausable) this).deleteFromGame();
        }
        if (this instanceof DynamicVisualObject) {
            map.dynamicObjects.remove(this);
        }
        if (this instanceof StaticVisualObject) {
            map.staticObjects.remove(this);
        }
    }
}
