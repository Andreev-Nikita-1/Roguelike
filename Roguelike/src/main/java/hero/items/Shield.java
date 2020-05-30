package hero.items;

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
 * Shield, which affects protection
 */
public class Shield extends Equipment {
    private static final Color SYMBOL_COLOR = new Color(199, 84, 1);
    private static final char SHIELD1_SYMBOL = (char) 0x0135;
    private static final char SHIELD2_SYMBOL = (char) 0x0136;
    private static final char SHIELD3_SYMBOL = (char) 0x0137;


    Type shieldType;

    public Shield(int durability, int protection, String name, Type type) {
        super(durability, protection, name);
        shieldType = type;
    }

    public enum Type {
        SHIELD1, SHIELD2, SHIELD3
    }


    @Override
    public InventoryText getText() {
        return new InventoryText(name,
                "",
                Arrays.asList("protection: " + tightNumber(value),
                        tightNumber(durability.get()) + "/" + tightNumber(maxDurability)),
                Arrays.asList(Arrays.asList(TEXT_COLOR, TEXT_COLOR),
                        Arrays.asList(greenRedScale(getDurabilityLevel()))));
    }

    @Override
    public Color getColor() {
        return SYMBOL_COLOR;
    }

    @Override
    public char getSymbol() {
        switch (shieldType) {
            case SHIELD1:
                return SHIELD1_SYMBOL;
            case SHIELD2:
                return SHIELD2_SYMBOL;
            case SHIELD3:
                return SHIELD3_SYMBOL;
        }
        return ' ';
    }


    /**
     * Returns shield snapshot
     */
    @Override
    public JSONObject getSnapshot() {
        return super.getSnapshot().put("type", shieldType.ordinal());
    }

    /**
     * Restores shield from snapshot
     */
    public static Shield restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Shield) new Shield(jsonObject.getInt("maxDurability"),
                jsonObject.getInt("value"),
                jsonObject.getString("name"),
                Type.values()[jsonObject.getInt("type")]
        ).setDurability(jsonObject.getInt("durability"))
                .setBaggagePlace(new Coord(jsonObject.getInt("xBaggage"),
                        jsonObject.getInt("yBaggage")));
    }
}
