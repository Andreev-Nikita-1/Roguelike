package hero.stats;

public class HealthVisitor extends StatsVisitor {
    int toAdd;

    public HealthVisitor(int toAdd) {
        this.toAdd = toAdd;
    }

    @Override
    public void visit(HeroStats stats) {
        if (stats.getMaxHealth() < stats.health.get() + toAdd) {
            stats.health.set(stats.getMaxHealth());
        } else {
            stats.health.addAndGet(toAdd);
        }
    }
}
