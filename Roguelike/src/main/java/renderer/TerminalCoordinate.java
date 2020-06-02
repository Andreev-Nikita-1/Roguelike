package renderer;

import util.Coord;

/**
 * This class is responsible for computing a frame, inside which objects on map will be drawn.
 */
class TerminalCoordinate {

    private Coord mapSize;
    private Coord coord = new Coord(Coord.ZERO);
    private Coord size = new Coord(Coord.ZERO);
    private Coord heroRelative = new Coord(Coord.ZERO);
    private Coord threshold = new Coord(Coord.ZERO);
    private Coord heroPred = new Coord(Coord.ZERO);
    private double thresholdCoeff = 0.4;

    TerminalCoordinate(Coord mapSize) {
        this.mapSize = mapSize;
    }

    /**
     * Returns coordinate, which will be drawn in the left up corner of the terminal
     */
    Coord getLeftUp(int newXSize, int newYSize, Coord heroLocation) {
        if (size.x != newXSize || size.y != newYSize) {
            setFrame(newXSize, newYSize, heroLocation);
        } else {
            update(heroLocation);
        }
        return coord;
    }

    private void update(Coord heroNew) {
        Coord diff = new Coord(heroNew.x - heroPred.x, heroNew.y - heroPred.y);
        if (diff.x == 0 && diff.y == 0) {
            return;
        }
        Coord heroRelativeNew = heroRelative.shifted(diff);
        if (heroRelativeNew.x >= threshold.x &&
                heroRelativeNew.x <= size.x - threshold.x ||
                heroRelativeNew.x < threshold.x && coord.x == 0 ||
                heroRelativeNew.x > size.x - threshold.x && size.x + coord.x == mapSize.x
        ) {
            heroRelative.x = heroRelativeNew.x;
        } else {
            coord.x += diff.x;
            if (coord.x < 0) {
                coord.x = 0;
            }
            if (coord.x + size.x > mapSize.x) {
                coord.x = mapSize.x - size.x;
            }
        }
        if (heroRelativeNew.y >= threshold.y &&
                heroRelativeNew.y <= size.y - threshold.y ||
                heroRelativeNew.y < threshold.y && coord.y == 0 ||
                heroRelativeNew.y > size.y - threshold.y && size.y + coord.y == mapSize.y
        ) {
            heroRelative.y = heroRelativeNew.y;
        } else {
            coord.y += diff.y;
            if (coord.y < 0) {
                coord.y = 0;
            }
            if (coord.y + size.y > mapSize.y) {
                coord.y = mapSize.y - size.y;
            }
        }
        heroPred = new Coord(heroNew);
    }

    private void setFrame(int newXSize, int newYSize, Coord heroLocation) {
        size = new Coord(newXSize, newYSize);
        threshold = new Coord((int) (thresholdCoeff * newXSize), (int) (thresholdCoeff * newYSize));
        heroPred = new Coord(heroLocation);
        int x = centralOneDimFrame(newXSize, mapSize.x, heroLocation.x);
        int y = centralOneDimFrame(newYSize, mapSize.y, heroLocation.y);
        coord = new Coord(x, y);
        heroRelative = new Coord(heroLocation.x - x, heroLocation.y - y);
    }

    private static int centralOneDimFrame(int size, int maxSize, int location) {
        int a = size / 2;
        int b = size - a;
        if (location - a < 0) {
            return 0;
        }
        if (location + b >= maxSize) {
            return maxSize - size;
        }
        return location - a;
    }
}
