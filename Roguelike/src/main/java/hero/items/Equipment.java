package hero.items;


import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Equipment: weapon and shield
 */
public abstract class Equipment extends Item {
    public AtomicInteger durability;
    public int maxDurability;
    public int value;
    public String name;

    Equipment(int durability, int value, String name) {
        this.durability = new AtomicInteger(durability);
        maxDurability = durability;
        this.value = value;
        this.name = name;
    }

    /**
     * Cant be used
     */
    @Override
    public void use() {
    }

    /**
     * Sets durability
     */
    public Equipment setDurability(int durability) {
        this.durability = new AtomicInteger(durability);
        return this;
    }

    /**
     * Get durability proportion. Used for drawing scale
     */
    public double getDurabilityLevel() {
        return durability.get() / (double) maxDurability;
    }

    /**
     * Takes default snapshot. Can be overriden
     */
    @Override
    public JSONObject getSnapshot() {
        JSONObject snapshot = super.getSnapshot();
        snapshot.put("durability", durability.get());
        snapshot.put("maxDurability", maxDurability);
        snapshot.put("value", value);
        snapshot.put("name", name);
        return snapshot;
    }
}
