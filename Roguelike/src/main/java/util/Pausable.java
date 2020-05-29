package util;

import basicComponents.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public interface Pausable {
    default void includeToGame(Game game) {
        game.pausables.add(this);
        if (!game.paused.get()) {
            start();
        }
    }

    default void deleteFromGame(Game game) {
        game.pausables.remove(this);
        kill();
    }

    void pause();

    void unpause();

    Pausable start();

    void kill();
}
