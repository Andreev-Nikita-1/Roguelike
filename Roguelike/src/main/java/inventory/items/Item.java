package inventory.items;

import com.googlecode.lanterna.TextColor;
import inventory.Hero;
import inventory.InventoryText;

import java.util.List;

public abstract class Item {
    public char symbol;
    protected Hero owner;

    public void setOwner(Hero hero) {
        owner = hero;
    }

    public abstract void use();

    public abstract InventoryText getText();

    public abstract TextColor getColor();
}
