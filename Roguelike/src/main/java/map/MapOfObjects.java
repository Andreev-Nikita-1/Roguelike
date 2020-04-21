package map;

import objects.*;
import objects.creatures.HeroObject;
import util.Coord;
import util.Pausable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapOfObjects implements Pausable {
    public int xSize;
    public int ySize;
    private MapObject[][] objectsMap;
    private Lock[][] lockMap;
    private int lockSize = 10;
    public List<StaticVisualObject> staticObjects = new CopyOnWriteArrayList<>();
    public List<DynamicVisualObject> dynamicObjects = new CopyOnWriteArrayList<>();
    public List<PausableObject> pausableObjects = new CopyOnWriteArrayList<>();
    public HeroObject heroObject;
    public ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public MapOfObjects(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        objectsMap = new MapObject[xSize][ySize];
        lockMap = new Lock[xSize / lockSize + 1][ySize / lockSize + 1];
        for (int i = 0; i < lockMap.length; i++) {
            for (int j = 0; j < lockMap[0].length; j++) {
                lockMap[i][j] = new ReentrantLock();
            }
        }
    }

    public Lock getCoordLock(Coord coord) {
        return lockMap[coord.x / lockSize][coord.y / lockSize];
    }

    public Coord getHeroLocation() {
        return heroObject.getLocation();
    }

    public boolean setObject(MapObject object, Coord c) {
        if (c.y == 20) {
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
        scheduler.shutdown();
    }
}
