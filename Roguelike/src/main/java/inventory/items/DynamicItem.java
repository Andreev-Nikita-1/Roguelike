package inventory.items;

import util.TimeIntervalActor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DynamicItem extends Item implements TimeIntervalActor {
    private AtomicBoolean paused = new AtomicBoolean(true);

    @Override
    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }

    @Override
    public boolean getPaused() {
        return paused.get();
    }

    @Override
    public ScheduledExecutorService getScheduler() {
        return owner.scheduler;
    }
}
