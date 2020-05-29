package hero.items;

import hero.Inventory;
import org.json.JSONObject;
import renderer.inventoryWindow.InventoryText;
import util.Coord;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public abstract class Item {

    public void applyTakenEffect(boolean taken) {
    }

    public Coord baggagePlace;
    protected Inventory ownerInventory;

    public void setOwnerInventory(Inventory inventory) {
        ownerInventory = inventory;
    }

    public Item setBaggagePlace(Coord coord) {
        baggagePlace = coord;
        return this;
    }

    public abstract void use();

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

    public abstract InventoryText getText();

    public abstract Color getColor();

    public abstract char getSymbol();

    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("xBaggage", (baggagePlace == null) ? 0 : baggagePlace.x)
                .put("yBaggage", (baggagePlace == null) ? 0 : baggagePlace.y)
                .put("class", this.getClass().getName());
    }

    public static Item restoreSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Item) Class
                .forName(jsonObject.getString("class"))
                .getMethod("restoreFromSnapshot", JSONObject.class)
                .invoke(null, jsonObject);
    }
}
