package util;

import map.objects.DynamicObject;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadState implements DynamicObject, Runnable {
    private volatile boolean active = false;
    private volatile boolean dead = false;
    private Thread thread;
    private Actor actor;
    private Lock lock = new ReentrantLock();
    private Condition activeCond;

    public ThreadState(Actor actor) {
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
                actor.act();
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public ThreadState start() {
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
