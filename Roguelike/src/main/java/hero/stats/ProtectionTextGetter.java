package hero.stats;

import hero.items.Shield;
import hero.items.Weapon;
import renderer.inventoryWindow.InventoryText;
import util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;

public class ProtectionTextGetter extends TextVisitor {
    @Override
    public void visit(HeroStats stats) {
        List<String> info = new ArrayList<>();
        List<List<Color>> colors = new ArrayList<>();
        List<Color> firstColors = new ArrayList<>();
        firstColors.add(TEXT_COLOR);
        firstColors.add(STATS_COLOR);
        info.add("basic: " + Util.tightNumber(stats.basicProtection));
        colors.add(firstColors);
        Shield shield = stats.owner.shield;
        if (shield != null) {
            info.add("shield: " + Util.tightNumber(shield.value)
                    + " " + (char) 0x008A + Util.tightNumber((int) (stats.powerCoeffitent * 100)) + "%");
            List<Color> secondColors = new ArrayList<>();
            secondColors.add(TEXT_COLOR);
            secondColors.add(TEXT_COLOR);
            secondColors.add(STATS_COLOR);
            colors.add(secondColors);
        }
        text = new InventoryText(
                "PROTECTION",
                "",
                info, colors);
    }
}
