package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public interface TimeIntervalActor extends Runnable, Pausable {

    int act();

    void setActive(boolean active);

    boolean isActive();

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    @Override
    default void run() {
        if (isActive() && !paused.get()) {
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

