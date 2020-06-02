package objects;

/**
 * Has method update. Is implemented by dynamic objects, which depends on other objects behavior,
 * e.g. item taking by hero or door highlighting when hero is near
 */
public interface DependingObject {
    void update();
}
