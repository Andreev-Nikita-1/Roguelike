package util;

import map.MapOfObjects;
import objects.DependingObject;

import java.util.*;
import java.util.function.Function;

import static util.Coord.*;

public class AccessNeighbourhood implements DependingObject {
    private MapOfObjects map;
    private Coord center;
    private Coord centerSnapshot;
    private int radius;
    private Direction[][] directions;

    public AccessNeighbourhood(MapOfObjects map, Coord center, int radius) {
        this.map = map;
        this.center = center;
        centerSnapshot = new Coord(center);
        this.radius = radius;
        directions = new Direction[2 * radius + 1][2 * radius + 1];
    }

    private volatile boolean relevant = false;

    private void requireRelevancy() {
        if (!relevant) {
            relevant = true;
            reset();
            resetSubscriptions();
        }
    }


    private Coord subscriptionCenter;

    private synchronized void resetSubscriptions() {
        if (subscriptionCenter == null) {
            subscriptionCenter = new Coord(center);
            map.subscribeOnCoords(this, subscriptionCenter, radius);
        } else {
            map.unsubscribeFromCoords(this, subscriptionCenter, radius);
            subscriptionCenter = new Coord(center);
            map.subscribeOnCoords(this, subscriptionCenter, radius);
        }
    }

    public void delete() {
        if (subscriptionCenter != null) {
            map.unsubscribeFromCoords(this, subscriptionCenter, radius);
        }
    }

    private void reset() {
        Deque<Coord> deque = new ArrayDeque<>();
        deque.add(ZERO);
        int[][] tempMask = new int[2 * radius + 1][2 * radius + 1];
        for (int[] row : tempMask) {
            Arrays.fill(row, -1);
        }
        Direction[][] tempDirections = new Direction[2 * radius + 1][2 * radius + 1];
        tempMask[radius][radius] = 0;
        Coord tempCenter = new Coord(center);
        while (!deque.isEmpty()) {
            Coord current = deque.pollFirst();
            if (Coord.lInftyNorm(current) >= radius) continue;
            for (Direction direction : Direction.getDirections()) {
                Coord c = current.shifted(Coord.fromDirection(direction));
                if (map.inside(tempCenter.shifted(c))
                        && tempMask[c.x + radius][c.y + radius] == -1) {
                    tempDirections[c.x + radius][c.y + radius] = direction.opposite();
                    tempMask[c.x + radius][c.y + radius] = tempMask[current.x + radius][current.y + radius] + 1;
                    if (!map.isTaken(tempCenter.shifted(c))) {
                        deque.addLast(c);
                    }
                }
            }
        }
        synchronized (this) {
            centerSnapshot = tempCenter;
            directions = tempDirections;
        }
    }

    public boolean accessible(Coord c, Function<Coord, Double> norm, int radius) {
        return c.equals(center) || accessibleDirection(c, norm, radius) != null;
    }

    public boolean accessible(Coord c) {
        return c.equals(center) || accessibleDirection(c) != null;
    }

    public Direction accessibleDirection(Coord c, Function<Coord, Double> norm, int radius) {
        if (norm.apply(center.relative(c)) <= radius) {
            requireRelevancy();
            Coord shift = c.relative(centerSnapshot);
            if (Coord.lInftyNorm(shift) <= this.radius) {
                return directions[shift.x + this.radius][shift.y + this.radius];
            }
        }
        return null;
    }

    public Direction accessibleDirection(Coord c) {
        return accessibleDirection(c, Coord::lInftyNorm, radius);
    }

    @Override
    public void update() {
        relevant = false;
    }
}
