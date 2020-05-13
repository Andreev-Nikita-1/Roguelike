package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.MapObject;
import objects.creatures.Creature;
import util.Direction;

public class PersueStrategy extends Strategy {

    private int id;

    public PersueStrategy(Creature owner, int id) {
        super(owner);
        this.id = id;
    }

    @Override
    public GameplayOption getAction() {
        try {
            Direction direction = map.heroAccessNeighbourhoods[id].accessibleDirection(owner.getLocation());
            if (direction == null) {
                return GameplayOption.NOTHING;
            }
            if (map.getHeroLocation(id).near(owner.getLocation())) {
                return new DirectedOption(DirectedOption.Action.ATTACK, direction);
            } else {
                return new DirectedOption(DirectedOption.Action.RUN, direction);
            }
        } catch (NullPointerException e) {
            return GameplayOption.NOTHING;
        }
    }
}
