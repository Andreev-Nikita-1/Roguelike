package inventory;


import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;


public abstract class Subwindow {
    Coord location;
    Coord size;

    public Subwindow(Coord location, Coord size) {
        this.location = location;
        this.size = size;
    }

    public abstract void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation);
}
