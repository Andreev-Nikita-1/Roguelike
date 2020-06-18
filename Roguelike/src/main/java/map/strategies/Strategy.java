package map.strategies;

import gameplayOptions.GameplayOption;
import map.MapOfObjects;
import objects.creatures.Creature;

public abstract class Strategy {
    protected Creature owner;
    protected MapOfObjects map;

    public Strategy(Creature owner) {
        this.owner = owner;
        map = owner.map;
    }

    public abstract GameplayOption getAction();
}
