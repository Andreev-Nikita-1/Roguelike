package hero.items;

import hero.inventoryWindow.InventoryText;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Shield extends Item {
    public AtomicInteger durability;
    public int maxDurability;
    public int protection;

    public Shield(int durability, int protection) {
        this.durability = new AtomicInteger(durability);
        maxDurability = durability;
        this.protection = protection;
    }

    @Override
    public void use() {
    }

    @Override
    public InventoryText getText() {
        return null;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public char getSymbol() {
        return 0;
    }

    public double getDurabilityLevel() {
        return durability.get() / (double) maxDurability;
    }
}
