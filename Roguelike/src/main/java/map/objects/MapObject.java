package map.objects;

import util.Coord;
import map.MapOfObjects;
import map.LogicPixel;
import map.shapes.Shape;

import java.util.Map;

public abstract class MapObject {
    protected MapOfObjects map;
    protected Coord location;
    protected Shape shape;

    public Coord getLocation() {
        return location;
    }

    public Shape getShape() {
        return shape;
    }

    public MapObject(MapOfObjects map, Coord coord, Shape shape) {
        this.map = map;
        this.location = coord;
        this.shape = shape;
    }

    public MapObject attachToMap() {
        shape.placeObject(map, this);
        map.objectsList.add(this);
        return this;
    }

    public void deleteFromMap() {
        shape.detachObject(map, this);
        map.objectsList.remove(this);
    }

    public void shift(Coord shift) {
        shape.detachObject(map, this);
        location.shift(shift);
        shape.placeObject(map, this);
    }


    public abstract Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown);
}
