package map.strategies;

import gameplayOptions.GameplayOption;
import objects.creatures.Mob;

/**
 * Class, representing strategy for mob behaviour
 */
public abstract class Strategy {
    Mob owner;

    Strategy(Mob owner) {
        this.owner = owner;
    }

    public abstract GameplayOption getAction();
}
