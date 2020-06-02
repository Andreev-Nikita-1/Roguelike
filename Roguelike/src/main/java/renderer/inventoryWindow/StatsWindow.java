package renderer.inventoryWindow;

import basicComponents.AppLogic;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.stats.*;
import util.Coord;
import util.Direction;
import util.Util;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static util.Direction.DOWN;
import static util.Direction.UP;
import static util.Util.convertColor;

/**
 * Window with stats
 */
public class StatsWindow extends CursorWindow {


    StatsWindow(Coord location) {
        super(location, new Coord(3, 5));
    }


    static final Color SYMBOL_COLOR = new Color(100, 252, 151);
    static final char EXP = (char) 0x0085;
    static final char HP = (char) 0x0086;
    static final char POWER = (char) 0x0087;
    static final char PROTECTION = (char) 0x0088;
    static final char LUCK = (char) 0x0089;

    private ExperienceTextGetter experienceTextGetter = new ExperienceTextGetter();
    private HealthTextGetter healthTextGetter = new HealthTextGetter();
    private PowerTextGetter powerTextGetter = new PowerTextGetter();
    private ProtectionTextGetter protectionTextGetter = new ProtectionTextGetter();

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
        TextVisitor textGetter = null;
        if (cursorPosition.y == 0) {
            textGetter = experienceTextGetter;
        }
        if (cursorPosition.y == 1) {
            textGetter = healthTextGetter;
        }
        if (cursorPosition.y == 2) {
            textGetter = powerTextGetter;
        }
        if (cursorPosition.y == 3) {
            textGetter = protectionTextGetter;
        }
        if (textGetter != null) {
            AppLogic.currentGame.getHero().stats.accept(textGetter);
            return textGetter.getText();
        }
        return InventoryText.EMPTY_TEXT;
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
                graphics.setForegroundColor(convertColor(mark(InventoryText.TEXT_COLOR)));
            } else {
                graphics.setBackgroundColor(convertColor(BACKGROUND_COLOR));
                graphics.setForegroundColor(convertColor(SYMBOL_COLOR));
                graphics.setCharacter(inventoryWindowLocation.x + location.x,
                        i + inventoryWindowLocation.y + location.y, chars.get(i));
                graphics.setForegroundColor(convertColor(InventoryText.TEXT_COLOR));
            }
            int n = 0;
            switch (i) {
                case 0:
                    n = AppLogic.currentGame.getHero().stats.getLevel();
                    break;
                case 1:
                    n = AppLogic.currentGame.getHero().stats.getHealth();
                    break;
                case 2:
                    n = AppLogic.currentGame.getHero().stats.getPower();
                    break;
                case 3:
                    n = AppLogic.currentGame.getHero().stats.getProtection();
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

