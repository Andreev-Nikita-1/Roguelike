package hero.stats;

public class ExperienceVisitor extends StatsVisitor {
    int toAdd;

    public ExperienceVisitor(int toAdd) {
        this.toAdd = toAdd;
    }

    private void updateLevel(HeroStats stats) {
        stats.maxExp += 100;
        stats.level++;
        stats.maxHealth += 10;
    }

    @Override
    public void visit(HeroStats stats) {
        if (stats.maxExp < stats.exp + toAdd) {
            toAdd -= stats.maxExp - stats.exp;
            updateLevel(stats);
            stats.exp = 0;
            visit(stats);
        } else {
            stats.exp += toAdd;
        }
    }
}
