package renderer;

import com.googlecode.lanterna.TextColor;
import map.LogicPixel;

public class PixelsVisual {
    public static Renderer.PixelData getPixelData(LogicPixel pixel) {
        switch (pixel) {
            case HERO:
                return new Renderer.PixelData(10, 10, '+', COLOR10, null);

            case WATER_WALL:
                return new Renderer.PixelData(1, 1, '#', COLOR5, COLOR17);
            case WATER_BACKGROUND_EMPTY:
                return new Renderer.PixelData(-10, -10, ' ', null, COLOR13);
            case WATER_BACKGROUND_1:
                return new Renderer.PixelData(-10, -10, '`', COLOR5, COLOR13);
            case WATER_BACKGROUND_2:
                return new Renderer.PixelData(-10, -10, '~', COLOR5, COLOR13);

            case GRASS_WALL:
                return new Renderer.PixelData(1, 1, 'A', COLOR12, COLOR4);
            case GRASS_BACKGROUND_EMPTY:
                return new Renderer.PixelData(-10, -10, '`', null, COLOR4);
            case GRASS_BACKGROUND_1:
                return new Renderer.PixelData(-10, -10, '\"', TextColor.ANSI.GREEN, COLOR4);
            case GRASS_BACKGROUND_2:
                return new Renderer.PixelData(-10, -10, '`', TextColor.ANSI.GREEN, COLOR4);

            case SAND_WALL:
                return new Renderer.PixelData(1, 1, '#', COLOR6, COLOR3);
            case SAND_BACKGROUND_EMPTY:
                return new Renderer.PixelData(-10, -10, 'N', null, COLOR2);
            case SAND_BACKGROUND_1:
                return new Renderer.PixelData(-10, -10, ',', COLOR12, COLOR2);
            case SAND_BACKGROUND_2:
                return new Renderer.PixelData(-10, -10, '+', COLOR12, COLOR2);
            case SAND_BACKGROUND_3:
                return new Renderer.PixelData(-10, -10, 'N', COLOR3, COLOR2);

            case DUNGEON_WALL:
                return new Renderer.PixelData(1, 1, '*', TextColor.ANSI.RED, COLOR15);
            case DUNGEON_BACKGROUND_EMPTY:
                return new Renderer.PixelData(-10, -10, '`', null, COLOR14);
            case DUNGEON_BACKGROUND_1:
                return new Renderer.PixelData(-10, -10, '-', COLOR15, COLOR14);
            case DUNGEON_BACKGROUND_2:
                return new Renderer.PixelData(-10, -10, '=', COLOR15, COLOR14);

            default:
                return null;
        }
    }

    static TextColor COLOR1 = new TextColor.RGB(0, 0, 102);
    static TextColor COLOR2 = new TextColor.RGB(255, 255, 204);
    static TextColor COLOR3 = new TextColor.RGB(255, 255, 153);
    static TextColor COLOR4 = new TextColor.RGB(204, 255, 153);
    static TextColor COLOR5 = new TextColor.RGB(204, 255, 204);
    static TextColor COLOR6 = new TextColor.RGB(102, 51, 0);
    static TextColor COLOR7 = new TextColor.RGB(204, 153, 0);
    static TextColor COLOR8 = new TextColor.RGB(153, 153, 102);
    static TextColor COLOR9 = new TextColor.RGB(204, 51, 0);
    static TextColor COLOR10 = new TextColor.RGB(153, 255, 102);
    static TextColor COLOR11 = new TextColor.RGB(102, 153, 0);
    static TextColor COLOR12 = new TextColor.RGB(0, 153, 51);
    static TextColor COLOR13 = new TextColor.RGB(0, 153, 255);
    static TextColor COLOR14 = new TextColor.RGB(26, 13, 0);
    static TextColor COLOR15 = new TextColor.RGB(204, 51, 0);
    static TextColor COLOR16 = new TextColor.RGB(0, 153, 255);
    static TextColor COLOR17 = new TextColor.RGB(255, 255, 255);
}
