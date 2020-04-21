package objects;

import util.Coord;
import renderer.VisualPixel;

import java.util.HashMap;
import java.util.Map;

//
//public class LightSphere extends MapObject implements VisualObject, DependingObject {
//    private int radius;
//    private  owner;
//
//    public LightSphere(VisualObject1 owner, int radius) {
//        super(owner.map, owner.location, EMPTY_SHAPE);
//        this.owner = owner;
//        this.radius = radius;
//    }
//
//    @Override
//    public void update() {
////        location = owner.getLocation();
//    }
//
//    @Override
//    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
//        Map<Coord, VisualPixel> map = new HashMap<>();
//        for (int i = leftUp.x; i < rightDown.x; i++) {
//            for (int j = leftUp.y; j < rightDown.y; j++) {
//                int d = (int) (Math.pow((location.x - i), 2) + Math.pow(4 / 3 * (double) (location.y - j), 2));
//
//                if (d < radius) {
//                    map.put(new Coord(i, j), VisualPixel.LIGHT_1);
//                } else if (d < 5 * radius) {
//                    map.put(new Coord(i, j), VisualPixel.LIGHT_2);
//                } else if (d < 10 * radius) {
//                    map.put(new Coord(i, j), VisualPixel.LIGHT_3);
//                }
//            }
//        }
//        return map;
//    }
//
//    @Override
//    public LightSphere attachToMap() {
//        return (LightSphere) super.attachToMap();
//    }
//}
