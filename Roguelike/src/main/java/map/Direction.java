package map;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction[] getDirections() {
        return new Direction[]{UP, DOWN, LEFT, RIGHT};
    }
}
