package items;

import org.json.JSONObject;
import renderer.inventoryWindow.InventoryText;
import util.Coord;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;
import static util.Util.greenRedScale;
import static util.Util.tightNumber;

/**
 * Weapon, which affects attack
 */
public class Weapon extends Equipment {
    private static final Color SYMBOL_COLOR = new Color(196, 202, 206);
    private static final char KNIFE_SYMBOL = (char) 0x0130;
    private static final char SWORD_SYMBOL = (char) 0x0131;
    private static final char AXE_SYMBOL = (char) 0x0132;
    private static final char MORGENSTAR_SYMBOL = (char) 0x0133;


    Type weaponType;
    public int attackDelay;

    public Weapon(int durability, int power, int attackDelay, String name, Type type) {
        super(durability, power, name);
        weaponType = type;
        this.attackDelay = attackDelay;
    }


    public enum Type {
        KNIFE, SWORD, AXE, MORGENSTAR
    }

    @Override
    public InventoryText getText() {
        return new InventoryText(name,
                "",
                Arrays.asList("power: " + tightNumber(value),
                        "delay: " + tightNumber(attackDelay),
                        tightNumber(durability.get()) + "/" + tightNumber(maxDurability)),
                Arrays.asList(Arrays.asList(TEXT_COLOR, TEXT_COLOR),
                        Arrays.asList(TEXT_COLOR, TEXT_COLOR),
                        Arrays.asList(greenRedScale(getDurabilityLevel()))));
    }

    @Override
    public Color getColor() {
        return SYMBOL_COLOR;
    }

    @Override
    public char getSymbol() {
        switch (weaponType) {
            case KNIFE:
                return KNIFE_SYMBOL;
            case AXE:
                return AXE_SYMBOL;
            case SWORD:
                return SWORD_SYMBOL;
            case MORGENSTAR:
                return MORGENSTAR_SYMBOL;
        }
        return ' ';
    }


    /**
     * Takes snapshot
     */
    @Override
    public JSONObject getSnapshot() {
        return super.getSnapshot().put("type", weaponType.ordinal()).put("delay", attackDelay);
    }

    /**
     * Restores weapon from snapshot
     */
    public static Weapon restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Weapon) new Weapon(jsonObject.getInt("maxDurability"),
                jsonObject.getInt("value"),
                jsonObject.getInt("delay"),
                jsonObject.getString("name"),
                Type.values()[jsonObject.getInt("type")]
        ).setDurability(jsonObject.getInt("durability"))
                .setBaggagePlace(new Coord(jsonObject.getInt("xBaggage"),
                        jsonObject.getInt("yBaggage")));
    }
}
