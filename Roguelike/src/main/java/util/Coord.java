package util;

public class Coord {

    public static final Coord UP = new Coord(0, 1);
    public static final Coord DOWN = new Coord(0, -1);
    public static final Coord LEFT = new Coord(-1, 0);
    public static final Coord RIGHT = new Coord(1, 0);
    public static final Coord ZERO = new Coord(0, 0);

    public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord(Coord coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public void shift(Coord shift) {
        this.x += shift.x;
        this.y += shift.y;
    }

    public Coord shifted(Coord shift) {
        return new Coord(this.x + shift.x, this.y + shift.y);
    }

    public static Coord fromDirection(Direction direction) {
        switch (direction) {
            case UP:
                return Coord.UP;
            case LEFT:
                return Coord.LEFT;
            case DOWN:
                return Coord.DOWN;
            case RIGHT:
                return Coord.RIGHT;
        }
        return Coord.ZERO;
    }
}
