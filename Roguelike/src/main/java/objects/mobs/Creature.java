package objects.mobs;

import map.MapOfObjects;
import objects.*;
import util.Coord;

public abstract class Creature extends RunnableObject implements VisualObject, MovableObject, AttackingObject, DamageableObject {

    protected Coord location;

    public Creature(MapOfObjects map, Coord location) {
        super(map);
        this.location = location;
    }

    @Override
    public Creature attachToMap() {
        super.attachToMap();
        map.dynamicObjects.add(this);
        return this;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        map.dynamicObjects.remove(this);
    }
}
