package objects.neighbourfoods;

import com.googlecode.lanterna.TextColor;
import map.MapOfObjects;
import objects.DynamicVisualObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class testAcess extends AccessNeighbourhood implements DynamicVisualObject {

    public testAcess(MapOfObjects map, Coord center, int radius, Function<Coord, Double> norm) {
        super(map, center, radius, norm);
    }

    public testAcess(MapOfObjects map, Coord center, int radius) {
        super(map, center, radius);
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> map = new HashMap<>();
        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[0].length; j++) {
                if (mask[i][j] != -1) {
                    map.put(center.shifted(new Coord(i - radius, j - radius)),
                            new VisualPixel(
                                    new PixelData(
                                            true,
                                            100,
                                            TextColor.ANSI.WHITE,
                                            0.9,
                                            String.valueOf(mask[i][j]).charAt(0)
                                    )));
                }
            }
        }
        return map;
    }
}
