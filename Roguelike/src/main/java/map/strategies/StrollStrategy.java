package map.strategies;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import objects.creatures.Creature;
import util.Direction;
import util.Util;

public class StrollStrategy extends Strategy {
    @Override
    public GameplayOption getAction(Creature object) {
        if (object.map.heroAccessNeighbourhood.accessible(object.getLocation())) {
            object.strategy = new PersueStrategy();
            return GameplayOption.NOTHING;
        }
        Direction direction = Util.generate(Direction.values());
        double t = Math.random();
        if(t<0.25){
            direction = Direction.LEFT;
        }else if (t<0.5){
            direction = Direction.RIGHT;
        }else if (t< 0.75){
            direction = Direction.UP;
        }else{
            direction = Direction.DOWN;
        }
        return new DirectedOption(DirectedOption.Action.WALK, direction);
    }
}
