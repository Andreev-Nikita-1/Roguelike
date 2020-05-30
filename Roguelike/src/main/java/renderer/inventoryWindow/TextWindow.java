package renderer.inventoryWindow;

import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static util.Coord.DOWN;
import static util.Coord.RIGHT;
import static util.Util.convertColor;

/**
 * Window, which draws text, provided by active cursor window
 */
public class TextWindow extends Subwindow {
    private volatile InventoryText text;

    private int bias = 0;
    private int lengthLines = 0;

    void setText(InventoryText text) {
        this.text = text;
    }

    void resetBias() {
        bias = 0;
    }

    TextWindow(Coord location, Coord size) {
        super(location, size);
    }

    /**
     * Draws title
     */
    private void drawTitle(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        graphics.setForegroundColor(convertColor(InventoryText.TEXT_COLOR));
        int bias = (size.x - text.title.length()) / 2;
        for (int i = 0; i < size.x; i++) {
            graphics.setCharacter(inventoryWindowLocation.x + location.x + i,
                    inventoryWindowLocation.y + location.y, (char) 0x01F5);
        }
        graphics.putString(inventoryWindowLocation.x + location.x + bias,
                inventoryWindowLocation.y + location.y, text.title);
    }

    /**
     * Draws background
     */
    private void drawBackground(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        graphics.setBackgroundColor(convertColor(BACKGROUND_COLOR));
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                graphics.putString(i + inventoryWindowLocation.x + location.x,
                        j + inventoryWindowLocation.y + location.y, " ");
            }
        }
    }

    /**
     * Draws text if it fits into frame
     */
    private void drawFullText(TextGUIGraphics graphics, Coord inventoryWindowLocation, List<List<String>> lines) {
        int colorCounter = 0;
        Coord leftUp = inventoryWindowLocation.shifted(location).shifted(DOWN);
        for (int j = 0; j < lines.size(); j++) {
            int x = 0;
            for (int k = 0; k < lines.get(j).size(); k++) {
                String word = lines.get(j).get(k);
                graphics.setForegroundColor(convertColor(text.wordsColors.get(colorCounter++)));
                graphics.putString(x + leftUp.x,
                        j + leftUp.y,
                        word);
                x += word.length() + 1;
            }
        }
    }

    /**
     * Draws part of text, starting from line number bias
     */
    private void drawPartText(TextGUIGraphics graphics, Coord inventoryWindowLocation, List<List<String>> lines) {
        int colorCounter = 0;
        for (int j = 0; j < bias; j++) {
            colorCounter += lines.get(j).size();
        }
        Coord leftUp = inventoryWindowLocation.shifted(location).shifted(DOWN).shifted(RIGHT);
        for (int j = bias; j < lines.size(); j++) {
            int x = 0;
            for (int k = 0; k < lines.get(j).size(); k++) {
                String word = lines.get(j).get(k);
                graphics.setForegroundColor(convertColor(text.wordsColors.get(colorCounter++)));
                if (j - bias >= size.y - 2 - text.info.size()) return;
                graphics.putString(x + leftUp.x,
                        j - bias + leftUp.y,
                        word);
                x += word.length() + 1;
            }
        }
    }


    private static final Color grayArrow = new Color(40, 40, 40);

    /**
     * Draws two arrows for scrolling large text
     */
    private void drawFlipping(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        Color colorUp = (bias == 0) ? grayArrow : InventoryText.TEXT_COLOR;
        graphics.setForegroundColor(convertColor(colorUp));
        graphics.putString(inventoryWindowLocation.x + location.x,
                inventoryWindowLocation.y + location.y + 1, String.valueOf(up));
        Color colorDown = (bias == lengthLines - (size.y - 2 - text.info.size())) ? grayArrow : InventoryText.TEXT_COLOR;
        graphics.setForegroundColor(convertColor(colorDown));
        graphics.putString(inventoryWindowLocation.x + location.x,
                inventoryWindowLocation.y + location.y + size.y - text.info.size() - 2,
                String.valueOf(down));
    }

    /**
     * Draws info
     */
    private void drawInfo(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        for (int j = 0; j < text.info.size(); j++) {
            String[] words = text.info.get(j).split(" ");
            int x = 0;
            Iterator<Color> colors = text.infoWordsColors.get(j).iterator();
            for (String word : words) {
                graphics.setForegroundColor(convertColor(colors.next()));
                graphics.putString(inventoryWindowLocation.x + location.x + x,
                        size.y - text.info.size() + j + inventoryWindowLocation.y + location.y,
                        word);
                x += word.length() + 1;
            }
        }
    }

    /**
     * Draws full text window
     */
    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        drawBackground(graphics, inventoryWindowLocation);
        drawTitle(graphics, inventoryWindowLocation);
        List<List<String>> lines = text.lines(size.x);
        if (lines.size() <= size.y - 1 - text.info.size()) {
            drawFullText(graphics, inventoryWindowLocation, lines);
        } else {
            lines = text.lines(size.x - 1);
            lengthLines = lines.size();
            drawPartText(graphics, inventoryWindowLocation, lines);
            drawFlipping(graphics, inventoryWindowLocation);
        }
        graphics.setForegroundColor(convertColor(InventoryText.TEXT_COLOR));
        for (int i = 0; i < size.x; i++) {
            graphics.setCharacter(inventoryWindowLocation.x + location.x + i,
                    inventoryWindowLocation.y + location.y + size.y - 1 - text.info.size(), (char) 0x01F5);
        }
        drawInfo(graphics, inventoryWindowLocation);
    }


    static char up = (char) 0x01F8;
    static char down = (char) 0x01F9;

    /**
     * Scrolls text
     */

    void pgUp() {
        if (bias > 0) bias--;
    }

    void pgDn() {
        if (bias < lengthLines - (size.y - 2 - text.info.size())) bias++;
    }
}
