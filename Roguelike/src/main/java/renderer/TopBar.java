package renderer;

import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.items.Item;
import hero.stats.HeroStats;
import renderer.inventoryWindow.TileWindow;
import util.Util;

import java.awt.*;

import static util.Util.convertColor;

public class TopBar {
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color HEALTH_COLOR = new Color(150, 0, 0);
    private static final Color STAMINA_COLOR = new Color(0, 50, 150);

    private static char rightScale(double level) {
        return (char) (0x037D + (int) (level * 49));
    }

    public static void draw(TextGUIGraphics graphics) {
        int xMax = Controller.getTerminalSizeX();
        graphics.setBackgroundColor(convertColor(BACKGROUND_COLOR));
        for (int i = 0; i < xMax; i++) {
            graphics.setCharacter(i, 0, ' ');
        }

        HeroStats stats = AppLogic.currentGame.hero.stats;
        int health = stats.getHealth();
        String healthBar = Util.horizontalScale(13, Math.max(0, health / 1300.0));
        graphics.setForegroundColor(convertColor(HEALTH_COLOR));
        graphics.putString(0, 0, healthBar);

        for (int i = 0; i < 4; i++) {
            Item item = AppLogic.currentGame.hero.inventory.taken[i];
            if (item != null) {
                graphics.setForegroundColor(convertColor(item.getColor()));
                graphics.setCharacter(xMax / 2 - 1 + i, 0, item.getSymbol());
            } else {
                graphics.setForegroundColor(convertColor(TileWindow.EMPTY_SYMBOL_COLOR));
                graphics.setCharacter(xMax / 2 - 1 + i, 0, TileWindow.EMPTY_SYMBOL);
            }
        }


        double level = stats.getStamina() / (double) stats.getMaxStamina();
        if (level < 1 && level > 0) {
            graphics.setForegroundColor(convertColor(STAMINA_COLOR));
            graphics.setCharacter(xMax / 2 - 3, 0, rightScale(level));
        }
    }
}
