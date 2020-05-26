package util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public interface Pausable {

    AtomicBoolean paused = new AtomicBoolean(true);
    List<Pausable> pausables = new CopyOnWriteArrayList<>();

    static void pauseGame() {
        paused.set(true);
        for (Pausable pausable : pausables) {
            pausable.pause();
        }
    }

    static void unpauseGame() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.unpause();
        }
    }

    static void startGame() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.start();
        }
    }

    static void killGame() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.kill();
        }
    }

    default void includeToGame() {
        pausables.add(this);
        if (!paused.get()) {
            start();
        }
    }

    default void deleteFromGame() {
        pausables.remove(this);
        kill();
    }

    void pause();

    void unpause();

    Pausable start();

    void kill();
}
