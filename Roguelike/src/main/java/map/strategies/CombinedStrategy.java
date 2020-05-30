package map.strategies;

import gameplayOptions.GameplayOption;
import objects.creatures.Mob;
import util.Coord;

public class CombinedStrategy extends Strategy {
    private Strategy currentStrategy;

    public CombinedStrategy(Mob owner) {
        super(owner);
    }

    private int nothingCounter = 0;

    @Override
    public GameplayOption getAction() {
        if (currentStrategy == null) {
            switchStrategy();
        }
        if (owner.map.heroAccessNeighbourhood.accessible(owner.getLocation(), Coord::euqlidean, 5)) {
            if (!(currentStrategy instanceof PursueStrategy)) {
                currentStrategy = new PursueStrategy(owner);
            }
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
        if (currentStrategy instanceof PursueStrategy || currentStrategy == null) {
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
