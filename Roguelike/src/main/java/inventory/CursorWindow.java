package inventory;

import util.Coord;
import util.Direction;

public abstract class CursorWindow extends Subwindow {
    Coord cursorPosition;
    boolean active = false;

    public CursorWindow(Coord location, Coord size) {
        super(location, size);
    }


    protected abstract boolean tryShift(Direction direction);

    public abstract InventoryText getText();

    public void acceptCursor(Coord coord) {
        cursorPosition = coord;
    }
}
