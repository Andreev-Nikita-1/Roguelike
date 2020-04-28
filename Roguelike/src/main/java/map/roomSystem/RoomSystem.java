package map.roomSystem;

import map.MapOfObjects;
import objects.MapObject;
import util.Coord;

import java.util.ArrayList;
import java.util.List;

public class RoomSystem extends MapObject {

    private List<Room> rooms = new ArrayList<>();
    private List<Passage> passages = new ArrayList<>();

    public RoomSystem(MapOfObjects map) {
        super(map);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addPassage(Passage passage) {
        passages.add(passage);
    }

    public Room findOutRoom(Coord c) {
        for (Room room : rooms) {
            if (c.between(room.location, room.rightDown)) {
                return room;
            }
        }
        return null;
    }

    public Passage findOutPassage(Coord c) {
        for (Passage passage : passages) {
            if (c.between(passage.location, passage.rightDown())) {
                return passage;
            }
        }
        return null;
    }

    @Override
    public RoomSystem attachToMap() {
        map.roomSystem = this;
        for (Room room : rooms) {
            room.attachToMap();
        }
        for (Passage passage : passages) {
            passage.attachToMap();
        }
        return this;
    }

    @Override
    public void deleteFromMap() {
        for (Room room : rooms) {
            room.deleteFromMap();
        }
        for (Passage passage : passages) {
            passage.deleteFromMap();
        }
    }

}
