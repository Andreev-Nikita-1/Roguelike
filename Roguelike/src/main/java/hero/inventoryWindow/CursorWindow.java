package hero.inventoryWindow;

import util.Coord;
import util.Direction;

abstract class CursorWindow extends Subwindow {
    Coord cursorPosition;
    boolean active = false;

    CursorWindow(Coord location, Coord size) {
        super(location, size);
    }


    protected abstract boolean tryShift(Direction direction);

    abstract InventoryText getText();

    void acceptCursor(Coord coord) {
        cursorPosition = coord;
    }
}
