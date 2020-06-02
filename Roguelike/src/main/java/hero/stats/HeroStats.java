package hero.stats;

import hero.Inventory;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stats, containing information of hero
 */
public class HeroStats {

    Inventory ownerInventory;

    int level;
    int exp;
    int maxExp;

    AtomicInteger health;
    int maxHealth;


    double getPowerCoeffitent() {
        return 1 + level / 10.0;
    }

    int basicPower = 10;


    double getProtectionCoeffitent() {
        return 1 + level / 10.0;
    }

    int basicProtection = 10;

    AtomicInteger stamina;
    int maxStamina;
    int luck;


    private HeroStats(int level, int exp, int maxExp, AtomicInteger health, int maxHealth, int stamina, int maxStamina, int luck) {
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
        this.health = health;
        this.maxHealth = maxHealth;
        this.stamina = new AtomicInteger(stamina);
        this.maxStamina = maxStamina;
        this.luck = luck;
    }

    /**
     * Creates default stats
     */
    public HeroStats() {
        this(0, 0, 100, new AtomicInteger(100),
                100, 50, 50, 0);
    }

    /**
     * Accepts StatsVisitor
     */
    public void accept(StatsVisitor visitor) {
        visitor.visit(this);
    }


    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getHealth() {
        return health.get();
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Power calculated by (basicPower + weaponPower) * powerCoefficent
     */
    public int getPower() {
        if (ownerInventory.weapon == null) return (int) (getPowerCoeffitent() * basicPower);
        return (int) (getPowerCoeffitent() * (ownerInventory.weapon.value + basicPower));
    }

    /**
     * Protection calculated by (basicProtection + shieldProtection) * protectionCoefficent
     */
    public int getProtection() {
        if (ownerInventory.shield == null) return (int) (getProtectionCoeffitent() * basicProtection);
        return (int) (getProtectionCoeffitent() * (ownerInventory.shield.value + basicProtection));
    }

    public int getAttackDelay() {
        if (ownerInventory.weapon == null) return 200;
        return ownerInventory.weapon.attackDelay;
    }

    public void setOwnerInventory(Inventory ownerInventory) {
        this.ownerInventory = ownerInventory;
    }

    public int getStamina() {
        return stamina.get();
    }

    public int getMaxStamina() {
        return maxStamina;
    }


    /**
     * Takes stats snapshot
     */
    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("level", level)
                .put("exp", exp)
                .put("maxExp", maxExp)
                .put("health", health.get())
                .put("maxHealth", maxHealth)
                .put("stamina", stamina)
                .put("maxStamina", maxStamina)
                .put("luck", luck);
    }

    /**
     * Restores stats from snapshot
     */
    public static HeroStats restoreFromSnapshot(JSONObject jsonObject) {
        return new HeroStats(
                jsonObject.getInt("level"),
                jsonObject.getInt("exp"),
                jsonObject.getInt("maxExp"),
                new AtomicInteger(jsonObject.getInt("health")),
                jsonObject.getInt("maxHealth"),
                jsonObject.getInt("stamina"),
                jsonObject.getInt("maxStamina"),
                jsonObject.getInt("luck")
        );
    }
}
