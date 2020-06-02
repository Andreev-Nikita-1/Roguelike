package hero.stats;

/**
 * Performs, when hero attacks someone. Decrease durability of weapon and deletes it, if durability runs out
 */
public class AttackVisitor extends StatsVisitor {
    @Override
    public void visit(HeroStats stats) {
        if (stats.ownerInventory.weapon == null) return;
        stats.ownerInventory.weapon.durability.addAndGet(-1);
        if (stats.ownerInventory.weapon.durability.get() == 0) {
            stats.ownerInventory.weapon.delete();
        }
    }
}
