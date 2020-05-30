package map.roomSystem;

import map.MapOfObjects;
import map.roomSystem.textures.RoomTextures;
import objects.MapObject;
import org.json.JSONObject;
import util.Coord;
import util.Direction;

import java.lang.reflect.InvocationTargetException;

import static util.Direction.*;

/**
 * Class for passage between rooms
 */
public abstract class Passage extends MapObject {

    public Direction direction;
    public Coord location;
    public int width;
    public int bias;
    public int length;
    public Room room1;
    public Room room2;

    protected RoomTextures textures;
    protected Background background;


    /**
     * Returns true, if mob can go through it
     */
    public abstract boolean passable(int width);


    /**
     * Sets maximal width, equals to length of common wall of twe rooms,
     * location, and length of the passage
     */
    void setWidestPassage() {
        bias = 0;
        Coord commonLengthY = segmentsIntersection(
                new Coord(room1.location.y, room1.rightDown.y),
                new Coord(room2.location.y, room2.rightDown.y));
        if (commonLengthY.y >= 1) {
            int distance = room2.location.x - room1.rightDown.x;
            if (distance > 0) {
                width = commonLengthY.y;
                direction = Direction.RIGHT;
                this.length = distance - 1;
                location = new Coord(room1.rightDown.x + 1, commonLengthY.x);
                return;
            }
            distance = room1.location.x - room2.rightDown.x;
            if (distance > 0) {
                width = commonLengthY.y;
                direction = Direction.LEFT;
                this.length = distance - 1;
                location = new Coord(room2.rightDown.x + 1, commonLengthY.x);
                return;
            }
        }
        Coord commonLengthX = segmentsIntersection(
                new Coord(room1.location.x, room1.rightDown.x),
                new Coord(room2.location.x, room2.rightDown.x));
        if (commonLengthX.y >= 1) {
            int distance = room2.location.y - room1.rightDown.y;
            if (distance > 0) {
                width = commonLengthX.y;
                direction = Direction.DOWN;
                this.length = distance - 1;
                location = new Coord(commonLengthX.x, room1.rightDown.y + 1);
            }
            distance = room1.location.y - room2.rightDown.y;
            if (distance > 0) {
                width = commonLengthX.y;
                direction = UP;
                this.length = distance - 1;
                location = new Coord(commonLengthX.x, room2.rightDown.y + 1);
            }
        }
    }

    /**
     * Creates passage with maximal width
     */
    public Passage(Room room1, Room room2, RoomTextures textures) {
        super();
        this.room1 = room1;
        this.room2 = room2;
        room1.addPassage(this);
        room2.addPassage(this);
        this.textures = textures;
        setWidestPassage();
    }

    /**
     * Creates passage with given width and bias, counted from the start of common wall in the direction of increasing coordinate
     */
    public Passage(Room room1, Room room2, RoomTextures textures, int width, int bias) {
        this(room1, room2, textures);
        setWidthAndBias(width, bias);
    }

    /**
     * Attaches background
     */
    @Override
    public Passage attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        if (direction.horizontal()) {
            background = textures.createBackground(location, width, length).attachToMap(map);
        } else {
            background = textures.createBackground(location, length, width).attachToMap(map);
        }
        return this;
    }

    /**
     * Deletes background
     */
    @Override
    public void deleteFromMap() {
        background.deleteFromMap();
        super.deleteFromMap();
    }

    /**
     * Returns coordinate of right down corner of the passage
     */
    public Coord rightDown() {
        Coord shift = direction.horizontal() ? new Coord(length - 1, width - 1) : new Coord(width - 1, length - 1);
        return location.shifted(shift);
    }

    /**
     * Sets width and bias
     */
    void setWidthAndBias(int width, int bias) {
        setWidestPassage();
        this.width = width;
        this.bias = bias;
        Coord shift = direction.vertical() ? new Coord(bias, 0) : new Coord(0, bias);
        location.shift(shift);
    }

    /**
     * Return the direction, in which passage leads from given room
     */
    Direction directedTo(Room room) {
        if (room1 == room) {
            return direction;
        } else if (room2 == room) {
            return direction.opposite();
        } else {
            return null;
        }
    }

    /**
     * Returns coordinate in room interior, which is last before entering the passage
     */
    public Coord entryLocation(Room room) {
        Coord shiftWidth = direction.horizontal() ? new Coord(-1, width / 2) : new Coord(width / 2, -1);
        Coord shiftLength = direction.horizontal() ? new Coord(length + 1, 0) : new Coord(0, length + 1);
        if ((direction == RIGHT || direction == DOWN) && room2 == room
                || (direction == LEFT || direction == UP) && room1 == room) {
            shiftWidth.shift(shiftLength);
        }
        return location.shifted(shiftWidth);
    }


    /**
     * Returns room, to the other side of this passage
     */
    public Room otherSideRoom(Room room) {
        if (room1 == room) {
            return room2;
        } else {
            return room1;
        }
    }


    private static Coord segmentsIntersection(Coord s1, Coord s2) {
        if (s1.y < s2.x || s2.y < s1.x) {
            return new Coord(-1, -1);
        }
        if (s2.x < s1.x) {
            Coord t = s1;
            s1 = s2;
            s2 = t;
        }
        return new Coord(s2.x, Math.min(s1.y, s2.y) - s2.x + 1);
    }


    /**
     * Takes snapshot
     */
    public JSONObject getSnapshot() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("width", width);
        jsonObject.put("bias", bias);
        jsonObject.put("textures", textures.getSnapshot());
        jsonObject.put("class", this.getClass().getName());
        jsonObject.put("xRoom1", room1.location.x);
        jsonObject.put("yRoom1", room1.location.y);
        jsonObject.put("xRoom2", room2.location.x);
        jsonObject.put("yRoom2", room2.location.y);
        return jsonObject;
    }

    /**
     * Restores default passage from snapshot. Can be overriden (just defined) by the successors
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
                        jsonObject.getInt("bias"));
    }

    /**
     * Restores passage from snapshot. Invokes method "restoreFromSnapshot" on saved class.
     */
    public static Passage restoreSnapshot(JSONObject jsonObject, RoomSystem system) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Passage) Class
                .forName(jsonObject.getString("class"))
                .getMethod("restoreFromSnapshot", JSONObject.class, RoomSystem.class)
                .invoke(null, jsonObject, system);
    }
}
