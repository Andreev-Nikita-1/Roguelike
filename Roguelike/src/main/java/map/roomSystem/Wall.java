package map.roomSystem;

import objects.StaticObject;
import renderer.VisualPixel;
import map.MapOfObjects;
import util.Coord;

/**
 * Class for wall
 */
public class Wall extends StaticObject {
    public Wall(Coord coord, VisualPixel[][] array) {
        super(coord, array, true);
    }

    @Override
    public Wall attachToMap(MapOfObjects map) {
        return (Wall) super.attachToMap(map);
    }
}
