package map;

import basicComponents.Game;
import map.roomSystem.Passage;
import map.roomSystem.Room;
import map.roomSystem.RoomSystem;
import objects.*;
import objects.creatures.HeroObject;
import org.json.JSONArray;
import org.json.JSONObject;
import util.AccessNeighbourhood;
import util.Coord;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class manages all objects on map
 */
public class MapOfObjects {

    /**
     * Sets game for this map
     */
    public void setGame(Game game) {
        this.game = game;
    }

    public Game game;
    public int xSize;
    public int ySize;
    private MapObject[][] objectsMap;
    private MapAreaLock[][] lockMap;
    private int lockSize = 10;
    public List<StaticVisualObject> staticObjects = new CopyOnWriteArrayList<>();
    public List<DynamicVisualObject> dynamicObjects = new CopyOnWriteArrayList<>();
    public List<SnapshotableFromMap> snapshotableObjects = new CopyOnWriteArrayList<>();
    public HeroObject heroObject;
    public AccessNeighbourhood heroAccessNeighbourhood;
    public RoomSystem roomSystem;
    public Lighting lighting;

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

    /**
     * When someone wants to modify map state (e.g. mob moving), it locks map area lock
     */
    public MapAreaLock getCoordLock(Coord coord) {
        return lockMap[coord.x / lockSize][coord.y / lockSize];
    }


    /**
     * If DependingObject object subscribes on coordinate, every time someone unlocks map area lock, associated with this
     * coordinate, method "update" calls on the object
     */
    public void subscribeOnCoord(DependingObject object, Coord coord) {
        getCoordLock(coord).subscribe(object);
    }


    /**
     * Unsubscribing in terms of previous method
     */
    public void unsubscribeFromCoord(DependingObject object, Coord coord) {
        getCoordLock(coord).unsubscribe(object);
    }

    /**
     * Subscribing on area of curtain radius around coordinate
     */
    public void subscribeOnCoords(DependingObject object, Coord coord, int radius) {
        for (int i = -radius; i <= radius + lockSize; i += lockSize) {
            for (int j = -radius; j <= radius + lockSize; j += lockSize) {
                if (inside(coord.shifted(new Coord(i, j)))) {
                    subscribeOnCoord(object, coord.shifted(new Coord(i, j)));
                }
            }
        }
    }

    /**
     * Unsubscribing in terms of previous method
     */
    public void unsubscribeFromCoords(DependingObject object, Coord coord, int radius) {
        for (int i = -radius; i <= radius + lockSize; i += lockSize) {
            for (int j = -radius; j <= radius + lockSize; j += lockSize) {
                if (inside(coord.shifted(new Coord(i, j)))) {
                    unsubscribeFromCoord(object, coord.shifted(new Coord(i, j)));
                }
            }
        }
    }

    /**
     * Returns room, which interior contains coordinate, or closest room, if coordinate is located in passage between rooms
     */
    public Room closestRoom(Coord coord) {
        Room room = roomSystem.findOutRoom(coord);
        if (room != null) {
            return room;
        } else {
            Passage passage = roomSystem.findOutPassage(coord);
            AccessNeighbourhood neighbourhood = new AccessNeighbourhood(this, coord, 5);
            Coord first = passage.entryLocation(passage.room1);
            if (neighbourhood.accessible(first)) {
                neighbourhood.delete();
                return passage.room1;
            } else {
                neighbourhood.delete();
                return passage.room2;
            }
        }
    }


    /**
     * Returns hero location
     */
    public Coord getHeroLocation() {
        return heroObject.getLocation();
    }


    /**
     * Places firm object on the given coordinate map
     */
    public boolean setObject(MapObject object, Coord c) {
        if (objectsMap[c.x][c.y] != null) {
            return false;
        }
        objectsMap[c.x][c.y] = object;
        return true;
    }

    /**
     * Deletes firm object from the map on the given coordinate
     */
    public boolean unsetObject(MapObject object, Coord c) {
        if (objectsMap[c.x][c.y] != object) {
            return false;
        }
        objectsMap[c.x][c.y] = null;
        return true;
    }

    /**
     * Returns object, located on given coordinate
     */
    public MapObject getObject(Coord c) {
        return objectsMap[c.x][c.y];
    }

    /**
     * Returns true if given coordinate is taken by firm object
     */
    public boolean isTaken(Coord c) {
        return getObject(c) != null;
    }

    /**
     * Returns true if given coordinate is inside map frame
     */
    public boolean inside(Coord coord) {
        return coord.x < xSize && coord.x >= 0 && coord.y < ySize && coord.y >= 0;
    }

    /**
     * Returns true if object can be placed on given coordinate
     */
    public boolean accesible(Coord coord) {
        return inside(coord) && !isTaken(coord);
    }


    /**
     * This class represents lock for map area, and responsible for notifying objects, which subscribed on this area,
     * when unlocking
     */
    public class MapAreaLock extends ReentrantLock {
        private Set<DependingObject> subscribers = new CopyOnWriteArraySet<>();

        void subscribe(DependingObject object) {
            subscribers.add(object);
        }

        void unsubscribe(DependingObject object) {
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

    /**
     * Returns snapshot of the map
     */
    public JSONObject getSnapshot() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("xSize", xSize);
        jsonObject.put("ySize", ySize);
        jsonObject.put("xHero", heroObject.getLocation().x);
        jsonObject.put("yHero", heroObject.getLocation().y);
        jsonObject.put("roomSystem", roomSystem.getSnapshot());
        JSONArray snapshots = new JSONArray();
        for (SnapshotableFromMap snapshotable : snapshotableObjects) {
            snapshots.put(snapshotable.getSnapshot());
        }
        jsonObject.put("snapshots", snapshots);
        return jsonObject;
    }


    /**
     * Restores map from the snapshot. You must give Game object, for all dynamic objects be active when game starts
     */
    public static MapOfObjects restoreFromSnapshot(JSONObject jsonObject, Game game) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MapOfObjects map = new MapOfObjects(
                jsonObject.getInt("xSize"),
                jsonObject.getInt("ySize"));
        map.setGame(game);
        map.heroObject = new HeroObject(
                new Coord(jsonObject.getInt("xHero"),
                        jsonObject.getInt("yHero")))
                .attachToMap(map);
        map.roomSystem = RoomSystem.restoreSnapshot(
                jsonObject.getJSONObject("roomSystem")
        ).attachToMap(map);
        JSONArray snapshots = jsonObject.getJSONArray("snapshots");
        for (int i = 0; i < snapshots.length(); i++) {
            ((MapObject) SnapshotableFromMap.restoreSnapshot(snapshots.getJSONObject(i))).attachToMap(map);
        }
        map.lighting = (Lighting) new Lighting(7).attachToMap(map);
        return map;
    }
}
