package map.objects;

import util.Coord;
import map.LogicPixel;

import java.util.HashMap;
import java.util.Map;

import static map.shapes.Shape.EMPTY_SHAPE;


public class LightSphere extends MapObject implements DependingObject {
    private int radius;
    private MapObject owner;

    public LightSphere(MapObject owner, int radius) {
        super(owner.map, owner.location, EMPTY_SHAPE);
        this.owner = owner;
        this.radius = radius;
    }

    @Override
    public void update() {
//        location = owner.getLocation();
    }

    @Override
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> map = new HashMap<>();
        for (int i = leftUp.x; i < rightDown.x; i++) {
            for (int j = leftUp.y; j < rightDown.y; j++) {
                int d = (int) (Math.pow((location.x - i), 2) + Math.pow(2 * (location.y - j), 2));

                if (d < radius) {
                    map.put(new Coord(i, j), LogicPixel.LIGHT_1);
                } else if (d < 5 * radius) {
                    map.put(new Coord(i, j), LogicPixel.LIGHT_2);
                } else if (d < 10 * radius) {
                    map.put(new Coord(i, j), LogicPixel.LIGHT_3);
                }
            }
        }
        return map;
    }

    @Override
    public LightSphere attachToMap() {
        return (LightSphere) super.attachToMap();
    }
}
