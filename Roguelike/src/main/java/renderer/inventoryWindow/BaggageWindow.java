package renderer.inventoryWindow;

import basicComponents.AppLogic;
import items.Item;
import util.Coord;


/**
 * Window for hero baggage
 */
public class BaggageWindow extends TileWindow {

    BaggageWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    Item getItem(Coord position) {
        return AppLogic.currentGame.getHero().inventory.baggage[position.x][position.y];
    }

    @Override
    public InventoryText getText() {
        Item item = AppLogic.currentGame.getHero().inventory.baggage[cursorPosition.x][cursorPosition.y];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.EMPTY_TEXT;
    }
}

