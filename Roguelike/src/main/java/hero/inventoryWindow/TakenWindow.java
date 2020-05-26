package hero.inventoryWindow;

import basicComponents.AppLogic;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.items.Item;
import util.Coord;

import static util.Util.convertColor;

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
        return InventoryText.emptyText;
    }

    @Override
    Item getItem(Coord position) {
        return AppLogic.currentGame.currentInventory.taken[position.x];
    }
}
