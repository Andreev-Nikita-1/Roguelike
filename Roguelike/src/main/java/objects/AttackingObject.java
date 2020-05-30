package objects;

import util.Direction;

/**
 * for those, who can attack
 */
public interface AttackingObject {
    int attackVisualizationPeriod = 70;

    void attack(Direction direction);
}
