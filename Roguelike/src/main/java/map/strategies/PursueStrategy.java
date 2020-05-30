package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.Direction;

/**
 * Strategy, that dictates to pursue hero, when his AccessNeighbourhood allows to get him
 */
public class PursueStrategy extends Strategy {
    PursueStrategy(Mob owner) {
        super(owner);
    }

    /**
     * Returns direction, provided by heroAccessNeighbourhood, if it is not null, NOTHING otherwise
     */
    @Override
    public GameplayOption getAction() {
        Direction direction = owner.map.heroAccessNeighbourhood.accessibleDirection(owner.getLocation());
        if (direction == null) {
            return GameplayOption.NOTHING;
        }
        if (owner.map.getHeroLocation().near(owner.getLocation())) {
            return new DirectedOption(DirectedOption.Action.ATTACK, direction);
        } else {
            return new DirectedOption(DirectedOption.Action.RUN, direction);
        }
    }
}
