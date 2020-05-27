package hero.items;


import java.util.concurrent.atomic.AtomicInteger;

public abstract class Equipment extends Item {
    public AtomicInteger durability;
    public int maxDurability;
    public int value;
    public String name;

    public Equipment(int durability, int value, String name) {
        this.durability = new AtomicInteger(durability);
        maxDurability = durability;
        this.value = value;
        this.name = name;
    }

    @Override
    public void use() {
    }

    public double getDurabilityLevel() {
        return durability.get() / (double) maxDurability;
    }
}
