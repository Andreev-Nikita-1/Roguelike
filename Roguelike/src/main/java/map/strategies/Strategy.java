package map.strategies;

import gameplayOptions.GameplayOption;
import map.MapOfObjects;
import objects.creatures.Mob;

public abstract class Strategy {
    protected Mob owner;

    public Strategy(Mob owner) {
        this.owner = owner;
    }

    public abstract GameplayOption getAction();
}
