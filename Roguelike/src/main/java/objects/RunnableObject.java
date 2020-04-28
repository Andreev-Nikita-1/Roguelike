package objects;

import map.MapOfObjects;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class RunnableObject extends PausableObject implements Runnable {
    private volatile ScheduledFuture<?> future;
    private volatile int delay = 10000000;
    private AtomicBoolean paused = new AtomicBoolean(true);

    public RunnableObject(MapOfObjects map) {
        super(map);
    }

    public abstract int act();

    @Override
    public void run() {
        int newDelay = act();
        if (newDelay == 0) {
            while (newDelay == 0) {
                newDelay = act();
            }
        }
        if (future.getDelay(MILLISECONDS) != newDelay) {
            delay = newDelay;
            future.cancel(true);
            if (!paused.get()) {
                future = map.scheduler.scheduleAtFixedRate(this, delay, delay, MILLISECONDS);
            }
        }
    }

    @Override
    public RunnableObject start() {
        paused.set(false);
        future = map.scheduler.scheduleAtFixedRate(this, 0, delay, MILLISECONDS);
        return this;
    }

    @Override
    public void pause() {
        paused.set(true);
        future.cancel(true);
    }

    @Override
    public void unpause() {
        paused.set(false);
        future = map.scheduler.scheduleWithFixedDelay(this, delay, delay, MILLISECONDS);
    }

    @Override
    public void kill() {
        paused.set(true);
        future.cancel(true);
    }
}
