package hero.stats;

public class RunVisitor extends StatsVisitor {
    @Override
    public void visit(HeroStats stats) {
        stats.stamina.addAndGet(-1);
    }
}
