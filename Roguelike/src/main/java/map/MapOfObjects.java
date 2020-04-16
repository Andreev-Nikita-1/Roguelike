package map;

import map.objects.DynamicObject;
import map.objects.HeroObject;
import map.objects.MapObject;
import util.Coord;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MapOfObjects {
    public int xSize;
    public int ySize;
    public List<MapObject> objectsList = new CopyOnWriteArrayList<>();
    private MapObject[][] objectsMap;
    public HeroObject heroObject;

    public MapOfObjects(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        objectsMap = new MapObject[xSize][ySize];
    }

    public Coord getHeroLocation() {
        return heroObject.getLocation();
    }

    public void setObject(MapObject object, Coord c) {
        assert (objectsMap[c.x][c.y] == null);
        objectsMap[c.x][c.y] = object;
    }

    public void unsetObject(MapObject object, Coord c) {
        assert (objectsMap[c.x][c.y] == object);
        objectsMap[c.x][c.y] = null;
    }

    public MapObject getObject(Coord c) {
        return objectsMap[c.x][c.y];
    }

    public boolean isTaken(Coord c) {
        return getObject(c) != null;
    }

    public boolean inside(Coord coord) {
        return coord.x < xSize && coord.x >= 0 && coord.y < ySize && coord.y >= 0;
    }

    public void start() {
        for (MapObject object : objectsList) {
            if (object instanceof DynamicObject) {
                ((DynamicObject) object).start();
            }
        }
    }

    public void pause() {
        for (MapObject object : objectsList) {
            if (object instanceof DynamicObject) {
                ((DynamicObject) object).pause();
            }
        }
    }

    public void unpause() {
        for (MapObject object : objectsList) {
            if (object instanceof DynamicObject) {
                ((DynamicObject) object).unpause();
            }
        }
    }
}
