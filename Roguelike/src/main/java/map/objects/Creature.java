package map.objects;

public abstract class Creature extends MovableObject implements DynamicObject, AttackingObject, DamageableObject {
    protected int health;

    public void die() {
        this.delete();
    }
}
