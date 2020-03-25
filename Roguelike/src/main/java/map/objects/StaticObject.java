package map.objects;

import map.Coord;
import map.Damage;
import map.LogicPixel;

import java.util.Map;

public abstract class StaticObject extends Object {
    protected Map<Coord, LogicPixel> pixelSet;

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        return pixelSet;
    }
}
