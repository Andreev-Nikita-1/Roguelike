package util;

import map.MapOfObjects;
import objects.DependingObject;

import java.util.*;
import java.util.function.Function;

import static util.Coord.*;


/**
 * Class that computes path to certain coordinate from its neighbourhood.
 * Uses for mobs finding path to their targets
 */
public class AccessNeighbourhood implements DependingObject {
    private MapOfObjects map;
    private Coord center;
    private Coord centerSnapshot;
    private int radius;
    private Direction[][] directions;


    /**
     * @param map    - map
     * @param center - target, for finding path to
     * @param radius - only coordinates which distance to target is less then radius will be processed
     */
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


    /**
     * Called for reset map coordinates subscriptions (neighbourhood center may change during
     * the period of existence this)
     */
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

    /**
     * Has to be deleted manually, for deleting from subscribers lists of map locks
     */
    public void delete() {
        if (subscriptionCenter != null) {
            map.unsubscribeFromCoords(this, subscriptionCenter, radius);
        }
    }


    /**
     * Method, that computes proper directions for center neighbourhood.
     * Called only when someone calls accessibleDirection method
     */
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
                Coord c = current.shifted(direction);
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


    /**
     * Returns the direction for achieving the target
     *
     * @param c      - initial coordinate, from which you try to find path to the target
     * @param norm   - will return proper direction if norm(c - target) is less then radius, null otherwise
     * @param radius
     * @return
     */
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

    /**
     * Returns true, if previous method returned not null
     */

    public boolean accessible(Coord c, Function<Coord, Double> norm, int radius) {
        return c.equals(center) || accessibleDirection(c, norm, radius) != null;
    }

    /**
     * Analogically to the previous, specifying when norm is L-infinity
     */
    public boolean accessible(Coord c) {
        return c.equals(center) || accessibleDirection(c) != null;
    }

    /**
     * Analogically to the self-titled, specifying when norm is L-infinity
     */
    public Direction accessibleDirection(Coord c) {
        return accessibleDirection(c, Coord::lInftyNorm, radius);
    }

    /**
     * If map configuration around the neighbourhood center is changed, this method is called
     */
    @Override
    public void update() {
        relevant = false;
    }
}
