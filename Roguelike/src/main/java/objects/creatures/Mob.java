package objects.creatures;

import basicComponents.Game;
import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import map.strategies.CombinedStrategy;
import map.strategies.ConfusedStrategy;
import map.strategies.Strategy;
import objects.SnapshotableFromMap;
import objects.stuff.Stuff;
import util.Coord;
import util.TimeIntervalActor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;


/**
 * Simple mob
 */
public abstract class Mob extends OnePixelCreature implements TimeIntervalActor, SnapshotableFromMap {

    public AtomicInteger health;
    int maxHealth;
    volatile int power;
    int speedDelay;
    int attackDelay;

    Strategy strategy;
    private AtomicBoolean active = new AtomicBoolean(true);

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    /**
     * Acting, according to strategy
     */
    @Override
    public int act() {
        GameplayOption action = strategy.getAction();
        if (action instanceof DirectedOption) {
            switch (((DirectedOption) action).action) {
                case WALK:
                case RUN:
                    if (move(((DirectedOption) action).direction)) {
                        int d = (((DirectedOption) action).action == DirectedOption.Action.WALK) ? 1 : 2;
                        return speedDelay / d;
                    } else {
                        return 10;
                    }
                case ATTACK:
                    attack(((DirectedOption) action).direction);
                    return attackDelay;
            }
        }
        return 10;
    }

    /**
     * Die method
     */
    public synchronized void die() {
        Lock lock = map.getCoordLock(location);
        lock.lock();
        try {
            deleteFromMap();
            generateItem().attachToMap(map);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Taking damage method
     */
    @Override
    public void takeDamage(int damage) {
        health.addAndGet(-damage);
        if (Math.random() < 0.01 + 0.00001 * damage) {
            ((CombinedStrategy) strategy).setCurrentStrategy(
                    new ConfusedStrategy(this, 10));
        }
        if (health.get() <= 0) {
            die();
        }
    }


    public Mob setHealth(int health) {
        this.health.set(health);
        return this;
    }

    private Game game;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }


    /**
     * Stuff, that appears when mob is slayed
     */
    protected abstract Stuff generateItem();

    public Mob(Coord location) {
        super(location);
    }
}
