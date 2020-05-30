package objects;

import map.MapOfObjects;
import util.Pausable;

/**
 * Based class for all objects on map
 */
public abstract class MapObject {
    public MapOfObjects map;

    /**
     * Method called to attach object to map. Can be overriden for all successors.
     */
    public MapObject attachToMap(MapOfObjects map) {
        this.map = map;
        if (this instanceof Pausable && map.game != null) {
            ((Pausable) this).includeToGame(map.game);
        }
        if (this instanceof DynamicVisualObject) {
            map.dynamicObjects.add((DynamicVisualObject) this);
        }
        if (this instanceof StaticVisualObject) {
            map.staticObjects.add((StaticVisualObject) this);
        }
        if (this instanceof SnapshotableFromMap) {
            map.snapshotableObjects.add((SnapshotableFromMap) this);
        }
        return this;
    }

    /**
     * Deletes object from map.
     */
    public void deleteFromMap() {
        if (this instanceof Pausable && map.game != null) {
            ((Pausable) this).deleteFromGame(map.game);
        }
        if (this instanceof DynamicVisualObject) {
            map.dynamicObjects.remove(this);
        }
        if (this instanceof StaticVisualObject) {
            map.staticObjects.remove(this);
        }
        if (this instanceof SnapshotableFromMap) {
            map.snapshotableObjects.remove((SnapshotableFromMap) this);
        }
    }
}
