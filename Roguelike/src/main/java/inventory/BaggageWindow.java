package inventory;

import basicComponents.GameplayLogic;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import inventory.items.Item;
import util.Coord;

public class BaggageWindow extends TileWindow {

    public BaggageWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    public InventoryText getText() {
        Item item = GameplayLogic.currentHero.baggage[cursorPosition.x][cursorPosition.y];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.emptyText;
    }


    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
                graphics.setForegroundColor(new TextColor.RGB(70, 70, 70));
                if (active && cursorPosition.equals(new Coord(i, j)))
                    graphics.setBackgroundColor(new TextColor.RGB(50, 30, 30));
                char symbol = (char) 9974;
                if (GameplayLogic.currentHero.baggage[i][j] != null) {
                    symbol = GameplayLogic.currentHero.baggage[i][j].symbol;
                    graphics.setForegroundColor(GameplayLogic.currentHero.baggage[i][j].getColor());
                }
                graphics.putString(i + inventoryWindowLocation.x + location.x,
                        j + inventoryWindowLocation.y + location.y, String.valueOf(symbol));
            }
        }
    }
}

