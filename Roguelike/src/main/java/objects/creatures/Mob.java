package objects.creatures;

import map.MapOfObjects;
import map.strategies.Strategy;
import objects.SnapshotableFromMap;
import util.Coord;
import util.TimeIntervalActor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Mob extends OnePixelCreature implements TimeIntervalActor, SnapshotableFromMap {

    public AtomicInteger health;
    protected int maxHealth;
    public volatile int power;
    protected int speedDelay;
    protected int attackDelay;

    public Strategy strategy;
    private AtomicBoolean active = new AtomicBoolean(true);

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    public abstract void die();

    public Mob(Coord location) {
        super(location);
    }
}
