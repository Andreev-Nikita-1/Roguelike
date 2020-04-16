package util;

import map.objects.DynamicObject;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Waiter implements DynamicObject, Actor {

    private volatile int delay;
    private volatile boolean ready = true;
    private Lock lock = new ReentrantLock();
    private Condition notReadyCond;
    private Condition readyCond;
    private ThreadState threadState;

    public Waiter(int delay) {
        this.delay = delay;
        threadState = new ThreadState(this);
        notReadyCond = lock.newCondition();
        readyCond = lock.newCondition();
    }

    public synchronized void changeDelay(int delay) {
        this.delay = delay;
    }

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
        changeDelay(delay);
        reset();
    }

    @Override
    public Waiter start() {
        threadState.start();
        return this;
    }

    @Override
    public void pause() {
        threadState.pause();
    }

    @Override
    public void unpause() {
        threadState.unpause();
    }

    @Override
    public void kill() throws InterruptedException {
        threadState.kill();
    }

    @Override
    public void act() throws InterruptedException {
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
        } finally {
            lock.unlock();
        }
    }

}
