package util;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction[] getDirections() {
        return new Direction[]{UP, DOWN, LEFT, RIGHT};
    }

    public boolean horizontal() {
        return this == LEFT || this == RIGHT;
    }

    public boolean vertical() {
        return this == UP || this == DOWN;
    }

    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
        }
        return null;
    }
}
