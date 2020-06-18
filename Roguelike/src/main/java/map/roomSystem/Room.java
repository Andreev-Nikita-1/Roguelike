package map.roomSystem;

import map.MapOfObjects;
import objects.MapObject;
import util.Coord;
import util.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static util.Direction.*;


public class Room extends MapObject {
    public Coord location;
    public Coord rightDown;
    public Coord size;
    private int upWallWidth;
    private int downWallWidth;
    private int leftWallWidth;
    private int rightWallWidth;
    public List<Passage> passages = new ArrayList<>();
    private RoomTextures textures;
    private Wall upWall;
    private Wall downWall;
    private Wall rightWall;
    private Wall leftWall;
    private Background background;

    public Room(MapOfObjects map,
                Coord location, Coord size,
                int upWallWidth,
                int downWallWidth,
                int leftWallWidth,
                int rightWallWidth,
                RoomTextures textures) {
        super(map);
        this.location = location;
        this.size = size;
        this.rightDown = location.shifted(size).shift(Coord.UP).shift(Coord.LEFT);
        this.upWallWidth = upWallWidth;
        this.downWallWidth = downWallWidth;
        this.leftWallWidth = leftWallWidth;
        this.rightWallWidth = rightWallWidth;
        this.textures = textures;
    }

    public Room(MapOfObjects map,
                Coord location, Coord size,
                int wallWidth,
                RoomTextures textures) {
        this(map, location, size, wallWidth, wallWidth, wallWidth, wallWidth, textures);
    }

    public void addPassage(Passage passage) {
        passages.add(passage);
    }

    public Coord center() {
        return new Coord((location.x + rightDown.x) / 2, (location.y + rightDown.y) / 2);
    }

    @Override
    public Room attachToMap() {
        if (upWallWidth > 0) {
            upWall = textures.createWall(map,
                    location.shifted(new Coord(0, -upWallWidth)),
                    true,
                    size.x,
                    upWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == UP)
                            .collect(Collectors.toList())).attachToMap();
        }
        if (downWallWidth > 0) {
            downWall = textures.createWall(map,
                    location.shifted(new Coord(0, size.y)),
                    true,
                    size.x,
                    downWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == DOWN)
                            .collect(Collectors.toList())).attachToMap();
        }
        if (rightWallWidth > 0) {
            rightWall = textures.createWall(map,
                    location.shifted(new Coord(size.x, -upWallWidth)),
                    false,
                    size.y + upWallWidth + downWallWidth,
                    rightWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == RIGHT)
                            .collect(Collectors.toList())).attachToMap();
        }
        if (leftWallWidth > 0) {
            leftWall = textures.createWall(map,
                    location.shifted(new Coord(-leftWallWidth, -upWallWidth)),
                    false,
                    size.y + upWallWidth + downWallWidth,
                    leftWallWidth,
                    passages.stream()
                            .filter(passage -> passage.directedTo(this) == LEFT)
                            .collect(Collectors.toList())).attachToMap();
        }
        background = textures.createBackground(map, location.shifted(new Coord(-leftWallWidth, -upWallWidth)),
                size.y + upWallWidth + downWallWidth,
                size.x + leftWallWidth + rightWallWidth
        ).attachToMap();
        return this;
    }

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

    @Override
    public void deleteFromMap() {
        upWall.deleteFromMap();
        downWall.deleteFromMap();
        rightWall.deleteFromMap();
        leftWall.deleteFromMap();
        background.deleteFromMap();
    }
}
