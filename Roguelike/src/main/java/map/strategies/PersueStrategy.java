package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.MapObject;
import objects.creatures.Creature;
import util.Direction;

public class PersueStrategy extends Strategy {
    public PersueStrategy(Creature owner) {
        super(owner);
    }

    @Override
    public GameplayOption getAction() {
        Direction direction = map.heroAccessNeighbourhood.accessibleDirection(owner.getLocation());
        if (direction == null) {
            return GameplayOption.NOTHING;
        }
        if (map.getHeroLocation().near(owner.getLocation())) {
            return new DirectedOption(DirectedOption.Action.ATTACK, direction);
        } else {
            return new DirectedOption(DirectedOption.Action.RUN, direction);
        }
    }
}
