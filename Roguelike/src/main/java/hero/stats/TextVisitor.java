package hero.stats;

import renderer.inventoryWindow.InventoryText;

import java.awt.*;

/**
 * Visitor, that collect information to show in TextWindow of InventoryWindow
 */
public abstract class TextVisitor extends StatsVisitor {
    static final Color STATS_COLOR = new Color(100, 252, 151);
    InventoryText text;

    public InventoryText getText() {
        return text;
    }
}
