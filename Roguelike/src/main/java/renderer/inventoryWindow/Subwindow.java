package renderer.inventoryWindow;


import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;

import java.awt.*;


public abstract class Subwindow {
    static final Color BACKGROUND_COLOR = new Color(10, 10, 10);
    static final Color SELECTED_BACKGROUND_COLOR = new Color(20, 20, 20);

    Coord location;
    Coord size;

    Subwindow(Coord location, Coord size) {
        this.location = location;
        this.size = size;
    }

    static Color mark(Color color) {
        double l = 2;
        int r1 = (int) (255 - (255 - color.getRed()) / l);
        int g1 = (int) (255 - (255 - color.getGreen()) / l);
        int b1 = (int) (255 - (255 - color.getBlue()) / l);
        return new Color(r1, g1, b1);
    }

    abstract void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation);
}
