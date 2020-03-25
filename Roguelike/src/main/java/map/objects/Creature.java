package map.objects;

import basicComponents.GameplayLogic;
import map.MapOfObjects;
import map.Waiter;

import static map.MapOfObjects.mapLock;

public abstract class Creature extends MovableObject implements DynamicObject, AttackingObject, DamageableObject {
    protected int health;

    public void die() {
        this.delete();
    }
}
