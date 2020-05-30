package gameplayOptions;

import util.Direction;

import static util.Direction.*;
import static gameplayOptions.DirectedOption.Action.*;

/**
 * Directed option, e.g. walking or attacking
 */
public class DirectedOption extends GameplayOption {
    public Direction direction;
    public Action action;

    public static final DirectedOption WALK_LEFT = new DirectedOption(WALK, LEFT);
    public static final DirectedOption WALK_RIGHT = new DirectedOption(WALK, RIGHT);
    public static final DirectedOption WALK_UP = new DirectedOption(WALK, UP);
    public static final DirectedOption WALK_DOWN = new DirectedOption(WALK, DOWN);
    public static final DirectedOption RUN_LEFT = new DirectedOption(RUN, LEFT);
    public static final DirectedOption RUN_RIGHT = new DirectedOption(RUN, RIGHT);
    public static final DirectedOption RUN_UP = new DirectedOption(RUN, UP);
    public static final DirectedOption RUN_DOWN = new DirectedOption(RUN, DOWN);
    public static final DirectedOption ATTACK_LEFT = new DirectedOption(ATTACK, LEFT);
    public static final DirectedOption ATTACK_RIGHT = new DirectedOption(ATTACK, RIGHT);
    public static final DirectedOption ATTACK_UP = new DirectedOption(ATTACK, UP);
    public static final DirectedOption ATTACK_DOWN = new DirectedOption(ATTACK, DOWN);

    public DirectedOption(Action action, Direction direction) {
        this.action = action;
        this.direction = direction;
    }

    public enum Action {
        WALK, RUN, ATTACK
    }
}
