package map.roomSystem;

import map.MapOfObjects;
import objects.MapObject;

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

    @Override
    public RoomSystem attachToMap() {
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
