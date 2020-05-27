package hero.stats;

import renderer.inventoryWindow.InventoryText;

import java.awt.*;

public abstract class TextVisitor extends StatsVisitor {
    protected static final Color STATS_COLOR = new Color(100, 252, 151);
    InventoryText text;

    public InventoryText getText() {
        return text;
    }
}
