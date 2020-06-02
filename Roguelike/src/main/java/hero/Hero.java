package hero;

import hero.stats.HeroStats;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Hero have HeroStats and Inventory
 */
public class Hero {
    public Inventory inventory;
    public HeroStats stats;

    public Hero(Inventory inventory, HeroStats stats) {
        this.inventory = inventory;
        this.stats = stats;
        stats.setOwnerInventory(inventory);
    }

    /**
     * Takes snapshot
     */
    public JSONObject getSnapshot() {
        return new JSONObject().put("stats", stats.getSnapshot()).put("inventory", inventory.getSnapshot());
    }

    /**
     * Restores hero from snapshot
     */
    public static Hero restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return new Hero(Inventory.restoreFromSnapshot(jsonObject.getJSONObject("inventory")),
                HeroStats.restoreFromSnapshot(jsonObject.getJSONObject("stats")));
    }
}
