package util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PauseController implements Pausable, Runnable {
    private volatile boolean active = false;
    private volatile boolean dead = false;
    private Thread thread;
    private Runnable actor;
    private Lock lock = new ReentrantLock();
    private Condition activeCond;

    public PauseController(Runnable actor) {
        this.actor = actor;
        thread = new Thread(this);
        activeCond = lock.newCondition();
    }

    @Override
    public void run() {
        while (!dead) {
            lock.lock();
            try {
                while (!active) {
                    activeCond.await();
                }
                actor.run();
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }
    }

    public boolean isActive(){
        return active;
    }

    @Override
    public PauseController start() {
        active = true;
        thread.start();
        return this;
    }

    @Override
    public void pause() {
        active = false;
        thread.interrupt();
    }

    @Override
    public void unpause() {
        lock.lock();
        active = true;
        activeCond.signal();
        lock.unlock();
    }

    @Override
    public void kill() throws InterruptedException {
        dead = true;
        thread.interrupt();
        thread.join();
    }
}
