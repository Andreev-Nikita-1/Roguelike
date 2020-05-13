package inventory;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;
import util.Direction;

import static util.Direction.*;

public abstract class TileWindow extends CursorWindow {
    public TileWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    protected boolean tryShift(Direction direction) {
        if (cursorPosition.shifted(direction).between(Coord.ZERO, size.shifted(UP).shifted(LEFT))) {
            cursorPosition.shift(direction);
            return true;
        }
        return false;
    }


}


