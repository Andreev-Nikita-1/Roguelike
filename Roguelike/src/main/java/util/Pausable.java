package util;

public interface Pausable {

    Pausable start();

    void pause();

    void unpause();

    void kill() throws InterruptedException;
}
