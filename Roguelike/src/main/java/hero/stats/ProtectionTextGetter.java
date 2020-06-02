package hero.stats;

import items.Shield;
import renderer.inventoryWindow.InventoryText;
import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;

/**
 * Create InventoryText, that is shown, when cursor is focused on protection
 */
public class ProtectionTextGetter extends TextVisitor {
    @Override
    public void visit(HeroStats stats) {
        List<String> info = new ArrayList<>();
        List<List<Color>> colors = new ArrayList<>();
        List<Color> firstColors = new ArrayList<>();
        firstColors.add(TEXT_COLOR);
        firstColors.add(STATS_COLOR);
        info.add("basic: " + Util.tightNumber((int) (stats.basicProtection * stats.getProtectionCoeffitent())));
        colors.add(firstColors);
        Shield shield = stats.ownerInventory.shield;
        if (shield != null) {
            info.add("shield: " + Util.tightNumber(shield.value)
                    + " " + (char) 0x008A + Util.tightNumber((int) (stats.getPowerCoeffitent() * 100)) + "%");
            List<Color> secondColors = new ArrayList<>();
            secondColors.add(TEXT_COLOR);
            secondColors.add(TEXT_COLOR);
            secondColors.add(STATS_COLOR);
            colors.add(secondColors);
        }
        text = new InventoryText(
                "PROTECTION",
                "Your protection absorbs some of the damage taken",
                info, colors);
    }
}
