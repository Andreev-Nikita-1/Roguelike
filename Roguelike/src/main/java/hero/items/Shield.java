package hero.items;

import renderer.inventoryWindow.InventoryText;

import java.awt.*;
import java.util.Arrays;

import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;
import static util.Util.greenRedScale;
import static util.Util.tightNumber;

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
    public void use() {
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

}
