package map.roomSystem;

import map.MapOfObjects;
import map.roomSystem.textures.RoomTextures;
import objects.MapObject;
import org.json.JSONObject;
import util.Coord;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static util.Direction.*;

/**
 * Class for room, containing its geometry, textures
 */
public class Room extends MapObject {
    public Coord location;
    public Coord rightDown;
    public Coord size;
    private int upWallWidth;
    private int downWallWidth;
    private int leftWallWidth;
    private int rightWallWidth;
    public List<Passage> passages = new ArrayList<>();
    public RoomTextures textures;
    private Wall upWall;
    private Wall downWall;
    private Wall rightWall;
    private Wall leftWall;
    private Background background;

    public Room(Coord location, Coord size,
                int upWallWidth,
                int downWallWidth,
                int leftWallWidth,
                int rightWallWidth,
                RoomTextures textures) {
        super();
        this.location = location;
        this.size = size;
        this.rightDown = location.shifted(size).shift(Coord.UP).shift(Coord.LEFT);
        this.upWallWidth = upWallWidth;
        this.downWallWidth = downWallWidth;
        this.leftWallWidth = leftWallWidth;
        this.rightWallWidth = rightWallWidth;
        this.textures = textures;
    }

    public Room(Coord location, Coord size,
                int wallWidth,
                RoomTextures textures) {
        this(location, size, wallWidth, wallWidth, wallWidth, wallWidth, textures);
    }

    /**
     * Adds passage
     */
    void addPassage(Passage passage) {
        passages.add(passage);
    }

    /**
     * Returns center of the room
     */
    public Coord center() {
        return new Coord((location.x + rightDown.x) / 2, (location.y + rightDown.y) / 2);
    }

    /**
     * Attach walls and background to map, using RoomTextures
     */
    @Override
    public Room attachToMap(MapOfObjects map) {
        super.attachToMap(map);
        if (upWallWidth > 0) {
            upWall = textures.createWall(
                    location.shifted(new Coord(0, -upWallWidth)),
                    UP,
                    size.x,
                    upWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == UP)
                            .collect(Collectors.toList())).attachToMap(map);
        }
        if (downWallWidth > 0) {
            downWall = textures.createWall(
                    location.shifted(new Coord(0, size.y)),
                    DOWN,
                    size.x,
                    downWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == DOWN)
                            .collect(Collectors.toList())).attachToMap(map);
        }
        if (rightWallWidth > 0) {
            rightWall = textures.createWall(
                    location.shifted(new Coord(size.x, -upWallWidth)),
                    RIGHT,
                    size.y + upWallWidth + downWallWidth,
                    rightWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == RIGHT)
                            .collect(Collectors.toList())).attachToMap(map);
        }
        if (leftWallWidth > 0) {
            leftWall = textures.createWall(
                    location.shifted(new Coord(-leftWallWidth, -upWallWidth)),
                    LEFT,
                    size.y + upWallWidth + downWallWidth,
                    leftWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == LEFT)
                            .collect(Collectors.toList())).attachToMap(map);
        }
        background = textures.createBackground(
                location,
                size.y,
                size.x
        ).attachToMap(map);
        return this;
    }

    /**
     * Returns passage to another room, if exists
     */
    public Passage passageTo(Room targetRoom, int minWidth) {
        for (Passage passage : passages) {
            if ((passage.room1 == this && passage.room2 == targetRoom
                    || passage.room2 == this && passage.room1 == targetRoom)
                    && passage.width >= minWidth) {
                return passage;
            }
        }
        return null;
    }

    /**
     * Deletes walls and background
     */
    @Override
    public void deleteFromMap() {
        upWall.deleteFromMap();
        downWall.deleteFromMap();
        rightWall.deleteFromMap();
        leftWall.deleteFromMap();
        background.deleteFromMap();
    }

    /**
     * Takes snapshot
     */
    public JSONObject getSnapshot() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", location.x);
        jsonObject.put("y", location.y);
        jsonObject.put("xSize", size.x);
        jsonObject.put("ySize", size.y);
        jsonObject.put("up", upWallWidth);
        jsonObject.put("down", downWallWidth);
        jsonObject.put("left", leftWallWidth);
        jsonObject.put("right", rightWallWidth);
        jsonObject.put("textures", textures.getSnapshot());
        return jsonObject;
    }

    /**
     * Restores from snapshot
     */
    public static Room restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return new Room(
                new Coord(jsonObject.getInt("x"), jsonObject.getInt("y")),
                new Coord(jsonObject.getInt("xSize"), jsonObject.getInt("ySize")),
                jsonObject.getInt("up"),
                jsonObject.getInt("down"),
                jsonObject.getInt("left"),
                jsonObject.getInt("right"),
                RoomTextures.restoreSnapshot(jsonObject.getJSONObject("textures"))
        );
    }
}
