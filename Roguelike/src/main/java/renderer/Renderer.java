package renderer;

import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

import map.roomSystem.textures.VisualPixelGenerator;
import renderer.inventoryWindow.InventoryWindow;
import util.Util;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class, responsible for rendering map and inventory
 */
public class Renderer {

    public static volatile int page = 0;

    /**
     * Draws picture
     */
    public static void render(TextGUIGraphics graphics) {
        switch (AppLogic.gameplayState) {
            case NOT_STARTED:
            case MAP_GENERATING:
            case KILLED:
                drawStartPicture(graphics);
                break;
            case INVENTORY:
                AppLogic.currentGame.mapRenderer.drawMap(graphics, Controller.getTerminalSizeX(), Controller.getTerminalSizeY());
                InventoryWindow.draw(graphics);
                break;
            case PAUSED:
            case PLAYING:
                AppLogic.currentGame.mapRenderer.drawMap(graphics, Controller.getTerminalSizeX(), Controller.getTerminalSizeY());
                TopBar.draw(graphics);
                break;
        }
    }

    /**
     * Draws start background
     */
    private static void drawStartPicture(TextGUIGraphics graphics) {
        Color SYMBOL_COLOR = new Color(40, 40, 40);
        Color BACK_COLOR = new Color(20, 20, 20);
        Random random = new Random(0);
        for (int i = 0; i < Controller.getTerminalSizeX(); i++) {
            for (int j = 0; j < Controller.getTerminalSizeY(); j++) {
                char symbol = (char) (0x01A7 + random.nextDouble() * 5);
                graphics.setCharacter(i, j, new TextCharacter(symbol,
                        Util.convertColor(SYMBOL_COLOR),
                        Util.convertColor(BACK_COLOR)));
            }
        }
    }
}
