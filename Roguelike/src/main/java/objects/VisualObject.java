package objects;

import renderer.VisualPixel;
import util.Coord;

import java.util.Map;


/**
 * Has method getPixels for MapRenderer
 */
public interface VisualObject {
    Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown);
}
