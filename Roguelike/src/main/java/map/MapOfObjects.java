package map;

import objects.*;
import objects.creatures.HeroObject;
import util.Coord;
import util.Pausable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class MapOfObjects implements Pausable {
    public int xSize;
    public int ySize;
    private MapObject[][] objectsMap;
    private MapAreaLock[][] lockMap;
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
        lockMap = new MapAreaLock[xSize / lockSize + 1][ySize / lockSize + 1];
        for (int i = 0; i < lockMap.length; i++) {
            for (int j = 0; j < lockMap[0].length; j++) {
                lockMap[i][j] = new MapAreaLock();
            }
        }
    }

    public MapAreaLock getCoordLock(Coord coord) {
        return lockMap[coord.x / lockSize][coord.y / lockSize];
    }

    public void subscribeOnCoord(DependingObject object, Coord coord) {
        getCoordLock(coord).subscribe(object);
    }

    public void unsubscribeFromCoord(DependingObject object, Coord coord) {
        getCoordLock(coord).unsubscribe(object);
    }

    public void subscribeOnCoords(DependingObject object, Coord coord, int radius) {
        for (int i = -radius; i <= radius + lockSize; i += lockSize) {
            for (int j = -radius; j <= radius + lockSize; j += lockSize) {
                if (inside(coord.shifted(new Coord(i, j)))) {
                    subscribeOnCoord(object, coord.shifted(new Coord(i, j)));
                }
            }
        }
    }

    public void unsubscribeFromCoords(DependingObject object, Coord coord, int radius) {
        for (int i = -radius; i <= radius + lockSize; i += lockSize) {
            for (int j = -radius; j <= radius + lockSize; j += lockSize) {
                if (inside(coord.shifted(new Coord(i, j)))) {
                    subscribeOnCoord(object, coord.shifted(new Coord(i, j)));
                }
            }
        }
    }

    public Coord getHeroLocation() {
        return heroObject.getLocation();
    }

    public boolean setObject(MapObject object, Coord c) {
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

    public class MapAreaLock extends ReentrantLock {
        private Set<DependingObject> subscribers = new CopyOnWriteArraySet<>();

        public void subscribe(DependingObject object) {
            subscribers.add(object);
        }

        public void unsubscribe(DependingObject object) {
            subscribers.remove(object);
        }

        @Override
        public void unlock() {
            super.unlock();
            for (DependingObject subscriber : subscribers) {
                subscriber.update();
            }
        }
    }
}
