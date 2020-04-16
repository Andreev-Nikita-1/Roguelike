package util;

import map.objects.MovableObject;

import static util.Direction.*;

public class Mover {
    private int speedDelay;
    public Waiter waiterX;
    public Waiter waiterY;


    public Mover(int speedDelay) {
        this.speedDelay = speedDelay;
        waiterX = new Waiter(speedDelay);
        waiterY = new Waiter((int) ((double) speedDelay * 4 / 3));
    }

    public Mover start() {
        waiterX.start();
        waiterY.start();
        return this;
    }

    public void changeSpeed(int speedDelay) {
        waiterX.changeDelay(speedDelay);
        waiterY.changeDelay((int) ((double) speedDelay * 4 / 3));
    }

    public void pause() {
        waiterX.pause();
        waiterY.pause();
    }

    public void unpause() {
        waiterX.unpause();
        waiterY.unpause();
    }

    public void kill() throws InterruptedException {
        waiterX.kill();
        waiterY.kill();
    }
}
