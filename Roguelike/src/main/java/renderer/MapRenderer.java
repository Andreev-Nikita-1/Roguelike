package renderer;

import basicComponents.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import objects.DynamicVisualObject;
import objects.StaticVisualObject;
import util.Coord;
import map.MapOfObjects;

import java.util.List;
import java.util.Map;

import static util.Util.convertColor;


public class MapRenderer {
    private Game game;
    private MapOfObjects map;
    private PixelStack[][] pixelStacks;
    private TerminalCoordinate terminalCoordinate;

    public MapRenderer(Game game) {
        this.game = game;
        this.map = game.currentMap;
        pixelStacks = new PixelStack[map.xSize][map.ySize];
        terminalCoordinate = new TerminalCoordinate(new Coord(map.xSize, map.ySize));
    }

    private void showUnicode(TextGUIGraphics graphics) {
        graphics.setBackgroundColor(new TextColor.RGB(255, 255, 255));
        graphics.setForegroundColor(new TextColor.RGB(0, 0, 0));
        graphics.fill(' ');
        TextColor color1 = new TextColor.RGB(0, 0, 0);
        TextColor color2 = new TextColor.RGB(200, 200, 200);
        int i, j;
        for (j = 0; j < 20; j++) {
            graphics.putString(j + 6, 0, String.valueOf(j % 10));
        }
        char k = (char) (40 + Renderer.page * 200);
        for (i = 0; i < 10; i++) {
            graphics.putString(0, i + 1, String.valueOf((int) k));
            for (j = 0; j < 20; j++) {
                if (k >= 32 && k != 7 * 16 + 15) {
                    if ((i + j) % 2 == 0)
                        graphics.setCharacter(j + 6, i + 1, new TextCharacter(k, color1, color2));
                    else
                        graphics.setCharacter(j + 6, i + 1, new TextCharacter(k, color2, color1));
                }
                k++;
            }
        }
    }

    public void drawMap(TextGUIGraphics graphics, int terminalSizeX, int terminalSizeY) {
        int xSize = Math.min(terminalSizeX, map.xSize);
        int ySize = Math.min(terminalSizeY, map.ySize);
        Coord leftUp = terminalCoordinate.getLeftUp(xSize, ySize, map.getHeroLocation());
        int xLeftUp = leftUp.x;
        int yLeftUp = leftUp.y;

        mergePixelsInsideFrame(xSize, ySize, xLeftUp, yLeftUp);

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                Pixel pixel = pixelStacks[i + xLeftUp][j + yLeftUp].getPixel()
                        .darkness(!map.lighting.lighted)
                        .noir(game.gameplayState == Game.GameplayState.PAUSED
                                || game.gameplayState == Game.GameplayState.INVENTORY);
                graphics.setCharacter(i, j, new TextCharacter(pixel.symbol,
                        convertColor(pixel.symbolColor), convertColor(pixel.backgroundColor)));
            }
        }
//        graphics.putString(0, 0, String.valueOf((char) ((map.heroObject.hero.health.get() < 20) ? 57356 : 57355)) + String.valueOf(map.heroObject.hero.health.get()));
        graphics.putString(0, 0, String.valueOf(game.currentInventory.stats.getHealth()));
        graphics.putString(5, 0, String.valueOf(game.currentInventory.stats.getStamina()));
        try {
            graphics.putString(10, 0, String.valueOf(game.currentInventory.weapon.durability));
        } catch (NullPointerException e) {
        }
//        graphics.setBackgroundColor(Renderer.convertColor(new Color(10, 10, 10)));
//        graphics.setForegroundColor(Renderer.convertColor(new Color(50, 50, 50)));
//        graphics.putString(0, 0, (char) (0x01DE) + "" + (char) (0x01AB) + "" + (char) (0x01AB) + "" + (char) (0x01AB1) + "" + (char) (0x01DF));
    }

    public MapRenderer fit() {
        pixelStacksInitialize();
        return this;
    }

    private void pixelStacksInitialize() {
        pixelStacks = new PixelStack[map.xSize][map.ySize];
        for (int i = 0; i < map.xSize; i++) {
            for (int j = 0; j < map.ySize; j++) {
                pixelStacks[i][j] = new PixelStack();
            }
        }
        for (StaticVisualObject object : map.staticObjects) {
            Map<Coord, VisualPixel> mappingPixels = object.getPixels(Coord.ZERO, new Coord(map.xSize, map.ySize));
            for (Coord c : mappingPixels.keySet()) {
                List<PixelData> pixelDatas = mappingPixels.get(c).getPixelDataList();
                for (PixelData pixelData : pixelDatas) {
                    pixelStacks[c.x][c.y].insertStaticPixel(pixelData);
                }
            }
        }
        for (int i = 0; i < map.xSize; i++) {
            for (int j = 0; j < map.ySize; j++) {
                pixelStacks[i][j].fitStaticPixel();
            }
        }
    }

    private void mergePixelsInsideFrame(int xSize, int ySize, int xLeftUp, int yLeftUp) {
        for (int i = xLeftUp; i < xLeftUp + xSize; i++) {
            for (int j = yLeftUp; j < yLeftUp + ySize; j++) {
                pixelStacks[i][j].reset();
            }
        }
        for (DynamicVisualObject object : map.dynamicObjects) {
            Map<Coord, VisualPixel> mappingPixels = object.getPixels(new Coord(xLeftUp, yLeftUp), new Coord(xLeftUp + xSize, yLeftUp + ySize));
            for (Coord c : mappingPixels.keySet()) {
                if (c.x >= xLeftUp && c.x < xLeftUp + xSize &&
                        c.y >= yLeftUp && c.y < yLeftUp + ySize) {
                    List<PixelData> pixelDatas = mappingPixels.get(c).getPixelDataList();
                    for (PixelData pixelData : pixelDatas) {
                        pixelStacks[c.x][c.y].insert(pixelData);

                    }
                }
            }
        }
    }

}
