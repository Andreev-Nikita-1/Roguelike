package util;

import basicComponents.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This interface represents all objects, which can be launched when game starts, or paused when game is paused
 */
public interface Pausable {

    void setGame(Game game);


    /**
     * introduces this to the game given
     */
    default void includeToGame(Game game) {
        setGame(game);
        game.addPausable(this);
        if (!game.isPaused()) {
            start();
        }
    }

    /**
     * deletes this from the game
     */
    default void deleteFromGame(Game game) {
        game.removePausable(this);
        kill();
    }


    /**
     * Methods for managing launching object
     */

    void pause();

    void unpause();

    Pausable start();

    void kill();
}
