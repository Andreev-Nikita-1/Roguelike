package objects.neighbourfoods;

import map.MapOfObjects;
import util.Coord;
import util.Direction;

import java.util.*;
import java.util.function.Function;

import static util.Coord.*;

public class AccessNeighbourhood extends Neghbourhood {


    protected Direction[][] directions;

    public AccessNeighbourhood(MapOfObjects map, Coord center, int radius, Function<Coord, Double> norm) {
        super(map, center, radius, norm);
    }

    public AccessNeighbourhood(MapOfObjects map, Coord center, int radius) {
        super(map, center, radius);
        directions = new Direction[2 * radius + 1][2 * radius + 1];
    }

    @Override
    public void reset() {
        Deque<Coord> deque = new ArrayDeque<>();
        deque.add(ZERO);
        tempMask = new int[2 * radius + 1][2 * radius + 1];
        for (int[] row : tempMask) {
            Arrays.fill(row, -1);
        }
        directions = new Direction[2 * radius + 1][2 * radius + 1];
        tempMask[radius][radius] = 0;
        while (!deque.isEmpty()) {
            Coord current = deque.pollFirst();
            if (norm.apply(current) >= radius) continue;
            for (Direction direction : Direction.getDirections()) {
                Coord c = current.shifted(Coord.fromDirection(direction));
                if (map.inside(center.shifted(c))
                        && tempMask[c.x + radius][c.y + radius] == -1) {
                    directions[c.x + radius][c.y + radius] = direction.opposite();
                    tempMask[c.x + radius][c.y + radius] = tempMask[current.x + radius][current.y + radius] + 1;
                    if (!map.isTaken(center.shifted(c))) {
                        deque.addLast(c);
                    }
                }
            }
        }
    }

    public Direction follow(Coord c) {
        if (norm.apply(center.relative(c)) <= radius) {
            Coord shift = c.relative(center);
            return directions[shift.x + radius][shift.y + radius];
        }
        return null;
    }

    @Override
    public AccessNeighbourhood attachToMap() {
        return (AccessNeighbourhood) super.attachToMap();
    }
}
