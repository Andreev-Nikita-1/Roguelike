package renderer.inventoryWindow;

import basicComponents.AppLogic;
import hero.items.Item;
import util.Coord;

/**
 * Window with four taken items
 */
public class TakenWindow extends TileWindow {
    TakenWindow(Coord location) {
        super(location, new Coord(4, 1));
    }

    @Override
    public InventoryText getText() {
        Item item = AppLogic.currentGame.hero.inventory.taken[cursorPosition.x];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.EMPTY_TEXT;
    }

    @Override
    Item getItem(Coord position) {
        return AppLogic.currentGame.hero.inventory.taken[position.x];
    }
}
