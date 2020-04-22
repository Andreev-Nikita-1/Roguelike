package objects.neighbourfoods;

import map.MapOfObjects;
import objects.DependingObject;
import objects.MapObject;
import util.Coord;
import util.Direction;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Function;

public abstract class Neghbourhood extends MapObject implements DependingObject {
    protected Coord center;
    protected Coord centerSnapshot;
    protected int radius;
    protected int[][] mask;
    protected Direction[][] directions;


    public Neghbourhood(MapOfObjects map, Coord center, int radius) {
        this(map, center, radius, Coord::euqlideanScaled);
    }

    protected Function<Coord, Double> norm;

    public Neghbourhood(MapOfObjects map, Coord center, int radius, Function<Coord, Double> norm) {
        super(map);
        this.center = center;
        this.centerSnapshot = new Coord(center);
        this.radius = radius;
        this.norm = norm;
        directions = new Direction[2 * radius + 1][2 * radius + 1];
        mask = new int[2 * radius + 1][2 * radius + 1];
    }

    @Override
    public Neghbourhood attachToMap() {
        super.attachToMap();
        map.subscribeOnCoords(this, center, radius);
        return this;
    }

    public int number(Coord coord) {
        if (norm.apply(coord.relative(center)) <= radius) {
            Coord shift = coord.relative(center);
            return mask[shift.x + radius][shift.y + radius];
        }
        return -1;
    }

    public boolean accessible(Coord coord) {
        return number(coord) != -1;
    }

    public int number(Coord coord, int distance, Function<Coord, Double> norm) {
        if (norm.apply(coord.relative(center)) > radius + distance) return -1;
        int[][] used = new int[2 * distance + 1][2 * distance + 1];
        Deque<Coord> deque = new ArrayDeque<>();
        deque.add(coord);
        for (int[] row : used) {
            Arrays.fill(row, -1);
        }
        used[distance][distance] = 0;
        while (!deque.isEmpty()) {
            Coord current = deque.pollFirst();
            if (norm.apply(current.relative(coord)) > distance) continue;
            if (Coord.lInftyNorm(current.relative(center)) <= radius) {
                if (mask[current.x - center.x + radius][current.y - center.y + radius] != -1) {
                    return mask[current.x - center.x + radius][current.y - center.y + radius] +
                            used[current.x - coord.x + distance][current.y - coord.y + distance];
                }
            }
            for (Direction direction : Direction.getDirections()) {
                Coord next = current.shifted(Coord.fromDirection(direction));
                Coord currentRelative = current.relative(coord).shift(new Coord(distance, distance));
                Coord nextRelative = currentRelative.shifted(Coord.fromDirection(direction));
                if (Coord.lInftyNorm(next.relative(coord)) <= distance
                        && used[nextRelative.x][nextRelative.y] == -1) {
                    used[nextRelative.x][nextRelative.y] = used[currentRelative.x][currentRelative.y] + 1;
                    deque.addLast(next);
                }
            }
        }
        return -1;
    }

    public Direction follow(Coord c) {
        if (norm.apply(center.relative(c)) <= radius) {
            Coord shift = c.relative(center);
            return directions[shift.x + radius][shift.y + radius];
        }
        return null;
    }
}
