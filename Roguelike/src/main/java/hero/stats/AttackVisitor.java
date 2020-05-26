package hero.stats;

public class AttackVisitor extends StatsVisitor {
    @Override
    public void visit(HeroStats stats) {
        if (stats.owner.weapon == null) return;
        stats.owner.weapon.durability.addAndGet(-1);
        if (stats.owner.weapon.durability.get() == 0) {
            stats.owner.weapon.delete();
        }
    }
}
