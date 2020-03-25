package map;

import map.Coord;
import map.objects.Object;

import java.util.List;
import java.util.concurrent.locks.Lock;

public abstract class MapOfObjects {
    public static int xSize;
    public static int ySize;
    private static Object[][] mapObjects;
    public static final String mapLock = "MAP LOCK";

    public static void initialize(int xSize_, int ySize_) {
        xSize = xSize_;
        ySize = ySize_;
        mapObjects = new Object[xSize][ySize];
    }

    public static void placeObject(Object object) {
        List<Coord> objectCoords = object.getCoords();
        for (Coord c : objectCoords) {
            if (mapObjects[c.x][c.y] != null) {

            }
        }
        for (Coord c : objectCoords) {
            mapObjects[c.x][c.y] = object;
        }
    }

    public static void detachObject(Object object) {
        List<Coord> objectCoords = object.getCoords();
        for (Coord c : objectCoords) {
            if (mapObjects[c.x][c.y] != object) {

            }
        }
        for (Coord c : objectCoords) {
            mapObjects[c.x][c.y] = null;
        }
    }

    public static Object getObject(Coord c) {
        return mapObjects[c.x][c.y];
    }

    public static boolean isTaken(Coord c) {
        return getObject(c) != null;
    }

    public static boolean inside(Coord coord) {
        return coord.x < xSize && coord.x >= 0 && coord.y < ySize && coord.y >= 0;
    }
}
