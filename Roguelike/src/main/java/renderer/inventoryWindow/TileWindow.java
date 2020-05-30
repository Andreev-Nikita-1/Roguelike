package renderer.inventoryWindow;

import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.items.Item;
import util.Coord;
import util.Direction;

import java.awt.*;

import static util.Direction.*;
import static util.Util.convertColor;

/**
 * Window, which cursor can move within rectangle tile, containing items
 */
public abstract class TileWindow extends CursorWindow {
    public static final Color EMPTY_SYMBOL_COLOR = new Color(40, 40, 40);
    public static final char EMPTY_SYMBOL = (char) 0x01F6;

    TileWindow(Coord location, Coord size) {
        super(location, size);
    }

    /**
     * Returns item in given position
     */
    abstract Item getItem(Coord position);

    @Override
    protected boolean tryShift(Direction direction) {
        if (cursorPosition.shifted(direction).between(Coord.ZERO, size.shifted(UP).shifted(LEFT))) {
            cursorPosition.shift(direction);
            return true;
        }
        return false;
    }

    @Override
    void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                Item item = getItem(new Coord(i, j));
                Color symbolColor = (item == null) ? EMPTY_SYMBOL_COLOR : item.getColor();
                char symbol = (item == null) ? EMPTY_SYMBOL : item.getSymbol();
                if (active && cursorPosition.equals(new Coord(i, j))) {
                    graphics.setBackgroundColor(convertColor(SELECTED_BACKGROUND_COLOR));
                    symbolColor = mark(symbolColor);
                } else {
                    graphics.setBackgroundColor(convertColor(BACKGROUND_COLOR));
                }
                graphics.setForegroundColor(convertColor(symbolColor));
                graphics.putString(i + inventoryWindowLocation.x + location.x,
                        j + inventoryWindowLocation.y + location.y, String.valueOf(symbol));
            }
        }
    }
}


