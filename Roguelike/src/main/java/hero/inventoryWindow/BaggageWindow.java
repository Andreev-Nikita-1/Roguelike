package hero.inventoryWindow;

import basicComponents.AppLogic;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.items.Item;
import util.Coord;

import static util.Util.convertColor;

public class BaggageWindow extends TileWindow {

    public BaggageWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    Item getItem(Coord position) {
        return AppLogic.currentGame.currentInventory.baggage[position.x][position.y];
    }

    @Override
    public InventoryText getText() {
        Item item = AppLogic.currentGame.currentInventory.baggage[cursorPosition.x][cursorPosition.y];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.emptyText;
    }
}

