package renderer.inventoryWindow;

import basicComponents.AppLogic;
import hero.items.Item;
import util.Coord;

public class BaggageWindow extends TileWindow {

    public BaggageWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    Item getItem(Coord position) {
        return AppLogic.currentGame.hero.inventory.baggage[position.x][position.y];
    }

    @Override
    public InventoryText getText() {
        Item item = AppLogic.currentGame.hero.inventory.baggage[cursorPosition.x][cursorPosition.y];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.EMPTY_TEXT;
    }
}

