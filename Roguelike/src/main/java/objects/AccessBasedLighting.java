package objects;

import map.MapOfObjects;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.Map;

public class AccessBasedLighting extends MapObject implements DynamicVisualObject {

    private int radius;

    public AccessBasedLighting(MapOfObjects map, int radius) {
        super(map);
        this.radius = radius;
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        for (int i = leftUp.x; i < rightDown.x; i++) {
            for (int j = leftUp.y; j < rightDown.y; j++) {
                Coord current = new Coord(i, j);
                if (!map.heroAccessNeighbourhood.accessible(current, Coord::euqlideanScaled, radius)) {
                    pixelMap.put(new Coord(i, j), VisualPixel.DARKNESS_FULL);
                    continue;
                }
                double dist = Coord.euqlideanScaled(current.relative(map.getHeroLocation()));
                if (dist < 8) {
                    continue;
                } else if (dist < 10) {
                    pixelMap.put(new Coord(i, j), VisualPixel.DARKNESS_1);
//                } else if (dist < 12) {
//                    map.put(new Coord(i, j), VisualPixel.DARKNESS_2);
                } else if (dist < 14) {
                    pixelMap.put(new Coord(i, j), VisualPixel.DARKNESS_3);
                } else {
                    pixelMap.put(new Coord(i, j), VisualPixel.DARKNESS_FULL);
                }
            }
        }
        return pixelMap;
    }
}
