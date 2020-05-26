package hero.inventoryWindow;

import basicComponents.AppLogic;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;
import util.Direction;
import util.Util;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static util.Direction.DOWN;
import static util.Direction.UP;
import static util.Util.convertColor;

public class StatsWindow extends CursorWindow {
    public StatsWindow(Coord location) {
        super(location, new Coord(3, 5));
    }


    static Color SYMBOL_COLOR = new Color(100, 252, 151);
    static final char EXP = (char) 0x0085;
    static char HP = (char) 0x0086;
    static char POWER = (char) 0x0087;
    static char PROTECTION = (char) 0x0088;
    static char LUCK = (char) 0x0089;

    @Override
    protected boolean tryShift(Direction direction) {
        if (direction.horizontal()
                || direction == DOWN && cursorPosition.y == 4
                || direction == UP && cursorPosition.y == 0) {
            return false;
        }
        cursorPosition.shift(direction);
        return true;
    }

    @Override
    public InventoryText getText() {
        return InventoryText.emptyText;
    }

    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        List<Character> chars = Arrays.asList(EXP, HP, POWER, PROTECTION, LUCK);

        for (int i = 0; i < 5; i++) {
            if (active && cursorPosition.y == i) {
                graphics.setBackgroundColor(convertColor(SELECTED_BACKGROUND_COLOR));
                graphics.setForegroundColor(convertColor(mark(SYMBOL_COLOR)));
                graphics.setCharacter(inventoryWindowLocation.x + location.x,
                        i + inventoryWindowLocation.y + location.y, chars.get(i));
                graphics.setForegroundColor(convertColor(mark(TEXT_COLOR)));
            } else {
                graphics.setBackgroundColor(convertColor(BACKGROUND_COLOR));
                graphics.setForegroundColor(convertColor(SYMBOL_COLOR));
                graphics.setCharacter(inventoryWindowLocation.x + location.x,
                        i + inventoryWindowLocation.y + location.y, chars.get(i));
                graphics.setForegroundColor(convertColor(TEXT_COLOR));
            }
            int n = 100;
            switch (i) {
                case 1:
                    n = AppLogic.currentGame.currentInventory.stats.getHealth();
                    break;
                case 2:
                    n = AppLogic.currentGame.currentInventory.stats.getPower();
                    break;
                case 3:
                    n = AppLogic.currentGame.currentInventory.stats.getProtection();
                    break;

            }
            String str = Util.tightNumber(n);
            if (str.length() == 1) str = " " + str;
            graphics.putString(1 + inventoryWindowLocation.x + location.x,
                    i + inventoryWindowLocation.y + location.y,
                    str);

        }

    }
}

