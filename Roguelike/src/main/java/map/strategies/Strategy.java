package map.strategies;

import gameplayOptions.GameplayOption;
import map.MapOfObjects;
import objects.creatures.Mob;

public abstract class Strategy {
    protected Mob owner;
    protected MapOfObjects map;

    public Strategy(Mob owner) {
        this.owner = owner;
        map = owner.map;
    }

    public abstract GameplayOption getAction();
}
