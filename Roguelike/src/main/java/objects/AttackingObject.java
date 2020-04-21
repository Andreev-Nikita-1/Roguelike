package objects;

import util.Direction;

public interface AttackingObject {
    int attackVisualizationPeriod = 70;

    void attack(Direction direction);
}
