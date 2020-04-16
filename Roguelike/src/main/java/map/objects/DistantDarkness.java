package map.objects;

import map.MapOfObjects;
import util.Coord;
import map.LogicPixel;
import map.shapes.Shape;

import java.util.HashMap;
import java.util.Map;
//
//import static map.MapOfObjects.heroObject;
//
public class DistantDarkness extends MapObject implements DependingObject {
    public DistantDarkness(MapOfObjects map, Coord coord, Shape shape) {
        super(map, coord, shape);
    }

    @Override
    public void update() {

    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        return null;
    }
//
//    private int radius;
//
//    public DistantDarkness(int radius) {
//        init(heroObject.location, Shape.EMPTY_SHAPE);
//        this.radius = radius;
//    }
//
//    @Override
//    public void update() {
//        location = heroObject.getLocation();
//    }
//
//    @Override
//    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
//        Map<Coord, LogicPixel> map = new HashMap<>();
//        for (int i = leftUp.x; i < rightDown.x; i++) {
//            for (int j = leftUp.y; j < rightDown.y; j++) {
//                int d = (int) (Math.pow((location.x - i), 2) + Math.pow(2 * (location.y - j), 2));
//
//                if (d > 20 * radius) {
//                    map.put(new Coord(i, j), LogicPixel.DARKNESS_FULL);
//                } else if (d > 10 * radius) {
//                    map.put(new Coord(i, j), LogicPixel.DARKNESS_3);
//                } else if (d > 5 * radius) {
//                    map.put(new Coord(i, j), LogicPixel.DARKNESS_2);
//                } else if (d > radius) {
//                    map.put(new Coord(i, j), LogicPixel.DARKNESS_1);
//                }
//            }
//        }
//        return map;
//    }
}
