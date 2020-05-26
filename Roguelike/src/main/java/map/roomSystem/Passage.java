package map.roomSystem;

import map.roomSystem.textures.RoomTextures;
import objects.MapObject;
import util.Coord;
import util.Direction;

import static util.Direction.*;

public abstract class Passage extends MapObject {

    public Direction direction;
    public Coord location;
    public int width;
    public int length;
    public Room room1;
    public Room room2;

    protected RoomTextures textures;
    protected Background background;


    public abstract boolean passable(int width);

    public void setWidestPassage() {
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

    public Passage(Room room1, Room room2, RoomTextures textures) {
        super(room1.map);
        this.room1 = room1;
        this.room2 = room2;
        room1.addPassage(this);
        room2.addPassage(this);
        this.textures = textures;
        setWidestPassage();
    }

    public Passage(Room room1, Room room2, RoomTextures textures, int width, int bias) {
        this(room1, room2, textures);
        setWidthAndBias(width, bias);
    }

    @Override
    public Passage attachToMap() {
        super.attachToMap();
        if (direction.horizontal()) {
            background = textures.createBackground(map, location, width, length).attachToMap();
        } else {
            background = textures.createBackground(map, location, length, width).attachToMap();
        }
        return this;
    }

    @Override
    public void deleteFromMap() {
        background.deleteFromMap();
        super.deleteFromMap();
    }

    public Coord rightDown() {
        Coord shift = direction.horizontal() ? new Coord(length - 1, width - 1) : new Coord(width - 1, length - 1);
        return location.shifted(shift);
    }

    public void setWidthAndBias(int width, int bias) {
        setWidestPassage();
        this.width = width;
        Coord shift = direction.vertical() ? new Coord(bias, 0) : new Coord(0, bias);
        location.shift(shift);
    }

    public Direction directedTo(Room room) {
        if (room1 == room) {
            return direction;
        } else if (room2 == room) {
            return direction.opposite();
        } else {
            return null;
        }
    }

    public Coord entryLocation(Room room) {
        Coord shiftWidth = direction.horizontal() ? new Coord(-1, width / 2) : new Coord(width / 2, -1);
        Coord shiftLength = direction.horizontal() ? new Coord(length + 1, 0) : new Coord(0, length + 1);
        if ((direction == RIGHT || direction == DOWN) && room2 == room
                || (direction == LEFT || direction == UP) && room1 == room) {
            shiftWidth.shift(shiftLength);
        }
        return location.shifted(shiftWidth);
    }


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
}
