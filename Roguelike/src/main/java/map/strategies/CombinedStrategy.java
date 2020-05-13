package map.strategies;

import gameplayOptions.GameplayOption;
import objects.creatures.Creature;
import util.Coord;

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
        try {
            for (int i = 0; i < map.heroObjects.length; i++) {
                if (map.heroObjects[i] == null) continue;
                if (map.heroAccessNeighbourhoods[i].accessible(owner.getLocation(), Coord::euqlideanScaled, 5)) {
                    if (!(currentStrategy instanceof PersueStrategy)) {
                        currentStrategy = new PersueStrategy(owner, i);
                    }
                }
            }
        } catch (NullPointerException e) {
        }
        GameplayOption option = currentStrategy.getAction();
        if (option == GameplayOption.NOTHING) {
            nothingCounter++;
        }
        if (nothingCounter == 5
                || (currentStrategy instanceof RoomPatrolStrategy && Math.random() < 0.1)) {
            nothingCounter = 0;
            if (map.closestRoom(owner.getLocation()) == null) {
                return GameplayOption.NOTHING;
            }
            switchStrategy();
        }
        return option;
    }

    private void switchStrategy() {
        if (currentStrategy instanceof PersueStrategy) {
            currentStrategy = new RoomRandomTravelingStrategy(owner);
        } else {
            if (Math.random() < 0.1) {
                currentStrategy = new RoomPatrolStrategy(owner);
            } else {
                currentStrategy = new RoomRandomTravelingStrategy(owner);
            }
        }
    }
}
