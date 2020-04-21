package objects;

import util.Coord;
import map.MapOfObjects;
import renderer.VisualPixel;

import java.util.Map;

public abstract class MapObject {
    public final MapOfObjects map;

    public MapObject(MapOfObjects map) {
        this.map = map;
    }

    public abstract MapObject attachToMap();

    public abstract void deleteFromMap();

}
