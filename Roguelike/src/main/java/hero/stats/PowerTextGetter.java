package hero.stats;

import hero.items.Weapon;
import renderer.inventoryWindow.InventoryText;
import util.Util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;
import static util.Util.tightNumber;


public class PowerTextGetter extends TextVisitor {
    @Override
    public void visit(HeroStats stats) {
        List<String> info = new ArrayList<>();
        List<List<Color>> colors = new ArrayList<>();
        List<Color> firstColors = new ArrayList<>();
        firstColors.add(TEXT_COLOR);
        firstColors.add(STATS_COLOR);
        info.add("basic: " + Util.tightNumber(stats.basicPower));
        colors.add(firstColors);
        Weapon weapon = stats.owner.weapon;
        if (weapon != null) {
            info.add("weapon: " + Util.tightNumber(weapon.value)
                    + " " + (char) 0x008A + Util.tightNumber((int) (stats.powerCoeffitent * 100)) + "%");
            List<Color> secondColors = new ArrayList<>();
            secondColors.add(TEXT_COLOR);
            secondColors.add(TEXT_COLOR);
            secondColors.add(STATS_COLOR);
            colors.add(secondColors);
        }
        text = new InventoryText(
                "POWER",
                "",
                info, colors);
    }
}
