package inventory;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;

public class ArmorWindow extends TileWindow {
    public ArmorWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    public InventoryText getText() {
        return InventoryText.emptyText;
    }

    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
        graphics.setForegroundColor(new TextColor.RGB(70, 70, 70));
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                graphics.putString(i + inventoryWindowLocation.x + location.x,
                        j + inventoryWindowLocation.y + location.y, String.valueOf((char) 9974));
            }
        }
        if (active) {
            graphics.setForegroundColor(new TextColor.RGB(200, 50, 50));
            graphics.putString(cursorPosition.x + inventoryWindowLocation.x + location.x,
                    cursorPosition.y + inventoryWindowLocation.y + location.y, String.valueOf((char) 9974));
        }
    }
}
