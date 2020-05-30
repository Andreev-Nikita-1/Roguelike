package renderer;

import basicComponents.AppLogic;
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

/**
 * Class, that renders a map
 */
public class MapRenderer {
    private Game game;
    private MapOfObjects map;
    private PixelStack[][] pixelStacks;
    private TerminalCoordinate terminalCoordinate;

    public MapRenderer(MapOfObjects map) {
        this.map = map;
        pixelStacks = new PixelStack[map.xSize][map.ySize];
        terminalCoordinate = new TerminalCoordinate(new Coord(map.xSize, map.ySize));
    }

    /**
     * Draws map
     */
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
                        .noir(AppLogic.gameplayState == AppLogic.GameplayState.PAUSED
                                || AppLogic.gameplayState == AppLogic.GameplayState.INVENTORY);
                graphics.setCharacter(i, j, new TextCharacter(pixel.symbol,
                        convertColor(pixel.symbolColor), convertColor(pixel.backgroundColor)));
            }
        }
    }

    /**
     * Gets visual from static objects
     */
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
