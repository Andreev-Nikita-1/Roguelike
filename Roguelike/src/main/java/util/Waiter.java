package util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Waiter implements Pausable, Runnable {

    private volatile int delay;
    private volatile boolean ready = true;
    private Lock lock = new ReentrantLock();
    private Condition notReadyCond;
    private Condition readyCond;
    private PauseController pauseController;

    public Waiter(int delay) {
        this.delay = delay;
        pauseController = new PauseController(this);
        notReadyCond = lock.newCondition();
        readyCond = lock.newCondition();
    }

    public synchronized void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isActive(){
        return pauseController.isActive();
    };

    public boolean isReady() {
        return ready;
    }

    public void waitUntilReady() throws InterruptedException {
        lock.lock();
        try {
            while (!ready) {
                readyCond.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void reset() {
        ready = false;
        lock.lock();
        notReadyCond.signal();
        lock.unlock();
    }

    public void reset(int delay) {
        setDelay(delay);
        reset();
    }

    @Override
    public Waiter start() {
        pauseController.start();
        return this;
    }

    @Override
    public void pause() {
        pauseController.pause();
    }

    @Override
    public void unpause() {
        pauseController.unpause();
    }

    @Override
    public void kill() throws InterruptedException {
        pauseController.kill();
    }

    @Override
    public void run() {
        lock.lock();
        try {
            if (!ready) {
                sleep(delay);
            }
            ready = true;
            readyCond.signal();
            while (ready) {
                notReadyCond.await();
            }
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

}
