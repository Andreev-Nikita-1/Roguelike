package objects;

import map.MapOfObjects;

import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class RunnableObject extends PausableObject implements Runnable {
    private volatile ScheduledFuture<?> future;
    private volatile int delay = 10000000;

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
            future = map.scheduler.scheduleAtFixedRate(this, delay, delay, MILLISECONDS);
        }
    }

    @Override
    public RunnableObject start() {
        future = map.scheduler.scheduleAtFixedRate(this, 0, delay, MILLISECONDS);
        return this;
    }

    @Override
    public void pause() {
        future.cancel(false);
    }

    @Override
    public void unpause() {
        future = map.scheduler.scheduleWithFixedDelay(this, delay, delay, MILLISECONDS);
    }

    @Override
    public void kill() {
        future.cancel(true);
    }
}
