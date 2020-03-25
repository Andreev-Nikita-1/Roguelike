package renderer;

import com.googlecode.lanterna.TextColor;
import map.LogicPixel;

import static basicComponents.AppLogic.HERO_SYMBOL;

public class PixelsVisual {
    public static Renderer.PixelData[] getPixelData(LogicPixel pixel) {
        switch (pixel) {
            case HERO:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 10, HERO_COLOR, 1, HERO_SYMBOL)
                };
            case SWORDSMAN:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 10, COLOR2, 1, '@')
                };
            case ATTACK:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, 20, TextColor.ANSI.RED, 0.3, ' ')
                };
            case LIGHT:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, 20, COLOR3, 0.2, ' ')
                };
            case DARKNESS_1:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, 20, TextColor.ANSI.BLACK, 0.5, ' ')
                };
            case DARKNESS_2:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, 20, TextColor.ANSI.BLACK, 0.7, ' ')
                };
            case DARKNESS_3:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, 20, TextColor.ANSI.BLACK, 0.9, ' ')
                };
            case DARKNESS_FULL:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, 20, TextColor.ANSI.BLACK, 1, ' ')
                };
            case WATER_WALL:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 1, COLOR17, 1, '#'),
                        new Renderer.PixelData(false, 0, COLOR5, 1, ' ')
                };

            case WATER_BACKGROUND_EMPTY:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, -10, COLOR13, 1, ' ')
                };
            case WATER_BACKGROUND_1:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR5, 1, '`'),
                        new Renderer.PixelData(false, -10, COLOR13, 1, ' ')
                };
            case WATER_BACKGROUND_2:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR5, 1, '~'),
                        new Renderer.PixelData(false, -10, COLOR13, 1, ' ')
                };

            case GRASS_WALL_1:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 1, COLOR18, 1, '^'),
                        new Renderer.PixelData(false, 0, COLOR4, 1, ' ')
                };
            case GRASS_WALL_2:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 1, COLOR11, 1, 'A'),
                        new Renderer.PixelData(false, 0, COLOR4, 1, ' ')
                };
            case GRASS_BACKGROUND_EMPTY:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, -10, COLOR4, 1, ' ')
                };
            case GRASS_BACKGROUND_1:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, TextColor.ANSI.GREEN, 1, '`'),
                        new Renderer.PixelData(false, -10, COLOR4, 1, ' ')
                };
            case GRASS_BACKGROUND_2:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, TextColor.ANSI.GREEN, 1, '\"'),
                        new Renderer.PixelData(false, -10, COLOR4, 1, ' ')
                };

            case SAND_WALL:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 1, COLOR6, 1, '#'),
                        new Renderer.PixelData(false, 0, COLOR3, 1, ' ')
                };
            case SAND_BACKGROUND_EMPTY:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, -10, COLOR2, 1, ' ')
                };
            case SAND_BACKGROUND_1:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR12, 1, ','),
                        new Renderer.PixelData(false, -10, COLOR2, 1, ' ')
                };
            case SAND_BACKGROUND_2:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR12, 1, '+'),
                        new Renderer.PixelData(false, -10, COLOR2, 1, ' ')
                };
            case SAND_BACKGROUND_3:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR3, 1, 'N'),
                        new Renderer.PixelData(false, -10, COLOR2, 1, ' ')
                };

            case DUNGEON_WALL:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, 1, TextColor.ANSI.RED, 1, '*'),
                        new Renderer.PixelData(false, 0, COLOR15, 1, ' ')
                };
            case DUNGEON_BACKGROUND_EMPTY:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(false, -10, COLOR14, 1, ' ')
                };
            case DUNGEON_BACKGROUND_1:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR15, 1, '-'),
                        new Renderer.PixelData(false, -10, COLOR14, 1, ' ')
                };
            case DUNGEON_BACKGROUND_2:
                return new Renderer.PixelData[]{
                        new Renderer.PixelData(true, -9, COLOR15, 1, '='),
                        new Renderer.PixelData(false, -10, COLOR14, 1, ' ')
                };
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
    static TextColor COLOR18 = new TextColor.RGB(0, 100, 0);


    static TextColor HERO_COLOR = COLOR7;
}
