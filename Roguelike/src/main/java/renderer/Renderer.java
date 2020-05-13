package renderer;

import basicComponents.Controller;
import basicComponents.GameplayLogic;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import inventory.TextWindow;
import inventory.InventoryWindow;
import util.Coord;

import java.util.Arrays;

import static com.googlecode.lanterna.TextColor.ANSI.BLACK;
import static renderer.Colors.*;

public class Renderer {

    public static volatile int page = 0;

    public static void render(TextGUIGraphics graphics) {
        switch (GameplayLogic.gameplayState) {
            case NOT_STARTED:
            case MAP_GENERATING:
                drawStartPicture(graphics);
                break;
            case INVENTORY:
                GameplayLogic.currentMapRenderer.drawMap(graphics, Controller.getTerminalSizeX(), Controller.getTerminalSizeY());
                drawInventoryWindow(graphics);
                break;
            case PAUSED:
            case PLAYING:
                GameplayLogic.currentMapRenderer.drawMap(graphics, Controller.getTerminalSizeX(), Controller.getTerminalSizeY());
                break;
        }
    }


    public static void drawInventoryWindow(TextGUIGraphics graphics) {
        InventoryWindow.draw(graphics);
    }


    private static void drawStartPicture(TextGUIGraphics graphics) {
        for (int i = 0; i < Controller.getTerminalSizeX(); i++) {
            for (int j = 0; j < Controller.getTerminalSizeY(); j++) {
                TextColor color1 = COLOR23;
                TextColor color2 = COLOR22;
                if ((i + j) % 2 == 0) {
                    color1 = color2;
                }
                graphics.setCharacter(i, j, new TextCharacter('#', color1, color2));
            }
        }
    }

    private static void drawLoading(TextGUIGraphics graphics, double percent) {
        graphics.setBackgroundColor(BLACK);
        graphics.fill(' ');
        int xSize = Controller.getTerminalSizeX();
        int ySize = Controller.getTerminalSizeY();
        int percentInt = (int) (percent * 100 + 1);
        String text = "CREATING MAP " + String.valueOf(percentInt) + "%";
        if (percentInt <= 50) {
            graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
            graphics.setForegroundColor(new TextColor.RGB(255, 255, 255));
            int ind = (int) (percent * 32) + 1;
            graphics.putString((int) (xSize / 2) - 8, (int) (ySize / 2), text.substring(0, ind));
            graphics.setBackgroundColor(new TextColor.RGB(255, 255, 255));
            graphics.setForegroundColor(new TextColor.RGB(0, 0, 0));
            graphics.putString((int) (xSize / 2) - 8 + ind, (int) (ySize / 2), text.substring(ind));
        } else if (percentInt <= 99) {
            graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
            graphics.setForegroundColor(new TextColor.RGB(255, 255, 255));
            int ind = (int) ((percent - 0.5) * 24);
            graphics.putString((int) (xSize / 2) - 8 + ind, (int) (ySize / 2), text.substring(ind));
        } else {
            graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
            graphics.setForegroundColor(new TextColor.RGB(255, 255, 255));
            graphics.putString((int) (xSize / 2) + 4, (int) (ySize / 2), "100%");
        }
    }
}
