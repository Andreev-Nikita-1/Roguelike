package map.objects;

import map.Coord;
import map.Damage;
import basicComponents.MapOfFirmObjects;
import map.LogicPixel;
import map.shapes.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Object {
    protected Coord location;
    protected Shape shape;

    public void init(Coord coord, Shape shape) {
        location = new Coord(coord);
        this.shape = shape;
    }

    public List<Coord> getCoords() {
        List<Coord> shifts = shape.getShifts();
        List<Coord> coords = new ArrayList<>();
        for (Coord shift : shifts) {
            coords.add(new Coord(location.x + shift.x, location.y + shift.y));
        }
        return coords;
    }

    public void shift(Coord shift) {
        MapOfFirmObjects.detachObject(this);
        location.shift(shift);
        MapOfFirmObjects.placeObject(this);
    }

    public abstract void act();

    public abstract void takeDamage(Damage damage);

    public abstract Map<Coord, LogicPixel> getPixels();
}
