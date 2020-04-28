package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Creature;
import objects.neighbourfoods.AccessNeighbourhood;
import util.Coord;
import util.Direction;

import static gameplayOptions.DirectedOption.Action.*;

public abstract class LocalTargetSwitchingStrategy extends Strategy {
    protected Coord currentLocation = owner.getLocation();
    public Coord target;
    protected AccessNeighbourhood neighbourhood;

    public LocalTargetSwitchingStrategy(Creature owner, Coord initialTarget) {
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
            if (neighbourhood != null) {
                neighbourhood.deleteFromMap();
            }
            neighbourhood = null;
        }

        Direction direction = currentLocation.properDirection(target);
        if (!map.accesible(currentLocation.shifted(Coord.fromDirection(direction)))) {
            if (neighbourhood == null) {
                neighbourhood = new AccessNeighbourhood(map, target, 15, Coord::lInftyNorm).attachToMap();
            }
            direction = neighbourhood.follow(currentLocation);
        }
        if (direction == null || !map.accesible(currentLocation.shifted(Coord.fromDirection(direction)))) {
            return GameplayOption.NOTHING;
        } else {
            return new DirectedOption(WALK, direction);
        }
    }
}
