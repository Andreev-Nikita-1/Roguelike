package map.objects;

import basicComponents.GameplayLogic;
import map.Coord;
import map.MapOfObjects;
import map.LogicPixel;
import map.shapes.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static map.MapOfObjects.mapLock;

public abstract class Object {
    protected Coord location;
    protected Shape shape;

    public void init(Coord coord, Shape shape) {
        location = new Coord(coord);
        this.shape = shape;
        synchronized (mapLock) {
            MapOfObjects.placeObject(this);
        }
        GameplayLogic.objects.add(this);
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
        synchronized (mapLock) {
            MapOfObjects.detachObject(this);
            location.shift(shift);
            MapOfObjects.placeObject(this);
        }
    }

    public void delete() {
        synchronized (mapLock) {
            MapOfObjects.detachObject(this);
            GameplayLogic.objects.remove(this);
        }
    }

    public abstract Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown);
}
