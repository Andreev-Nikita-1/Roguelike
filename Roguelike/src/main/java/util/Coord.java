package util;

import java.util.Objects;

public class Coord {

    public static final Coord UP = new Coord(0, -1);
    public static final Coord DOWN = new Coord(0, 1);
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

    public Coord shift(Coord shift) {
        this.x += shift.x;
        this.y += shift.y;
        return this;
    }

    public Coord shifted(Coord shift) {
        return new Coord(this.x + shift.x, this.y + shift.y);
    }

    public Coord relative(Coord shift) {
        return new Coord(this.x - shift.x, this.y - shift.y);
    }

    public boolean near(Coord other) {
        return x == other.x && Math.abs(y - other.y) <= 1 || y == other.y && Math.abs(x - other.x) <= 1;
    }

    public boolean between(Coord leftUp, Coord rightDown) {
        return leftUp.x <= x && x <= rightDown.x && leftUp.y <= y && y <= rightDown.y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x &&
                y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
