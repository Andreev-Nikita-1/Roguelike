package hero.stats;

/**
 * Performs, when hero takes damage. Decrease durability of shield and deletes it, if durability runs out
 */
public class DamageVisitor extends StatsVisitor {
    int damage;

    public DamageVisitor(int damage) {
        this.damage = damage;
    }

    @Override
    public void visit(HeroStats stats) {
        stats.health.addAndGet((int) (-damage * (0.5 + 0.5 / (1 + stats.getProtection() / 50.0))));
        if (stats.ownerInventory.shield == null) return;
        stats.ownerInventory.shield.durability.addAndGet(-damage / 5);
        if (stats.ownerInventory.shield.durability.get() == 0) {
            stats.ownerInventory.shield.delete();
        }
    }
}
