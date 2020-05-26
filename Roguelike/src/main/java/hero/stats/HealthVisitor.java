package hero.stats;

public class HealthVisitor extends StatsVisitor {
    int toAdd;

    public HealthVisitor(int toAdd) {
        this.toAdd = toAdd;
    }

    @Override
    public void visit(HeroStats stats) {
        stats.health.addAndGet(toAdd);
    }
}
