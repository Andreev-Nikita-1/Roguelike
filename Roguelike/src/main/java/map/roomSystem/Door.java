package map.roomSystem;

import map.MapOfObjects;
import map.roomSystem.textures.RoomTextures;
import objects.*;
import org.json.JSONObject;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static map.roomSystem.Door.DoorState.*;

/**
 * Class for door
 */
public class Door extends Passage implements DynamicVisualObject, InteractiveObject {
    private static Color DOOR_COLOR = new Color(21, 8, 3);
    private static final VisualPixel DOOR_CLOSED = new VisualPixel(
            new PixelData(true, 10, DOOR_COLOR, 1, (char) 0x01E3));
    private static final VisualPixel DOOR_OPEN_HORIZONTAL = new VisualPixel(
            new PixelData(true, 5, DOOR_COLOR, 1, (char) 0x01E6));
    private static final VisualPixel DOOR_OPEN_VERTICAl = new VisualPixel(
            new PixelData(true, 5, DOOR_COLOR, 1, (char) 0x01E5));


    Coord doorCoord;
    DoorState state = CLOSED;
    boolean highlighted = false;
    int depth;

    /**
     * Returns true if open
     */
    @Override
    public boolean passable(int width) {
        return width <= 1 && state == OPEN;
    }

    /**
     * Door is located at a distance od "depth", counting from passage location in the direction of increasing coordinate
     */

    public Door(Room room1, Room room2, RoomTextures textures) {
        super(room1, room2, textures);
        setWidthAndBias(1, width / 2);
        setDepth((length - 1) / 2);
    }

    public Door(Room room1, Room room2, RoomTextures textures, int bias) {
        super(room1, room2, textures, 1, bias);
        setDepth((length - 1) / 2);
    }

    public Door(Room room1, Room room2, RoomTextures textures, int bias, int depth) {
        super(room1, room2, textures, 1, bias);
        setDepth(depth);
    }

    /**
     * Sets depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
        Coord shift = direction.vertical() ?
                new Coord(0, depth) : new Coord(depth, 0);
        doorCoord = location.shifted(shift);
    }

    /**
     * Closes door
     */
    boolean closeDoor() {
        if (state == CLOSED) return true;
        map.getCoordLock(doorCoord).lock();
        try {
            if (map.setObject(this, doorCoord)) {
                state = CLOSED;
                return true;
            }
        } finally {
            map.getCoordLock(doorCoord).unlock();
        }
        return false;
    }

    /**
     * Opens door
     */
    void openDoor() {
        if (state == OPEN) return;
        map.getCoordLock(doorCoord).lock();
        try {
            map.unsetObject(this, doorCoord);
            state = OPEN;
        } finally {
            map.getCoordLock(doorCoord).unlock();
        }
    }

    /**
     * Method, called, when hero interact with it
     */
    @Override
    public void interact() {
        switch (state) {
            case OPEN:
                closeDoor();
                break;
            case CLOSED:
                openDoor();
                break;
        }
    }

    /**
     * Checks is hero is near to the door
     */
    @Override
    public void update() {
        if (doorCoord.near(map.heroObject.getLocation())
                && !doorCoord.equals(map.heroObject.getLocation())) {
            highlighted = true;
            map.heroObject.interactiveObject = this;
        } else {
            highlighted = false;
            if (map.heroObject.interactiveObject == this) {
                map.heroObject.interactiveObject = null;
            }
        }
    }

    /**
     * Attaches the door and the background to map
     */
    @Override
    public Door attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        map.setObject(this, doorCoord);
        map.subscribeOnCoords(this, doorCoord, 3);
        update();
        return this;
    }


    /**
     * Analogically to previous, but deletes
     */
    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        map.unsetObject(this, doorCoord);
        map.unsubscribeFromCoords(this, doorCoord, 3);
        if (map.heroObject.interactiveObject == this) {
            map.heroObject.interactiveObject = null;
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixels = new HashMap<>();
        if (doorCoord.between(leftUp, rightDown)) {
            VisualPixel doorPixel;
            switch (state) {
                case OPEN:
                    doorPixel = direction.horizontal() ? DOOR_OPEN_HORIZONTAL : DOOR_OPEN_VERTICAl;
                    break;
                case CLOSED:
                    doorPixel = DOOR_CLOSED;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + state);
            }
            if (highlighted) {
                pixels.put(doorCoord, doorPixel.brighter(2.5));
            } else {
                pixels.put(doorCoord, doorPixel);
            }
        }
        return pixels;
    }

    protected enum DoorState {
        CLOSED,
        OPEN
    }


    /**
     * Takes snapshot
     */
    public JSONObject getSnapshot() {
        JSONObject jsonObject = super.getSnapshot();
        jsonObject.put("depth", depth);
        return jsonObject;
    }

    /**
     * Resores door from snapshot
     */
    public static Passage restoreFromSnapshot(JSONObject jsonObject, RoomSystem system) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Coord room1Location = new Coord(jsonObject.getInt("xRoom1"), jsonObject.getInt("yRoom1"));
        Coord room2Location = new Coord(jsonObject.getInt("xRoom2"), jsonObject.getInt("yRoom2"));
        Room room1 = null;
        Room room2 = null;
        for (Room room : system.rooms) {
            if (room.location.equals(room1Location)) room1 = room;
            if (room.location.equals(room2Location)) room2 = room;
        }
        return (Passage) Class
                .forName(jsonObject.getString("class"))
                .getConstructor(Room.class, Room.class, RoomTextures.class, int.class, int.class)
                .newInstance(room1, room2,
                        RoomTextures.restoreSnapshot(jsonObject.getJSONObject("textures")),
                        jsonObject.getInt("width"),
                        jsonObject.getInt("depth"));
    }
}
