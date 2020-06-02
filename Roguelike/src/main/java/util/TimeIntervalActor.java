package util;

import basicComponents.Game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * This interface is implemented by most of dynamic objects in game.
 * Has the method "act", which performs some actions and returns time delay, that must pass before it is called again
 */
public interface TimeIntervalActor extends Runnable, Pausable {

    int act();

    /**
     * When active, object will be paused or unpaused depending on the game state
     */
    void setActive(boolean active);

    boolean isActive();

    /**
     * Those, who implements this interface, have to have game, on which it depends
     */
    Game getGame();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    @Override
    default void run() {
        if (isActive() && !getGame().isPaused()) {
            int delay = act();
            scheduler.schedule(this, delay, MILLISECONDS);
        }
    }

    @Override
    default TimeIntervalActor start() {
        setActive(true);
        scheduler.schedule(this, 0, MILLISECONDS);
        return this;
    }

    @Override
    default void pause() {
    }

    @Override
    default void unpause() {
        scheduler.schedule(this, 0, MILLISECONDS);
    }

    @Override
    default void kill() {
        setActive(false);
    }
}

