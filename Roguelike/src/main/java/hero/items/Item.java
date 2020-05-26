package hero.items;

import hero.Inventory;
import hero.inventoryWindow.InventoryText;

import java.awt.*;

public abstract class Item {

    public void applyTakenEffect(boolean taken) {
    }

    protected Inventory owner;

    public void setOwner(Inventory inventory) {
        owner = inventory;
    }

    public abstract void use();

    public void delete() {
        for (int i = 0; i < owner.taken.length; i++) {
            if (owner.taken[i] == this) {
                applyTakenEffect(false);
                owner.taken[i] = null;
                return;
            }
        }
        if (owner.weapon == this) {
            applyTakenEffect(false);
            owner.weapon = null;
            return;
        }
        if (owner.shield == this) {
            applyTakenEffect(false);
            owner.shield = null;
            return;
        }
        for (int i = 0; i < owner.baggageSize.x; i++) {
            for (int j = 0; j < owner.baggageSize.y; j++) {
                if (owner.baggage[i][j] == this) {
                    owner.baggage[i][j] = null;
                    return;
                }
            }
        }
    }

    public abstract InventoryText getText();

    public abstract Color getColor();

    public abstract char getSymbol();
}
