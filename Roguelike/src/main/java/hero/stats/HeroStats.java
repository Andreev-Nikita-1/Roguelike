package hero.stats;

import hero.Inventory;

import java.util.concurrent.atomic.AtomicInteger;

public class HeroStats {

    Inventory owner;

    int level;
    int exp;
    int maxExp;

    AtomicInteger health;
    int basicMaxHealth;

    double powerCoeffitent = 1;
    int basicPower = 10;
    double protectionCoeffitent = 1;
    int basicProtection = 10;

    AtomicInteger stamina;
    int maxStamina;
    int luck;


    public HeroStats(int level, int exp, int maxExp, AtomicInteger health, int maxHealth, int protection, int power, int attackDelay, int stamina, int maxStamina, int luck) {
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
        this.health = health;
        this.basicMaxHealth = maxHealth;
        this.stamina = new AtomicInteger(stamina);
        this.maxStamina = maxStamina;
        this.luck = luck;
    }

    public void accept(StatsVisitor visitor) {
        visitor.visit(this);
    }


    public int getHealth() {
        return health.get();
    }

    public int getPower() {
        if (owner.weapon == null) return (int) (powerCoeffitent * basicPower);
        return (int) (powerCoeffitent * (owner.weapon.power + basicPower));
    }

    public int getProtection() {
        if (owner.shield == null) return (int) (protectionCoeffitent * basicProtection);
        return (int) (protectionCoeffitent * (owner.shield.protection + basicProtection));
    }

    public int getAttackDelay() {
        if (owner.weapon == null) return 200;
        return owner.weapon.attackDelay;
    }

    public void setOwner(Inventory owner) {
        this.owner = owner;
    }

    public int getStamina() {
        return stamina.get();
    }
}
