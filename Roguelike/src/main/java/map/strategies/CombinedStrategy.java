package map.strategies;

import gameplayOptions.GameplayOption;
import objects.creatures.Creature;

public class CombinedStrategy extends Strategy {
    private Strategy currentStrategy;

    public CombinedStrategy(Creature owner) {
        super(owner);
    }

    private int nothingCounter = 0;

    @Override
    public GameplayOption getAction() {
        if (currentStrategy == null) {
            switchStrategy();
        }
        if (map.heroAccessNeighbourhood.accessible(owner.getLocation())) {
            if (!(currentStrategy instanceof PersueStrategy)) {
                currentStrategy = new PersueStrategy(owner);
            }
        }
        GameplayOption option = currentStrategy.getAction();
        if (option == GameplayOption.NOTHING) {
            nothingCounter++;
        }
        if (nothingCounter == 5 || Math.random() < 0.1) {
            nothingCounter = 0;
            if (map.closestRoom(owner.getLocation()) == null) {
                return GameplayOption.NOTHING;
            }
            switchStrategy();
        }
        return option;
    }

    private void switchStrategy() {
        if (currentStrategy instanceof PersueStrategy || currentStrategy == null) {
            currentStrategy = new RoomRandomTravelingStrategy(owner);
        } else {
            if (Math.random() < 0.2) {
                currentStrategy = new RoomPatrolStrategy(owner);
            } else {
                currentStrategy = new RoomRandomTravelingStrategy(owner);
            }
        }
    }
}
