package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.AccessNeighbourhood;
import util.Coord;
import util.Direction;

import static gameplayOptions.DirectedOption.Action.*;

/**
 * Strategy, that leads mob to current target, and then compute next target, to do it again
 */
public abstract class LocalTargetSwitchingStrategy extends Strategy {
    Coord currentLocation = owner.getLocation();
    Coord target;

    LocalTargetSwitchingStrategy(Mob owner, Coord initialTarget) {
        super(owner);
        target = initialTarget;
    }

    /**
     * Returns new target, when previous was achieved
     */
    public abstract void chooseNextTarget();

    /**
     * Return direction to current target, if it is accessible, NOTHING otherwise
     */
    @Override
    public GameplayOption getAction() {
        if (currentLocation.equals(target)) {
            chooseNextTarget();
            if (currentLocation.equals(target)) {
                return GameplayOption.NOTHING;
            }
        }

        Direction direction = currentLocation.properDirection(target);
        if (!owner.map.accessible(currentLocation.shifted(direction))) {
            AccessNeighbourhood neighbourhood = new AccessNeighbourhood(owner.map, target, 15);
            direction = neighbourhood.accessibleDirection(currentLocation);
            neighbourhood.delete();
        }

        if (direction == null || !owner.map.accessible(currentLocation.shifted(direction))) {
            return GameplayOption.NOTHING;
        } else {
            return new DirectedOption(WALK, direction);
        }
    }
}
