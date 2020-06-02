package util;

import java.util.Objects;

/**
 * This class represents a coordinate, containing pair of ints
 */
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

    /**
     * Shifts this to shift vector
     */
    public Coord shift(Coord shift) {
        this.x += shift.x;
        this.y += shift.y;
        return this;
    }

    /**
     * Returns shifted copy of this
     */
    public Coord shifted(Coord shift) {
        return new Coord(this.x + shift.x, this.y + shift.y);
    }

    public Coord shifted(Direction direction) {
        return shifted(fromDirection(direction));
    }

    public Coord shift(Direction direction) {
        return shift(fromDirection(direction));
    }

    /**
     * Returns relative location of this, relatively to other (just defference between this and other)
     */
    public Coord relative(Coord other) {
        return new Coord(this.x - other.x, this.y - other.y);
    }

    /**
     * Returns true, if you can do at most one move from other, to achieve this
     */
    public boolean near(Coord other) {
        return x == other.x && Math.abs(y - other.y) <= 1 || y == other.y && Math.abs(x - other.x) <= 1;
    }


    /**
     * Returns true if this is located in rectangle, which has vertices leftUp and rightDown
     */
    public boolean between(Coord leftUp, Coord rightDown) {
        return leftUp.x <= x && x <= rightDown.x && leftUp.y <= y && y <= rightDown.y;
    }

    /**
     * Returns the direction to move, for achieving the target directly
     */
    public Direction properDirection(Coord target) {
        Coord vector = target.relative(this);
        if (Math.abs(vector.x) > Math.abs(vector.y)) {
            if (vector.x > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (vector.y > 0) {
                return Direction.DOWN;
            } else if (vector.y < 0) {
                return Direction.UP;
            }
        }
        return null;
    }


    /**
     * Converts Direction object to Coord object
     */
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

    /**
     * Manhattan metrics
     */
    public static Double manhattan(Coord coord) {
        return Double.valueOf(Math.abs(coord.x) + Math.abs(coord.y));
    }

    /**
     * L-infinity metrics
     */

    public static Double lInftyNorm(Coord coord) {
        return Double.valueOf(Math.max(Math.abs(coord.x), Math.abs(coord.y)));
    }

    /**
     * Euclidean metrics
     */
    public static Double euqlidean(Coord coord) {
        return Math.sqrt(coord.x * coord.x + coord.y * coord.y);
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
