package hero.stats;

import renderer.inventoryWindow.InventoryText;
import renderer.inventoryWindow.InventoryWindow;
import util.Util;

import java.awt.*;
import java.util.Arrays;

public class HealthTextGetter extends TextVisitor {

    @Override
    public void visit(HeroStats stats) {
        text = new InventoryText(
                "HEALTH",
                "When it runs out, you die",
                Arrays.asList(Util.tightNumber(stats.getHealth()) + "/" + Util.tightNumber(stats.getMaxHealth()),
                        Util.horizontalScale(InventoryWindow.textWindowSize.x, stats.getHealth() / (double) (stats.getMaxHealth()))),
                Arrays.asList(Arrays.asList(Color.RED), Arrays.asList(Color.RED)));
    }
}
