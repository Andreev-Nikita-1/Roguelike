package inventory;

import basicComponents.GameplayLogic;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import inventory.items.Item;
import util.Coord;

public class TakenWindow extends TileWindow {
    public TakenWindow(Coord location) {
        super(location, new Coord(3, 1));
    }

    private char number(int n) {
        return (char) (9351 + n);
    }


    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        for (int i = 0; i < size.x; i++) {
            graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
            graphics.setForegroundColor(new TextColor.RGB(70, 70, 70));
            if (active && i == cursorPosition.x) graphics.setBackgroundColor(new TextColor.RGB(50, 30, 30));
            char symbol = number(i + 1);
            if (GameplayLogic.currentHero.taken[i] != null) {
                symbol = GameplayLogic.currentHero.taken[i].symbol;
                graphics.setForegroundColor(GameplayLogic.currentHero.taken[i].getColor());
            }
            graphics.putString(i + inventoryWindowLocation.x + location.x,
                    inventoryWindowLocation.y + location.y, String.valueOf(symbol));
        }
    }

    @Override
    public InventoryText getText() {
        Item item = GameplayLogic.currentHero.taken[cursorPosition.x];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.emptyText;
    }
}
