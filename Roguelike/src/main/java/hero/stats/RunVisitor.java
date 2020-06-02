package hero.stats;

/**
 * Performs, when hero runs. Decrease stamina
 */
public class RunVisitor extends StatsVisitor {
    @Override
    public void visit(HeroStats stats) {
        stats.stamina.addAndGet(-1);
    }
}
