package renderer.inventoryWindow;

import util.Coord;
import util.Direction;


/**
 * Window that can be active, when doing something in inventory
 */
abstract class CursorWindow extends Subwindow {
    Coord cursorPosition;
    boolean active = false;

    CursorWindow(Coord location, Coord size) {
        super(location, size);
    }


    /**
     * Tries to shift cursor, and returns true if succeeded
     */
    protected abstract boolean tryShift(Direction direction);

    /**
     * Returns InventoryText to be drawn in TextWindow
     */
    abstract InventoryText getText();

    /**
     * Sets cursor
     */
    void acceptCursor(Coord coord) {
        cursorPosition = coord;
    }
}
