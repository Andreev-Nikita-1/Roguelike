package map.shapes;

import map.LogicPixel;
import map.MapOfObjects;
import map.objects.MapObject;
import util.Coord;

import java.util.*;

public class CustomStaticShape extends Shape {

    private Map<Coord, LogicPixel> pixels;
    private Set<Coord> coords = new HashSet<>();

    public CustomStaticShape(LogicPixel[][] array, boolean substantial) {
        pixels = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != null) {
                    pixels.put(new Coord(i, j), array[i][j]);
                }
            }
        }
        if (substantial) {
            coords = pixels.keySet();
        }
    }

//    public CustomStaticShape(Map<Coord, LogicPixel> pixels) {
//        this.pixels = pixels;
//        coords = pixels.keySet();
//    }

    @Override
    public void placeObject(MapOfObjects map, MapObject object) {
        for (Coord c : coords) {
            map.setObject(object, object.getLocation().shifted(c));
        }
    }

    @Override
    public void detachObject(MapOfObjects map, MapObject object) {
        for (Coord c : coords) {
            map.unsetObject(object, object.getLocation().shifted(c));
        }
    }

    @Override
    public boolean canPlace(MapOfObjects map, Coord location) {
        for (Coord c : coords) {
            if (!map.inside(location.shifted(c)) || map.isTaken(location.shifted(c))) {
                return false;
            }
        }
        return true;
    }

    public Map<Coord, LogicPixel> getPixels() {
        return pixels;
    }
}
