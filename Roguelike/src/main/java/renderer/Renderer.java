package renderer;

import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

import renderer.inventoryWindow.InventoryWindow;
import util.Util;

import java.awt.Color;

import static renderer.VisualPixel.*;


public class Renderer {

    public static volatile int page = 0;

    public static void render(TextGUIGraphics graphics) {
        switch (AppLogic.currentGame.gameplayState) {
            case NOT_STARTED:
            case MAP_GENERATING:
                drawStartPicture(graphics);
                break;
            case INVENTORY:
                AppLogic.currentGame.currentMapRenderer.drawMap(graphics, Controller.getTerminalSizeX(), Controller.getTerminalSizeY());
                drawInventoryWindow(graphics);
                break;
            case PAUSED:
            case PLAYING:
                AppLogic.currentGame.currentMapRenderer.drawMap(graphics, Controller.getTerminalSizeX(), Controller.getTerminalSizeY());
                break;
        }
    }


    public static void drawInventoryWindow(TextGUIGraphics graphics) {
        InventoryWindow.draw(graphics);
    }


    private static void drawStartPicture(TextGUIGraphics graphics) {
        for (int i = 0; i < Controller.getTerminalSizeX(); i++) {
            for (int j = 0; j < Controller.getTerminalSizeY(); j++) {
                Color color1 = COLOR23;
                Color color2 = COLOR22;
                if ((i + j) % 2 == 0) {
                    color1 = color2;
                }
                graphics.setCharacter(i, j, new TextCharacter('#', Util.convertColor(color1), Util.convertColor(color2)));
            }
        }
    }
//TODO
//    private static void drawLoading(TextGUIGraphics graphics, double percent) {
//        graphics.setBackgroundColor(convertColor(Color.BLACK));
//        graphics.fill(' ');
//        int xSize = Controller.getTerminalSizeX();
//        int ySize = Controller.getTerminalSizeY();
//        int percentInt = (int) (percent * 100 + 1);
//        String text = "CREATING MAP " + String.valueOf(percentInt) + "%";
//        if (percentInt <= 50) {
//            graphics.setBackgroundColor(convertColor(Color.Bla));
//            graphics.setForegroundColor(new Color(255, 255, 255));
//            int ind = (int) (percent * 32) + 1;
//            graphics.putString((int) (xSize / 2) - 8, (int) (ySize / 2), text.substring(0, ind));
//            graphics.setBackgroundColor(new Color(255, 255, 255));
//            graphics.setForegroundColor(new Color(0, 0, 0));
//            graphics.putString((int) (xSize / 2) - 8 + ind, (int) (ySize / 2), text.substring(ind));
//        } else if (percentInt <= 99) {
//            graphics.setBackgroundColor(new Color(0, 0, 0));
//            graphics.setForegroundColor(new Color(255, 255, 255));
//            int ind = (int) ((percent - 0.5) * 24);
//            graphics.putString((int) (xSize / 2) - 8 + ind, (int) (ySize / 2), text.substring(ind));
//        } else {
//            graphics.setBackgroundColor(new Color(0, 0, 0));
//            graphics.setForegroundColor(new Color(255, 255, 255));
//            graphics.putString((int) (xSize / 2) + 4, (int) (ySize / 2), "100%");
//        }
//    }
}
