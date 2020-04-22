package objects.neighbourfoods;

import map.MapOfObjects;
import objects.creatures.Creature;
import objects.creatures.Swordsman;
import objects.neighbourfoods.Neghbourhood;
import util.Coord;
import util.Direction;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

public class ViewNeighbourhood extends Neghbourhood {

    private Deque<Coord> borders;

    public ViewNeighbourhood(MapOfObjects map, Coord center, int radius, Function<Coord, Double> norm) {
        super(map, center, radius, norm);
    }

    public ViewNeighbourhood(MapOfObjects map, Coord center, int radius) {
        super(map, center, radius);
    }

    public Deque<Coord> getBorders() {
        return borders;
    }

    static volatile int i = 0;

    @Override
    public synchronized void update() {
//        map.unsubscribeFromCoords(this, centerSnapshot, radius);
        map.unsubscribeFromCoord(this, centerSnapshot);
        List<Coord> frame = new ArrayList<>();
        borders = new ConcurrentLinkedDeque<>();
        int frameRadius = 2 * radius;
        for (int i = 0; i < 2 * frameRadius + 1; i++) {
            frame.add(new Coord(frameRadius, i - frameRadius).shift(center));
            frame.add(new Coord(-frameRadius, i - frameRadius).shift(center));
            frame.add(new Coord(i - frameRadius, frameRadius).shift(center));
            frame.add(new Coord(i - frameRadius, -frameRadius).shift(center));
        }
        for (int[] row : mask) {
            Arrays.fill(row, -1);
        }
        mask[radius][radius] = 0;
        for (Coord frameCoord : frame) {
            Coord current = new Coord(center);
            int counter = 0;
            Direction direction;
            Coord currentRelative;
            do {
                direction = current.walkTheLine(center, frameCoord);
                currentRelative = current.relative(center).shift(new Coord(radius, radius));
                if (norm.apply(current.relative(center)) >= radius || !map.inside(current)
                        || map.isTaken(current) && !(map.getObject(current) instanceof Swordsman)) {
                    break;
                }
                if (mask[currentRelative.x][currentRelative.y] == -1 ||
                        mask[currentRelative.x][currentRelative.y] > counter + 1) {
                    mask[currentRelative.x][currentRelative.y] = counter + 1;
                    directions[currentRelative.x][currentRelative.y] = direction.opposite();
                }
                counter++;
            } while (direction != null);

        }
//        map.subscribeOnCoords(this, center, radius);
        map.subscribeOnCoord(this, center);
        centerSnapshot = new Coord(center);
    }
}

