package map.strategies;

import gameplayOptions.GameplayOption;
import objects.MapObject;
import objects.creatures.Creature;

public abstract class Strategy {
    public abstract GameplayOption getAction(Creature object);
}
