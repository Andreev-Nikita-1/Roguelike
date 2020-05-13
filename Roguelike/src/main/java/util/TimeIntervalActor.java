package util;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public interface TimeIntervalActor extends Runnable, Pausable {


    int act();

    void setPaused(boolean paused);

    boolean getPaused();

    ScheduledExecutorService getScheduler();

    @Override
    default void run() {
        if (!getPaused()) {
            int delay = act();
            getScheduler().schedule(this, delay, MILLISECONDS);
        }
    }

    @Override
    default TimeIntervalActor start() {
        setPaused(false);
        getScheduler().schedule(this, 0, MILLISECONDS);
        return this;
    }

    @Override
    default void pause() {
        setPaused(true);
    }

    @Override
    default void unpause() {
        setPaused(false);
        getScheduler().schedule(this, 0, MILLISECONDS);
    }

    @Override
    default void kill() {
        setPaused(true);
    }
}

