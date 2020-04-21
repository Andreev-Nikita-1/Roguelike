package map.roomSystem;

import objects.MapObject;
import util.Coord;
import util.Direction;

import static util.Direction.UP;

public abstract class Passage extends MapObject {

    public Direction direction;
    public Coord location;
    public int width;
    public int length;
    public Room room1;
    public Room room2;


    public void setWidestPassage() {
        Coord commonLengthY = segmentsIntersection(
                new Coord(room1.location.y, room1.rightDown.y),
                new Coord(room2.location.y, room2.rightDown.y));
        if (commonLengthY.y >= 1) {
            int distance = room2.location.x - room1.rightDown.x;
            if (distance > 0) {
                width = commonLengthY.y;
                direction = Direction.RIGHT;
                this.length = distance;
                location = new Coord(room1.rightDown.x + 1, commonLengthY.x);
                return;
            }
            distance = room1.location.x - room2.rightDown.x;
            if (distance > 0) {
                width = commonLengthY.y;
                direction = Direction.LEFT;
                this.length = distance;
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
                this.length = distance;
                location = new Coord(commonLengthX.x, room1.rightDown.y + 1);
            }
            distance = room1.location.y - room2.rightDown.y;
            if (distance > 0) {
                width = commonLengthX.y;
                direction = UP;
                this.length = distance;
                location = new Coord(commonLengthX.x, room2.rightDown.y + 1);
            }
        }
    }

    public Passage(Room room1, Room room2) {
        super(room1.map);
        this.room1 = room1;
        this.room2 = room2;
        room1.addPassage(this);
        room2.addPassage(this);
        setWidestPassage();
    }

    public Passage(Room room1, Room room2, int width, int bias) {
        this(room1, room2);
        setWidthAndBias(width, bias);
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

    public static Coord segmentsIntersection(Coord s1, Coord s2) {
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
