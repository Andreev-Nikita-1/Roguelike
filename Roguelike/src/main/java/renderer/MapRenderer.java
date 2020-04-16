package renderer;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;
import map.LogicPixel;
import map.MapOfObjects;
import map.objects.MapObject;
import map.objects.StaticObject;

import java.util.Map;

public class MapRenderer {
    private MapOfObjects map;
    private PixelStack[][] pixelStacks;
    private TerminalCoordinate terminalCoordinate;

    public MapRenderer(MapOfObjects map) {
        this.map = map;
        pixelStacks = new PixelStack[map.xSize][map.ySize];
        terminalCoordinate = new TerminalCoordinate(new Coord(map.xSize, map.ySize));
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
                Pixel pixel = pixelStacks[i + xLeftUp][j + yLeftUp].getPixel();
                graphics.setCharacter(i, j, new TextCharacter(pixel.symbol,
                        pixel.symbolColor, pixel.backgroundColor));
            }
        }
    }

    public MapRenderer fit() {
        pixelStacksInitialize();
        return this;
    }

    private void pixelStacksInitialize() {
        pixelStacks = new PixelStack[map.xSize][map.ySize];
        for (int i = 0; i < map.xSize; i++) {
            for (int j = 0; j < map.ySize; j++) {
                Renderer.percent = 0.6 + ((double) i * map.ySize + j) / (map.xSize * map.ySize) / 5;
                pixelStacks[i][j] = new PixelStack();
            }
        }

        for (int i = 0; i < map.objectsList.size(); i++) {
            MapObject object = map.objectsList.get(i);
            Renderer.percent = 0.8 + (double) i / map.objectsList.size() / 5;
            if (object instanceof StaticObject) {
                Map<Coord, LogicPixel> mappingPixels = object.getPixels(Coord.ZERO, new Coord(map.xSize, map.ySize));
                for (Coord c : mappingPixels.keySet()) {
                    PixelData[] pixelDatas = PixelsVisual.getPixelData(mappingPixels.get(c));
                    for (PixelData pixelData : pixelDatas) {
                        pixelStacks[c.x][c.y].insertStaticPixel(pixelData);
                    }
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
        for (MapObject object : map.objectsList) {
            if (!(object instanceof StaticObject)) {
                Map<Coord, LogicPixel> mappingPixels = object.getPixels(new Coord(xLeftUp, yLeftUp), new Coord(xLeftUp + xSize, yLeftUp + ySize));
                for (Coord c : mappingPixels.keySet()) {
                    if (c.x >= xLeftUp && c.x < xLeftUp + xSize &&
                            c.y >= yLeftUp && c.y < yLeftUp + ySize) {
                        PixelData[] pixelDatas = PixelsVisual.getPixelData(mappingPixels.get(c));
                        for (PixelData pixelData : pixelDatas) {
                            pixelStacks[c.x][c.y].insert(pixelData);
                        }
                    }
                }
            }
        }
    }

}
