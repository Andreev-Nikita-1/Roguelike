package renderer;

import basicComponents.Controller;
import basicComponents.GameplayLogic;
import basicComponents.MapOfFirmObjects;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import map.Coord;
import map.LogicPixel;
import map.objects.Object;
import map.objects.StaticObject;

import java.util.Map;


public class Renderer {

    private static PixelStack[][] pixelStacks;
    private static boolean pixelStacksInitialized = false;

    public static void reset() {
        pixelStacksInitialized = false;
    }

    public static void render(TextGUIGraphics graphics) {
        switch (GameplayLogic.gameplayState) {
            case NOT_STARTED:
                drawStartPicture(graphics);
                break;
            case PAUSED:
            case PLAYING:
                drawMap(graphics);
                break;
        }
    }

    private static void drawMap(TextGUIGraphics graphics) {


        if (!pixelStacksInitialized) {
            pixelStacksInitialize();
        }

        int xSize = Math.min(Controller.getTerminalSizeX(), MapOfFirmObjects.xSize);
        int ySize = Math.min(Controller.getTerminalSizeY(), MapOfFirmObjects.ySize);
        int xLeftUp = getFrame(GameplayLogic.heroObject.getLocation().x, xSize, MapOfFirmObjects.xSize);
        int yLeftUp = getFrame(GameplayLogic.heroObject.getLocation().y, ySize, MapOfFirmObjects.ySize);

        mergePixelsInsideFrame(xSize, ySize, xLeftUp, yLeftUp);

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                PixelData pixelData = pixelStacks[i + xLeftUp][j + yLeftUp].currentPixel;
                graphics.setCharacter(i, j, new TextCharacter(pixelData.symbol,
                        pixelData.symbolColor, pixelData.backgroundColor));
            }
        }
    }

    private static int getFrame(int location, int size, int maxSize) {
        int a = size / 2;
        int b = size - a;
        if (location - a < 0) {
            return 0;
        }
        if (location + b >= maxSize) {
            return maxSize - size;
        }
        return location - a;
    }

    private static void pixelStacksInitialize() {
        pixelStacks = new PixelStack[MapOfFirmObjects.xSize][MapOfFirmObjects.ySize];
        for (int i = 0; i < MapOfFirmObjects.xSize; i++) {
            for (int j = 0; j < MapOfFirmObjects.ySize; j++) {
                pixelStacks[i][j] = new PixelStack();
            }
        }
        for (Object object : GameplayLogic.objects) {
            if (object instanceof StaticObject) {
                Map<Coord, LogicPixel> mappingPixels = object.getPixels();
                for (Coord c : mappingPixels.keySet()) {
                    pixelStacks[c.x][c.y].mergeStatic(PixelsVisual.getPixelData(mappingPixels.get(c)));
                }
            }
        }
        pixelStacksInitialized = true;
    }

    private static void mergePixelsInsideFrame(int xSize, int ySize, int xLeftUp, int yLeftUp) {
        for (int i = xLeftUp; i < xLeftUp + xSize; i++) {
            for (int j = yLeftUp; j < yLeftUp + ySize; j++) {
                pixelStacks[i][j].reset();
            }
        }
        for (Object object : GameplayLogic.objects) {
            if (!(object instanceof StaticObject)) {
                Map<Coord, LogicPixel> mappingPixels = object.getPixels();
                for (Coord c : mappingPixels.keySet()) {
                    if (c.x >= xLeftUp && c.x < xLeftUp + xSize &&
                            c.y >= yLeftUp && c.y < yLeftUp + ySize)
                        pixelStacks[c.x][c.y].merge(PixelsVisual.getPixelData(mappingPixels.get(c)));
                }
            }
        }
    }

    private static void drawStartPicture(TextGUIGraphics graphics) {
        for (int i = 0; i < Controller.getTerminalSizeX(); i++) {
            for (int j = 0; j < Controller.getTerminalSizeY(); j++) {
                TextColor color1 = TextColor.ANSI.BLUE;
                TextColor color2 = TextColor.ANSI.CYAN;
                if ((i + j) % 2 == 0) {
                    color1 = color2;
                }
                graphics.setCharacter(i, j, new TextCharacter('#', color1, color2));
            }
        }
    }

    private static class PixelStack {
        private PixelData staticPixel;
        private PixelData currentPixel;

        PixelStack() {
            staticPixel = new PixelData();
            currentPixel = new PixelData();
        }

        public void reset() {
            currentPixel = new PixelData(staticPixel);
        }

        public void merge(PixelData pixelData) {
            currentPixel.merge(pixelData);
        }

        public void mergeStatic(PixelData pixelData) {
            staticPixel.merge(pixelData);
        }

    }

    static class PixelData {
        public int level;
        public int backgroundLevel;
        public char symbol;
        TextColor symbolColor;
        TextColor backgroundColor;

        PixelData() {
            symbol = ' ';
            level = -1000;
            backgroundLevel = -1000;
        }

        PixelData(PixelData other) {
            level = other.level;
            backgroundLevel = other.backgroundLevel;
            symbol = other.symbol;
            symbolColor = other.symbolColor;
            backgroundColor = other.backgroundColor;
        }

        PixelData(int level, int backgroundLevel, char symbol, TextColor symbolColor, TextColor backgroundColor) {
            this.level = level;
            this.backgroundLevel = backgroundLevel;
            this.symbol = symbol;
            this.symbolColor = symbolColor;
            this.backgroundColor = backgroundColor;
        }

        public void merge(PixelData other) {
            if (other.symbolColor != null &&
                    (symbolColor == null || other.level > this.level)) {
                level = other.level;
                symbol = other.symbol;
                symbolColor = other.symbolColor;
            }
            if (other.backgroundColor != null
                    && (backgroundColor == null || other.backgroundLevel > backgroundLevel)) {
                backgroundColor = other.backgroundColor;
                backgroundLevel = other.backgroundLevel;
            }
        }

    }


}
