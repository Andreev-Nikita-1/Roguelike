package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.MapObject;
import objects.creatures.Creature;
import util.Direction;

public class PersueStrategy extends Strategy {
    @Override
    public GameplayOption getAction(Creature object) {
        Direction direction = object.map.heroAccessNeighbourhood.follow(object.getLocation());
        if (direction == null) {
            if (!object.map.heroAccessNeighbourhood.accessible(object.getLocation())) {
                object.strategy = new StrollStrategy();
                return GameplayOption.NOTHING;
            }
        }
        if (object.map.getHeroLocation().near(object.getLocation())) {
            return new DirectedOption(DirectedOption.Action.ATTACK, direction);
        } else {
            return new DirectedOption(DirectedOption.Action.WALK, direction);
        }
    }
}
