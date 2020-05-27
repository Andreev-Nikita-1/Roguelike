package renderer.inventoryWindow;

import basicComponents.AppLogic;
import hero.items.Item;
import util.Coord;

public class TakenWindow extends TileWindow {
    public TakenWindow(Coord location) {
        super(location, new Coord(4, 1));
    }

    @Override
    public InventoryText getText() {
        Item item = AppLogic.currentGame.currentInventory.taken[cursorPosition.x];
        if (active && item != null) {
            return item.getText();
        }
        return InventoryText.EMPTY_TEXT;
    }

    @Override
    Item getItem(Coord position) {
        return AppLogic.currentGame.currentInventory.taken[position.x];
    }
}
