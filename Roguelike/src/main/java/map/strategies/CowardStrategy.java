package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.Coord;
import util.Direction;


/**
 * Strategy, that dictates to run from hero, when his AccessNeighbourhood allows to get him
 */
public class CowardStrategy extends Strategy {
    CowardStrategy(Mob owner) {
        super(owner);
    }

    /**
     * Returns direction, provided by heroAccessNeighbourhood, if it is not null, NOTHING otherwise
     */
    @Override
    public GameplayOption getAction() {

        if (!owner.map.heroAccessNeighbourhood.accessible(owner.getLocation(), Coord::euqlidean, 5)) {
            return GameplayOption.NOTHING;
        } else {
            Direction direction = owner.getLocation().properDirection(owner.map.getHeroLocation()).opposite();
            return new DirectedOption(DirectedOption.Action.RUN, direction);
        }
    }
}
