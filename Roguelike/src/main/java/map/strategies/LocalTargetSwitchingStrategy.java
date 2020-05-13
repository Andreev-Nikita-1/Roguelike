package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.AccessNeighbourhood;
import util.Coord;
import util.Direction;

import static gameplayOptions.DirectedOption.Action.*;

public abstract class LocalTargetSwitchingStrategy extends Strategy {
    protected Coord currentLocation = owner.getLocation();
    protected Coord target;

    public LocalTargetSwitchingStrategy(Mob owner, Coord initialTarget) {
        super(owner);
        target = initialTarget;
    }

    public abstract void chooseNextTarget();

    @Override
    public GameplayOption getAction() {
        if (currentLocation.equals(target)) {
            chooseNextTarget();
            if (currentLocation.equals(target)) {
                return GameplayOption.NOTHING;
            }
        }

        Direction direction = currentLocation.properDirection(target);
        if (!map.accesible(currentLocation.shifted(direction))) {
            AccessNeighbourhood neighbourhood = new AccessNeighbourhood(map, target, 15);
            direction = neighbourhood.accessibleDirection(currentLocation);
            neighbourhood.delete();
        }

        if (direction == null || !map.accesible(currentLocation.shifted(direction))) {
            return GameplayOption.NOTHING;
        } else {
            return new DirectedOption(WALK, direction);
        }
    }
}
