package hero.stats;


public class DamageVisitor extends StatsVisitor {
    int damage;

    public DamageVisitor(int damage) {
        this.damage = damage;
    }

    @Override
    public void visit(HeroStats stats) {
        stats.health.addAndGet((int) (-damage * (0.5 + 0.5 / (1 + stats.getProtection() / 10.0))));
        if (stats.owner.shield == null) return;
        stats.owner.shield.durability.addAndGet(-damage / 5);
        if (stats.owner.shield.durability.get() == 0) {
            stats.owner.shield.delete();
        }
    }
}
