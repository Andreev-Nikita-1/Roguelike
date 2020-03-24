package renderer;

import basicComponents.Controller;
import basicComponents.GameplayLogic;
import map.MapOfObjects;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import map.Coord;
import map.LogicPixel;
import map.objects.Object;
import map.objects.StaticObject;

import java.util.*;


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

        int xSize = Math.min(Controller.getTerminalSizeX(), MapOfObjects.xSize);
        int ySize = Math.min(Controller.getTerminalSizeY(), MapOfObjects.ySize);
        int xLeftUp = getFrame(GameplayLogic.heroObject.getLocation().x, xSize, MapOfObjects.xSize);
        int yLeftUp = getFrame(GameplayLogic.heroObject.getLocation().y, ySize, MapOfObjects.ySize);

        mergePixelsInsideFrame(xSize, ySize, xLeftUp, yLeftUp);

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                Pixel pixel = pixelStacks[i + xLeftUp][j + yLeftUp].getPixel();
                graphics.setCharacter(i, j, new TextCharacter(pixel.symbol,
                        pixel.symbolColor, pixel.backgroundColor));
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
        pixelStacks = new PixelStack[MapOfObjects.xSize][MapOfObjects.ySize];
        for (int i = 0; i < MapOfObjects.xSize; i++) {
            for (int j = 0; j < MapOfObjects.ySize; j++) {
                pixelStacks[i][j] = new PixelStack();
            }
        }
        for (Object object : GameplayLogic.objects) {
            if (object instanceof StaticObject) {
                Map<Coord, LogicPixel> mappingPixels = object.getPixels(Coord.ZERO, new Coord(MapOfObjects.xSize, MapOfObjects.ySize));
                for (Coord c : mappingPixels.keySet()) {
                    PixelData[] pixelDatas = PixelsVisual.getPixelData(mappingPixels.get(c));
                    for (PixelData pixelData : pixelDatas) {
                        pixelStacks[c.x][c.y].insertStaticPixel(pixelData);
                    }
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

        private List<PixelData> staticObjectsStack = new ArrayList<>();
        private List<PixelData> currentStack = new ArrayList<>();
        private Pixel staticPixel = null;
        private boolean changed = false;

        public void insert(PixelData pixelData) {
            currentStack.add(pixelData);
            changed = true;
        }

        public void insertStaticPixel(PixelData pixelData) {
            staticObjectsStack.add(pixelData);
        }

        public void reset() {
            changed = false;
            currentStack = new ArrayList<>(staticObjectsStack);
        }

        public Pixel getPixel() {
            if (staticPixel == null) {
                staticPixel = overlay(staticObjectsStack);
            }
            if (!changed) {
                return staticPixel;
            } else {
                return overlay(currentStack);
            }
        }

        public static Pixel overlay(List<PixelData> stack) {
            double r1 = 0, g1 = 0, b1 = 0;
            double r2 = 0, g2 = 0, b2 = 0;
            double k1 = 1;
            double k2 = 1;
            char symbol = ' ';
            boolean symbolChosen = false;
            Collections.sort(stack);
            for (int i = stack.size() - 1; i >= 0; i--) {
                PixelData pixelData = stack.get(i);
                int r = pixelData.color.toColor().getRed();
                int g = pixelData.color.toColor().getGreen();
                int b = pixelData.color.toColor().getBlue();
                double t = pixelData.transparency;
                r1 += t * k1 * r;
                g1 += t * k1 * g;
                b1 += t * k1 * b;
                k1 *= (1 - t);
                if (!symbolChosen && pixelData.isSymbol) {
                    symbol = pixelData.symbol;
                    symbolChosen = true;
                }
                if (!pixelData.isSymbol) {
                    r2 += t * k2 * r;
                    g2 += t * k2 * g;
                    b2 += t * k2 * b;
                    k2 *= (1 - t);
                }
            }
            TextColor symbolColor = new TextColor.RGB((int) r1, (int) g1, (int) b1);
            TextColor backgroundColor = new TextColor.RGB((int) r2, (int) g2, (int) b2);
            return new Pixel(symbol, symbolColor, backgroundColor);
        }

    }

    static class Pixel {
        public final char symbol;
        public final TextColor symbolColor;
        public final TextColor backgroundColor;

        public Pixel(char symbol, TextColor symbolColor, TextColor backgroundColor) {
            this.symbol = symbol;
            this.symbolColor = symbolColor;
            this.backgroundColor = backgroundColor;
        }
    }

    public static class PixelData implements Comparable<PixelData> {
        public final boolean isSymbol;
        public final int level;
        public final TextColor color;
        public final double transparency;
        public final char symbol;

        PixelData(boolean isSymbol,
                  int level,
                  TextColor color,
                  double transparency,
                  char symbol
        ) {
            this.isSymbol = isSymbol;
            this.level = level;
            this.color = color;
            this.transparency = transparency;
            this.symbol = symbol;
        }

        @Override
        public int compareTo(Renderer.PixelData o) {
            return level - o.level;
        }
    }


}
