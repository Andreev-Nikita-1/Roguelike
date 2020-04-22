package objects.neighbourfoods;

import map.MapOfObjects;
import objects.DependingObject;
import objects.DynamicVisualObject;
import objects.MapObject;
import util.Coord;
import renderer.VisualPixel;

import java.util.HashMap;
import java.util.Map;


public class DistantDarkness extends MapObject implements DynamicVisualObject {


    private Coord center;
    private int radius;
    private ViewNeighbourhood neighbourhood;

    public DistantDarkness(MapOfObjects map, Coord center, int radius) {
        super(map);
        this.center = center;
        this.radius = radius;
        neighbourhood = new ViewNeighbourhood(map, center, radius);
    }

    @Override
    public DistantDarkness attachToMap() {
        super.attachToMap();
        neighbourhood.update();
        return this;
    }


    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> map = new HashMap<>();
        for (int i = leftUp.x; i < rightDown.x; i++) {
            for (int j = leftUp.y; j < rightDown.y; j++) {
                Coord current = new Coord(i, j);
                int dist = neighbourhood.number(current, 1, Coord::lInftyNorm);
                if (dist == -1) {
                    map.put(new Coord(i, j), VisualPixel.DARKNESS_FULL);
                    continue;
                }
                if (dist < 8) {
                    continue;
                } else if (dist < 10) {
                    map.put(new Coord(i, j), VisualPixel.DARKNESS_1);
//                } else if (dist < 12) {
//                    map.put(new Coord(i, j), VisualPixel.DARKNESS_2);
                } else if (dist < 14) {
                    map.put(new Coord(i, j), VisualPixel.DARKNESS_3);
                } else {
                    map.put(new Coord(i, j), VisualPixel.DARKNESS_FULL);
                }
            }
        }
        return map;
    }
}
