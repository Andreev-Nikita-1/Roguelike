package hero.stats;

import basicComponents.Game;
import util.TimeIntervalActor;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Launches at the beginning, and gradually restores stamina
 */
public class StaminaRestorer implements TimeIntervalActor {
    private AtomicBoolean active = new AtomicBoolean(true);
    private HeroStats stats;

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

    private Game game;

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }
}
