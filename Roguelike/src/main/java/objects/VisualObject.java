package objects;

import renderer.VisualPixel;
import util.Coord;

import java.util.Map;

public interface VisualObject {
    Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown);
}
