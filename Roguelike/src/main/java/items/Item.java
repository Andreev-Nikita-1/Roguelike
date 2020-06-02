package items;

import hero.Inventory;
import org.json.JSONObject;
import renderer.inventoryWindow.InventoryText;
import util.Coord;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Class for item
 */
public abstract class Item {

    /**
     * Called when hero take this or put in baggage
     */
    public void applyTakenEffect(boolean taken) {
    }

    public Coord baggagePlace;
    Inventory ownerInventory;

    /**
     * Sets inventory
     */
    public void setOwnerInventory(Inventory inventory) {
        ownerInventory = inventory;
    }

    /**
     * Sets last place in baggage, so when player put off item back to baggage, it places to the position, when in was before
     */
    public Item setBaggagePlace(Coord coord) {
        baggagePlace = coord;
        return this;
    }

    /**
     * Called, when taken by hero, and corresponding key is pressed
     */
    public abstract void use();

    /**
     * Deletes item from inventory
     */
    public void delete() {
        for (int i = 0; i < ownerInventory.taken.length; i++) {
            if (ownerInventory.taken[i] == this) {
                applyTakenEffect(false);
                ownerInventory.taken[i] = null;
                return;
            }
        }
        if (ownerInventory.weapon == this) {
            applyTakenEffect(false);
            ownerInventory.weapon = null;
            return;
        }
        if (ownerInventory.shield == this) {
            applyTakenEffect(false);
            ownerInventory.shield = null;
            return;
        }
        for (int i = 0; i < ownerInventory.baggageSize.x; i++) {
            for (int j = 0; j < ownerInventory.baggageSize.y; j++) {
                if (ownerInventory.baggage[i][j] == this) {
                    ownerInventory.baggage[i][j] = null;
                    return;
                }
            }
        }
    }

    /**
     * Returns text, which has to be shown in TextWindow, when focused on item
     */
    public abstract InventoryText getText();

    /**
     * Returns color
     */
    public abstract Color getColor();

    /**
     * Returns symbol
     */
    public abstract char getSymbol();

    /**
     * Takes snapshot
     */
    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("xBaggage", (baggagePlace == null) ? 0 : baggagePlace.x)
                .put("yBaggage", (baggagePlace == null) ? 0 : baggagePlace.y)
                .put("class", this.getClass().getName());
    }

    /**
     * Invokes method "restoreFromSnapshot" on saved class, taken from snapshot by "class" key
     */
    public static Item restoreSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Item) Class
                .forName(jsonObject.getString("class"))
                .getMethod("restoreFromSnapshot", JSONObject.class)
                .invoke(null, jsonObject);
    }
}
