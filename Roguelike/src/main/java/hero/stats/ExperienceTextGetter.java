package hero.stats;

import renderer.inventoryWindow.InventoryText;
import renderer.inventoryWindow.InventoryWindow;
import util.Util;

import java.util.Arrays;

import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;

/**
 * Create InventoryText, that is shown, when cursor is focused on experience
 */
public class ExperienceTextGetter extends TextVisitor {

    @Override
    public void visit(HeroStats stats) {
        text = new InventoryText(
                "EXPERIENCE",
                "Your level affects hero power, protection, maximum health",
                Arrays.asList(
                        "level: " + stats.getLevel(),
                        Util.tightNumber(stats.getExp()) + "/" + Util.tightNumber(stats.getMaxExp()),
                        Util.horizontalScale(InventoryWindow.textWindowSize.x, stats.getExp() / (double) (stats.getMaxExp()))),
                Arrays.asList(
                        Arrays.asList(TEXT_COLOR, TEXT_COLOR),
                        Arrays.asList(STATS_COLOR),
                        Arrays.asList(STATS_COLOR)));
    }
}
