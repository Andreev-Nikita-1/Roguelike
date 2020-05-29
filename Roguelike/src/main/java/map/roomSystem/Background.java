package map.roomSystem;

import map.MapOfObjects;
import objects.StaticObject;
import util.Coord;
import renderer.VisualPixel;

import java.util.Map;

public class Background extends StaticObject {

    public Background(Coord location, VisualPixel[][] array) {
        super(location, array, false);
    }

    public Background(Coord location, Map<Coord, VisualPixel> pixelMap) {
        super(location, pixelMap, false);
    }


    @Override
    public Background attachToMap(MapOfObjects map) {
        return (Background) super.attachToMap(map);
    }
}
