package map.roomSystem;

import objects.StaticObject;
import renderer.VisualPixel;
import map.MapOfObjects;
import util.Coord;

import java.util.List;

public class Wall extends StaticObject {
    public Wall(MapOfObjects map, Coord coord, VisualPixel[][] array) {
        super(map, coord, array, true);
    }

    @Override
    public Wall attachToMap() {
        return (Wall) super.attachToMap();
    }
}
