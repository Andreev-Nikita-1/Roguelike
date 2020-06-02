package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.Direction;
import util.Util;


/**
 * Strategy, that dictates to move in random direction
 */
public class ConfusedStrategy extends Strategy {
    private int maxSteps;
    private int counter = 0;

    public ConfusedStrategy(Mob owner, int maxSteps) {
        super(owner);
        this.maxSteps = maxSteps;
    }

    @Override
    public GameplayOption getAction() {
        if (++counter >= maxSteps) {
            return GameplayOption.NOTHING;
        }
        Direction direction = Util.generate(Direction.getDirections());
        return new DirectedOption(DirectedOption.Action.RUN, direction);
    }
}
