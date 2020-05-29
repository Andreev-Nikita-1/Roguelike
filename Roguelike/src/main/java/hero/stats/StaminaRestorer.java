package hero.stats;

import basicComponents.Game;
import util.TimeIntervalActor;

import java.util.concurrent.atomic.AtomicBoolean;

public class StaminaRestorer implements TimeIntervalActor {
    private AtomicBoolean active = new AtomicBoolean(true);
    HeroStats stats;

    public StaminaRestorer(HeroStats stats) {
        this.stats = stats;
    }

    @Override
    public int act() {
        if (stats.stamina.get() < stats.maxStamina) {
            stats.stamina.addAndGet(Math.min(1, stats.maxStamina - stats.stamina.get()));
        }
        return 300;
    }

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public Game getGame() {
        return stats.ownerInventory.heroMap.game;
    }
}
