package hero.items;

import hero.inventoryWindow.InventoryText;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static hero.inventoryWindow.InventoryText.emptyText;

public class Weapon extends Item {
    public AtomicInteger durability;
    public int maxDurability;
    Type weaponType;
    public int power;
    public int attackDelay;

    public Weapon(int durability, int power, int attackDelay, Type type) {
        this.durability = new AtomicInteger(durability);
        maxDurability = durability;
        weaponType = type;
        this.power = power;
        this.attackDelay = attackDelay;
    }


    public enum Type {
        KNIFE, SWORD, AXE, MORGENSTAR
    }

    @Override
    public void use() {
    }

    @Override
    public InventoryText getText() {
        return emptyText;
    }

    @Override
    public Color getColor() {
        return new Color(196, 202, 206);
    }

    @Override
    public char getSymbol() {
        return (char) 0x0130;
    }

    public double getDurabilityLevel() {
        return durability.get() / (double) maxDurability;
    }
}
