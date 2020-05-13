package inventory.items;

import com.googlecode.lanterna.TextColor;
import inventory.InventoryText;

import java.util.concurrent.atomic.AtomicInteger;

public class Armor extends Item {


    public int durability;
    public AtomicInteger a;

    @Override
    public void use() {
    }

    @Override
    public InventoryText getText() {
        return null;
    }

    @Override
    public TextColor getColor() {
        return null;
    }
}
