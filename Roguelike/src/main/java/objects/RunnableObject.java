package objects;

import map.MapOfObjects;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class RunnableObject extends PausableObject implements Runnable {
    private AtomicBoolean paused = new AtomicBoolean(true);

    public RunnableObject(MapOfObjects map) {
        super(map);
    }

    public abstract int act();

    @Override
    public void run() {
        int delay = act();
        if (!paused.get()) {
            map.scheduler.schedule(this, delay, MILLISECONDS);
        }

    }

    @Override
    public RunnableObject start() {
        paused.set(false);
        map.scheduler.schedule(this, 0, MILLISECONDS);
        return this;
    }

    @Override
    public void pause() {
        paused.set(true);
    }

    @Override
    public void unpause() {
        paused.set(false);
        map.scheduler.schedule(this, 0, MILLISECONDS);
    }

    @Override
    public void kill() {
        paused.set(true);
    }
}
