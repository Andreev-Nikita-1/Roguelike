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

import static renderer.PixelsVisual.COLOR3;
import static renderer.PixelsVisual.COLOR4;


public class Renderer {

    private static PixelStack[][] pixelStacks;
    private static TerminalCoord terminalCoord = new TerminalCoord();

    public static double percent = 0;

    public static void render(TextGUIGraphics graphics) {
        switch (GameplayLogic.gameplayState) {
            case NOT_STARTED:
                drawStartPicture(graphics);
                break;
            case MAP_GENERATING:
                drawLoading(graphics, percent);
                break;
            case PAUSED:
            case PLAYING:
                drawMap(graphics);
                break;
        }
    }

    private static void drawMap(TextGUIGraphics graphics) {
        int xSize = Math.min(Controller.getTerminalSizeX(), MapOfObjects.xSize);
        int ySize = Math.min(Controller.getTerminalSizeY(), MapOfObjects.ySize);
        Coord leftUp = terminalCoord.getLeftUp(xSize, ySize, new Coord(GameplayLogic.heroObject.getLocation()));
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

    public static void fit() {
        pixelStacksInitialize();
        terminalCoord.setFrame(Math.min(Controller.getTerminalSizeX(), MapOfObjects.xSize),
                Math.min(Controller.getTerminalSizeY(), MapOfObjects.ySize),
                new Coord(GameplayLogic.heroObject.getLocation()));
    }

    private static void pixelStacksInitialize() {
        pixelStacks = new PixelStack[MapOfObjects.xSize][MapOfObjects.ySize];
        for (int i = 0; i < MapOfObjects.xSize; i++) {
            for (int j = 0; j < MapOfObjects.ySize; j++) {
                Renderer.percent = 0.6 + ((double) i * MapOfObjects.ySize + j) / (MapOfObjects.xSize * MapOfObjects.ySize) / 5;
                pixelStacks[i][j] = new PixelStack();
            }
        }

        for (int i = 0; i < GameplayLogic.objects.size(); i++) {
            Object object = GameplayLogic.objects.get(i);
            Renderer.percent = 0.8 + (double) i / GameplayLogic.objects.size() / 5;
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
        for (int i = 0; i < MapOfObjects.xSize; i++) {
            for (int j = 0; j < MapOfObjects.ySize; j++) {
                pixelStacks[i][j].fitStaticPixel();
            }
        }
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
                TextColor color1 = COLOR4;
                TextColor color2 = COLOR3;
                if ((i + j) % 2 == 0) {
                    color1 = color2;
                }
                graphics.setCharacter(i, j, new TextCharacter('#', color1, color2));
            }
        }
    }

    private static void drawLoading(TextGUIGraphics graphics, double percent) {
        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
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

    private static class TerminalCoord {

        private Coord coord = new Coord(Coord.ZERO);
        private Coord size = new Coord(Coord.ZERO);
        private Coord heroRelative = new Coord(Coord.ZERO);
        private Coord treshold = new Coord(Coord.ZERO);
        private Coord heroPred = new Coord(Coord.ZERO);
        private double trasholdCoeff = 0.4;

        public Coord getLeftUp(int newXSize, int newYSize, Coord heroLocation) {
            if (size.x != newXSize || size.y != newYSize) {
                setFrame(newXSize, newYSize, heroLocation);
            } else {
                update(heroLocation);
            }
            return coord;
        }

        private void update(Coord heroNew) {
            Coord diff = new Coord(heroNew.x - heroPred.x, heroNew.y - heroPred.y);
            if (diff.x == 0 && diff.y == 0) {
                return;
            }
            Coord heroRelativeNew = heroRelative.shifted(diff);
            if (heroRelativeNew.x >= treshold.x &&
                    heroRelativeNew.x <= size.x - treshold.x ||
                    heroRelativeNew.x < treshold.x && coord.x == 0 ||
                    heroRelativeNew.x > size.x - treshold.x && size.x + coord.x == MapOfObjects.xSize
            ) {
                heroRelative.x = heroRelativeNew.x;
            } else {
                coord.x += diff.x;
                if (coord.x < 0) {
                    coord.x = 0;
                }
                if (coord.x + size.x > MapOfObjects.xSize) {
                    coord.x = MapOfObjects.xSize - size.x;
                }
            }
            if (heroRelativeNew.y >= treshold.y &&
                    heroRelativeNew.y <= size.y - treshold.y ||
                    heroRelativeNew.y < treshold.y && coord.y == 0 ||
                    heroRelativeNew.y > size.y - treshold.y && size.y + coord.y == MapOfObjects.ySize
            ) {
                heroRelative.y = heroRelativeNew.y;
            } else {
                coord.y += diff.y;
                if (coord.y < 0) {
                    coord.y = 0;
                }
                if (coord.y + size.y > MapOfObjects.ySize) {
                    coord.y = MapOfObjects.ySize - size.y;
                }
            }
            heroPred = heroNew;
        }

        public void setFrame(int newXSize, int newYSize, Coord heroLocation) {
            size = new Coord(newXSize, newYSize);
            treshold = new Coord((int) (trasholdCoeff * newXSize), (int) (trasholdCoeff * newYSize));
            heroPred = heroLocation;
            int x = centralOneDimFrame(newXSize, MapOfObjects.xSize, heroLocation.x);
            int y = centralOneDimFrame(newYSize, MapOfObjects.ySize, heroLocation.y);
            coord = new Coord(x, y);
            heroRelative = new Coord(heroLocation.x - x, heroLocation.y - y);
        }

        private static int centralOneDimFrame(int size, int maxSize, int location) {
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

        public void fitStaticPixel() {
            staticPixel = overlay(staticObjectsStack);
        }

        public Pixel getPixel() {
            if (!changed) {
                return staticPixel;
            } else {
                return overlay(currentStack);
            }
        }

        private static Pixel overlay(List<PixelData> stack) {
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
