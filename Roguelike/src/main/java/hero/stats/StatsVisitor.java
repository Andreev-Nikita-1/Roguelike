package hero.stats;

/**
 * Visitor, that can modify stats
 */
public abstract class StatsVisitor {
    public abstract void visit(HeroStats stats);
}
