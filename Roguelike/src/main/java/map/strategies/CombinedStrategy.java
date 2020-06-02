package map.strategies;

import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.Coord;

import java.util.function.Function;

/**
 * Strategy, that combines several strategies, depending on hero accessibility and confused state
 */
public class CombinedStrategy extends Strategy {
    private Strategy currentStrategy;
    private Function<Mob, Strategy> heroDependingStrategy;

    public CombinedStrategy(Mob owner, HeroDependingStrategyType type) {
        super(owner);
        switch (type) {
            case AGRESSIVE:
                heroDependingStrategy = PursueStrategy::new;
                break;
            case COWARD:
                heroDependingStrategy = CowardStrategy::new;
                break;
        }
    }

    public enum HeroDependingStrategyType {
        AGRESSIVE, COWARD
    }

    public void setCurrentStrategy(Strategy strategy) {
        currentStrategy = strategy;

    }


    private int nothingCounter = 0;

    @Override
    public GameplayOption getAction() {
        if (currentStrategy == null) {
            switchStrategy();
        }
        if (owner.map.heroAccessNeighbourhood.accessible(owner.getLocation(), Coord::euqlidean, 5)
                && !(currentStrategy instanceof ConfusedStrategy)) {
            currentStrategy = heroDependingStrategy.apply(owner);
        }
        GameplayOption option = currentStrategy.getAction();
        if (option == GameplayOption.NOTHING) {
            nothingCounter++;
        }
        if (nothingCounter == 5
                || (currentStrategy instanceof RoomPatrolStrategy && Math.random() < 0.1)) {
            nothingCounter = 0;
            if (owner.map.closestRoom(owner.getLocation()) == null) {
                return GameplayOption.NOTHING;
            }
            switchStrategy();
        }
        return option;
    }

    private void switchStrategy() {
        if (Math.random() < 0.1) {
            currentStrategy = new RoomPatrolStrategy(owner);
        } else {
            currentStrategy = new RoomRandomTravelingStrategy(owner);
        }
    }
}
