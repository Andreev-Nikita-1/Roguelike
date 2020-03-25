package map;

public class Waiter implements Runnable {

    public boolean ready = true;
    private int delay;

    public Waiter(int delay) {
        this.delay = delay;
    }

    public synchronized void changeDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (!ready) {
                try {
                    wait(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ready = true;
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}