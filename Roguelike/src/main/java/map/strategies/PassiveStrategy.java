package map.strategies;

import gameplayOptions.GameplayOption;
import objects.creatures.Mob;

/**
 * Strategy, that dictates to do nothing
 */
public class PassiveStrategy extends Strategy {
    PassiveStrategy(Mob owner) {
        super(owner);
    }

    @Override
    public GameplayOption getAction() {
        return GameplayOption.NOTHING;
    }
}
