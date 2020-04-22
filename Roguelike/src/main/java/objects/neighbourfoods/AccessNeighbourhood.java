package objects.neighbourfoods;

import map.MapOfObjects;
import objects.neighbourfoods.Neghbourhood;
import util.Coord;
import util.Direction;

import java.util.*;
import java.util.function.Function;

import static util.Coord.*;

public class AccessNeighbourhood extends Neghbourhood {


    public AccessNeighbourhood(MapOfObjects map, Coord center, int radius, Function<Coord, Double> norm) {
        super(map, center, radius, norm);
    }

    public AccessNeighbourhood(MapOfObjects map, Coord center, int radius) {
        super(map, center, radius);
    }

    @Override
    public synchronized void update() {
        map.unsubscribeFromCoords(this, centerSnapshot, radius);
        Deque<Coord> deque = new ArrayDeque<>();
        deque.add(ZERO);
        for (int[] row : mask) {
            Arrays.fill(row, -1);
        }
        mask[radius][radius] = 0;
        while (!deque.isEmpty()) {
            Coord current = deque.pollFirst();
            if (norm.apply(current) >= radius) continue;
            for (Direction direction : Direction.getDirections()) {
                Coord c = current.shifted(Coord.fromDirection(direction));
                if (map.inside(center.shifted(c))
                        && mask[c.x + radius][c.y + radius] == -1) {
                    directions[c.x + radius][c.y + radius] = direction.opposite();
                    mask[c.x + radius][c.y + radius] = mask[current.x + radius][current.y + radius] + 1;
                    if (!map.isTaken(center.shifted(c))) {
                        deque.addLast(c);
                    }
                }
            }
        }
        map.subscribeOnCoords(this, center, radius);
        centerSnapshot = new

                Coord(center);
    }
}
