package map.objects;

import map.MapOfObjects;
import map.shapes.Shape;
import util.Coord;

public abstract class Creature extends MovableObject implements DynamicObject, AttackingObject, DamageableObject {
    protected volatile int health;

    public Creature(MapOfObjects map, Coord coord, Shape shape) {
        super(map, coord, shape);
    }

    public abstract void die() throws InterruptedException;
}
