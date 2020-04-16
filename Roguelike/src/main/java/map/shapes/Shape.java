package map.shapes;

import map.MapOfObjects;
import map.objects.MapObject;
import util.Coord;

public abstract class Shape {

    public static Shape SINGLE_PIXEL_SHAPE = new Shape() {
        @Override
        public void placeObject(MapOfObjects map, MapObject object) {
            map.setObject(object, object.getLocation());
        }

        @Override
        public void detachObject(MapOfObjects map, MapObject object) {
            map.unsetObject(object, object.getLocation());
        }

        @Override
        public boolean canPlace(MapOfObjects map, Coord location) {
            return map.inside(location) && !map.isTaken(location);
        }
    };

    public static Shape EMPTY_SHAPE = new Shape() {
        @Override
        public void placeObject(MapOfObjects map, MapObject object) {
        }

        @Override
        public void detachObject(MapOfObjects map, MapObject object) {
        }

        @Override
        public boolean canPlace(MapOfObjects map, Coord location) {
            return true;
        }
    };

    public abstract void placeObject(MapOfObjects map, MapObject object);

    public abstract void detachObject(MapOfObjects map, MapObject object);

    public abstract boolean canPlace(MapOfObjects map, Coord location);
}
