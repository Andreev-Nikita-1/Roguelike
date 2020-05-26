package hero.inventoryWindow;


import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;

import java.awt.*;


abstract class Subwindow {
    static final Color BACKGROUND_COLOR = new Color(0, 0, 0);
    static final Color SELECTED_BACKGROUND_COLOR = new Color(15, 15, 15);
    static final Color TEXT_COLOR = Color.WHITE;

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
