package map.objects;

public interface DynamicObject {

    DynamicObject start();

    void pause();

    void unpause();

    void kill() throws InterruptedException;
}
