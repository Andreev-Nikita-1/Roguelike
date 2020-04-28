package util;

import map.MapOfObjects;

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

    public Coord relative(Coord other) {
        return new Coord(this.x - other.x, this.y - other.y);
    }

    public boolean near(Coord other) {
        return x == other.x && Math.abs(y - other.y) <= 1 || y == other.y && Math.abs(x - other.x) <= 1;
    }

    public boolean between(Coord leftUp, Coord rightDown) {
        return leftUp.x <= x && x <= rightDown.x && leftUp.y <= y && y <= rightDown.y;
    }

    public boolean pixelCrossedBySegment(Coord start, Coord finish) {
        Coord vector = finish.relative(start).multiply(2);
        Coord square = this.relative(start).multiply(2);
        Coord[] vertex = new Coord[]{
                square.shifted(new Coord(1, 1)),
                square.shifted(new Coord(1, -1)),
                square.shifted(new Coord(-1, 1)),
                square.shifted(new Coord(-1, -1))
        };
        for (int i = 0; i < 4; i++) {
            Coord v = vertex[i];
            Coord w = vertex[(i + 1) % 4];
            if (segmentLineIntersect(v, w, vector) <= 0
                    && segmentLineIntersect(ZERO.relative(v), vector.relative(v), w.relative(v)) <= 0) {
                return true;
            }
        }
        return false;
    }

    public Direction walkTheLine(Coord start, Coord finish) {
        if (this.equals(finish)) return null;
        Direction xDirection = (finish.x - start.x > 0) ? Direction.RIGHT : Direction.LEFT;
        Direction yDirection = (finish.y - start.y > 0) ? Direction.DOWN : Direction.UP;
        for (Direction direction : new Direction[]{xDirection, yDirection}) {
            if (this.shifted(fromDirection(direction)).pixelCrossedBySegment(start, finish)) {
                shift(fromDirection(direction));
                return direction;
            }
        }
        return null;
    }

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

    private static int segmentLineIntersect(Coord a, Coord b, Coord vector) {
        return (a.x * vector.y - a.y * vector.x) * (b.x * vector.y - b.y * vector.x);
    }

    public Coord multiply(int m) {
        x *= m;
        y *= m;
        return this;
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

    public static Double manhattan(Coord coord) {
        return Double.valueOf(Math.abs(coord.x) + Math.abs(coord.y));
    }

    public static Double lInftyNorm(Coord coord) {
        return Double.valueOf(Math.max(Math.abs(coord.x), Math.abs(coord.y)));
    }

    public static Double euqlidean(Coord coord) {
        return Math.sqrt(coord.x * coord.x + coord.y * coord.y);
    }

    public static Double euqlideanScaled(Coord coord) {
        return Math.sqrt(coord.x * coord.x + (double) coord.y * coord.y * 4 / 3);
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
