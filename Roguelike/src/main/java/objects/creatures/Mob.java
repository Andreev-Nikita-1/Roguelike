package objects.creatures;

import map.MapOfObjects;
import map.strategies.Strategy;
import util.Coord;
import util.TimeIntervalActor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Mob extends OnePixelCreature implements TimeIntervalActor {

    public AtomicInteger health;
    public volatile int power;
    public Strategy strategy;
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
        return map.scheduler;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        kill();
    }

    public abstract void die();

    public Mob(MapOfObjects map, Coord location) {
        super(map, location);
    }


}
