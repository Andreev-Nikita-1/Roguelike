package map.roomSystem;

import map.MapOfObjects;
import objects.StaticObject;
import util.Coord;
import renderer.VisualPixel;

public class Background extends StaticObject {

    public Background(MapOfObjects map, Coord location, VisualPixel[][] array) {
        super(map, location, array, false);
    }


    @Override
    public Background attachToMap() {
        return (Background) super.attachToMap();
    }
}
