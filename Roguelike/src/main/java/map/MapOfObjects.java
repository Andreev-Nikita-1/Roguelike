package map;

import objects.*;
import util.Coord;
import util.Pausable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MapOfObjects implements Pausable {
    public int xSize;
    public int ySize;
    private MapObject[][] objectsMap;
    public List<VisualObject> staticObjects = new CopyOnWriteArrayList<>();
    public List<VisualObject> dynamicObjects = new CopyOnWriteArrayList<>();
    public List<PausableObject> pausableObjects = new CopyOnWriteArrayList<>();
    public HeroObject heroObject;
    public ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public MapOfObjects(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        objectsMap = new MapObject[xSize][ySize];
    }

    public Coord getHeroLocation() {
        return heroObject.getLocation();
    }

    public boolean setObject(MapObject object, Coord c) {
        if(c.y==20){
            return false;
        }
        if (objectsMap[c.x][c.y] != null) {
            return false;
        }
        objectsMap[c.x][c.y] = object;
        return true;
    }

    public boolean unsetObject(MapObject object, Coord c) {
        if (objectsMap[c.x][c.y] != object) {
            return false;
        }
        objectsMap[c.x][c.y] = null;
        return true;
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

    @Override
    public MapOfObjects start() {
        for (PausableObject object : pausableObjects) {
            object.start();
        }
        return this;
    }

    @Override
    public void pause() {
        for (PausableObject object : pausableObjects) {
            object.pause();
        }
    }

    @Override
    public void unpause() {
        for (PausableObject object : pausableObjects) {
            object.unpause();
        }
    }

    @Override
    public void kill() throws InterruptedException {
        for (PausableObject object : pausableObjects) {
            object.kill();
        }
    }
}
