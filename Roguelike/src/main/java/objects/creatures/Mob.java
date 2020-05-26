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
    public void setActive(boolean active) {
        this.paused.set(active);
    }

    @Override
    public boolean isActive() {
        return paused.get();
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
    }

    public abstract void die();

    public Mob(MapOfObjects map, Coord location) {
        super(map, location);
    }


}
