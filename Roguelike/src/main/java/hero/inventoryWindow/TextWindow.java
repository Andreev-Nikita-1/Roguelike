package hero.inventoryWindow;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;

import java.util.Arrays;
import java.util.List;

import static util.Coord.DOWN;
import static util.Coord.RIGHT;

public class TextWindow extends Subwindow {
    public volatile InventoryText text;

    private int bias = 0;
    private int lengthLines = 0;

    public void setText(InventoryText text) {
        this.text = text;
    }

    public void resetBias() {
        bias = 0;
    }

    public TextWindow(Coord location, Coord size) {
        super(location, size);
    }


    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
        graphics.setForegroundColor(new TextColor.RGB(255, 255, 255));
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                graphics.putString(i + inventoryWindowLocation.x + location.x,
                        j + inventoryWindowLocation.y + location.y, " ");
            }
        }
        graphics.putString(inventoryWindowLocation.x + location.x,
                inventoryWindowLocation.y + location.y,
                text.title);
        List<List<String>> lines = text.lines(size.x);
        if (lines.size() <= size.y - 1 - text.info.size()) {
            drawText(graphics, inventoryWindowLocation.shifted(location).shifted(DOWN),
                    new Coord(size.x, size.y - 1 - text.info.size()), lines);
        } else {
            lines = text.lines(size.x - 1);
            lengthLines = lines.size();
            drawText(graphics, inventoryWindowLocation.shifted(location).shifted(DOWN).shifted(RIGHT),
                    new Coord(size.x - 1, size.y - 1 - text.info.size()), lines);
            if (bias == 0) {
                graphics.setForegroundColor(gray);
            } else {
                graphics.setForegroundColor(white);
            }
            graphics.putString(inventoryWindowLocation.x + location.x,
                    inventoryWindowLocation.y + location.y + 1, String.valueOf(up));

            if (bias == lengthLines - (size.y - 1 - text.info.size())) {
                graphics.setForegroundColor(gray);
            } else {
                graphics.setForegroundColor(white);
            }
            graphics.putString(inventoryWindowLocation.x + location.x,
                    inventoryWindowLocation.y + location.y + size.y - text.info.size() - 1, String.valueOf(down));
        }
        for (int j = 0; j < text.info.size(); j++) {
            graphics.setForegroundColor(text.infoColors.get(j));
            graphics.putString(inventoryWindowLocation.x + location.x,
                    size.y - text.info.size() + j + inventoryWindowLocation.y + location.y,
                    text.info.get(j));
        }
    }

    private void drawText(TextGUIGraphics graphics, Coord leftUp, Coord size, List<List<String>> lines) {
        int colorCounter = 0;
        for (int j = 0; j < bias; j++) {
            colorCounter += lines.get(j).size();

        }
        for (int j = bias; j < lines.size(); j++) {
            int x = 0;
            for (int k = 0; k < lines.get(j).size(); k++) {
                String word = lines.get(j).get(k);
                graphics.setForegroundColor(text.wordsColors.get(colorCounter++));
                if (j - bias >= size.y) return;
                graphics.putString(x + leftUp.x,
                        j - bias + leftUp.y,
                        word);
                x += word.length() + 1;
            }
        }
    }

    static char up = (char) 0x01F8;
    static char down = (char) 0x01F9;
    static TextColor gray = new TextColor.RGB(50, 50, 50);
    static TextColor white = new TextColor.RGB(255, 255, 255);

    public void pgUp() {
        if (bias > 0) bias--;
    }

    public void pgDn() {
        if (bias < lengthLines - (size.y - 1 - text.info.size())) bias++;
    }
}
