package map.roomSystem;

import map.MapOfObjects;
import objects.MapObject;
import util.Coord;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

/**
 * Class, containing information about rooms and passages between them
 */
public class RoomSystem extends MapObject {

    List<Room> rooms = new ArrayList<>();
    List<Passage> passages = new ArrayList<>();

    public RoomSystem() {
        super();
    }

    /**
     * Adds room
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Adds passage
     */
    public void addPassage(Passage passage) {
        passages.add(passage);
    }

    /**
     * Return room, which interior contains given coordinate
     */
    public Room findOutRoom(Coord c) {
        for (Room room : rooms) {
            if (c.between(room.location, room.rightDown)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Return passage, which interior contains given coordinate
     */
    public Passage findOutPassage(Coord c) {
        for (Passage passage : passages) {
            if (c.between(passage.location, passage.rightDown())) {
                return passage;
            }
        }
        return null;
    }

    /**
     * When RoomSystem attaches to map, all its rooms, passages, and their walls and backgrounds attache to map
     */
    @Override
    public RoomSystem attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        map.roomSystem = this;
        for (Room room : rooms) {
            room.attachToMap(map);
        }
        for (Passage passage : passages) {
            passage.attachToMap(map);
        }
        return this;
    }

    /**
     * Analogically to the previous
     */
    @Override
    public void deleteFromMap() {
        for (Room room : rooms) {
            room.deleteFromMap();
        }
        for (Passage passage : passages) {
            passage.deleteFromMap();
        }
    }


    /**
     * Takes snapshot of all room system, taking snapshots of rooms and passages recursively
     */
    public JSONObject getSnapshot() {
        JSONObject jsonObject = new JSONObject();
        JSONArray roomsArray = new JSONArray();
        JSONArray passagesArray = new JSONArray();
        for (Room room : rooms) {
            roomsArray.put(room.getSnapshot());
        }
        for (Passage passage : passages) {
            passagesArray.put(passage.getSnapshot());
        }
        jsonObject.put("rooms", roomsArray);
        jsonObject.put("passages", passagesArray);
        return jsonObject;
    }

    /**
     * Analogically to the previous, but restoring
     */
    public static RoomSystem restoreSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        RoomSystem roomSystem = new RoomSystem();
        JSONArray roomsArray = jsonObject.getJSONArray("rooms");
        JSONArray passagesArray = jsonObject.getJSONArray("passages");
        for (int i = 0; i < roomsArray.length(); i++) {
            JSONObject roomSnapshot = roomsArray.getJSONObject(i);
            roomSystem.addRoom(Room.restoreFromSnapshot(roomSnapshot));
        }
        for (int i = 0; i < passagesArray.length(); i++) {
            JSONObject passageSnapshot = passagesArray.getJSONObject(i);
            roomSystem.addPassage(Passage.restoreSnapshot(passageSnapshot, roomSystem));
        }
        return roomSystem;
    }

}
