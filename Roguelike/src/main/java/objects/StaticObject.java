package objects;

import map.MapOfObjects;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Class for static objects, such as walls and background.
 * Method getPixels will be called one time, for fit MapRenderer.
 */
public abstract class StaticObject extends MapObject implements StaticVisualObject {
    protected Coord location;
    protected Map<Coord, VisualPixel> pixels;
    protected Set<Coord> coords = new HashSet<>();

    /**
     * @param location - location
     * @param array - array of VisualPixel, which will be given to MapRenderer
     * @param substantial - if true, object will be placed on map as a firm objects (this is for walls)
     */
    public StaticObject(Coord location, VisualPixel[][] array, boolean substantial) {
        super();
        this.location = location;
        pixels = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != null) {
                    pixels.put(new Coord(i, j).shift(location), array[i][j]);
                }
            }
        }
        if (substantial) {
            coords = pixels.keySet();
        }
    }

    /**
     * The same as previous, but Map<Coord, VisualPixel> instead of array
     */
    public StaticObject(Coord location, Map<Coord, VisualPixel> pixelMap, boolean substantial) {
        super();
        this.location = location;
        pixels = pixelMap;
        if (substantial) {
            coords = pixels.keySet();
        }
    }


    /**
     * Places on map if substantial
     */
    @Override
    public StaticObject attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        for (Coord c : coords) {
            map.setObject(this, c);
        }
        return this;
    }

    /**
     * Deletes from map
     */
    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        for (Coord c : coords) {
            map.unsetObject(this, c);
        }
    }

    /**
     * Returns pixels for MapRenderer
     */
    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        return pixels;
    }
}
