package renderer;

import com.googlecode.lanterna.TextColor;
import map.LogicPixel;

import static basicComponents.AppLogic.HERO_SYMBOL;

public class PixelsVisual {
    public static TextColor COLOR1 = new TextColor.RGB(0, 0, 102);
    public static TextColor COLOR2 = new TextColor.RGB(255, 255, 204);
    public static TextColor COLOR3 = new TextColor.RGB(255, 255, 153);
    public static TextColor COLOR4 = new TextColor.RGB(225, 255, 175);
    public static TextColor COLOR5 = new TextColor.RGB(204, 255, 204);
    public static TextColor COLOR6 = new TextColor.RGB(102, 51, 0);
    public static TextColor COLOR7 = new TextColor.RGB(204, 153, 0);
    public static TextColor COLOR8 = new TextColor.RGB(153, 153, 102);
    public static TextColor COLOR9 = new TextColor.RGB(204, 51, 0);
    public static TextColor COLOR10 = new TextColor.RGB(153, 255, 102);
    public static TextColor COLOR11 = new TextColor.RGB(102, 153, 0);
    public static TextColor COLOR12 = new TextColor.RGB(0, 153, 51);
    public static TextColor COLOR13 = new TextColor.RGB(0, 153, 255);
    public static TextColor COLOR14 = new TextColor.RGB(26, 13, 0);
    public static TextColor COLOR15 = new TextColor.RGB(204, 51, 0);
    public static TextColor COLOR16 = new TextColor.RGB(0, 153, 255);
    public static TextColor COLOR17 = new TextColor.RGB(255, 255, 255);
    public static TextColor COLOR18 = new TextColor.RGB(0, 100, 0);
    public static TextColor COLOR19 = new TextColor.RGB(95, 77, 57);
    public static TextColor COLOR20 = new TextColor.RGB(60, 40, 30);
    public static TextColor COLOR21 = new TextColor.RGB(10, 9, 8);
    public static TextColor COLOR22 = new TextColor.RGB(19, 21, 22);
    public static TextColor COLOR23 = new TextColor.RGB(22, 32, 21);
    public static TextColor COLOR24 = new TextColor.RGB(30, 48, 52);
    public static TextColor COLOR25 = new TextColor.RGB(26, 37, 39);
    public static TextColor HERO_COLOR = COLOR9;
    public static TextColor SWORDMEN_COLOR = COLOR6;

    public static PixelData[] HERO = new PixelData[]{new PixelData(true, 10, HERO_COLOR, 1, HERO_SYMBOL)};
    public static PixelData[] SWORDMAN = new PixelData[]{new PixelData(true, 10, SWORDMEN_COLOR, 1, '@')};
    public static PixelData[] ATTACK = new PixelData[]{new PixelData(false, 20, TextColor.ANSI.RED, 0.3, ' ')};
    public static PixelData[] LIGHT_1 = new PixelData[]{new PixelData(false, 2, COLOR3, 0.2, ' ')};
    public static PixelData[] LIGHT_2 = new PixelData[]{new PixelData(false, 2, COLOR3, 0.1, ' ')};
    public static PixelData[] LIGHT_3 = new PixelData[]{new PixelData(false, 2, COLOR3, 0.05, ' ')};
    public static PixelData[] DARKNESS_1 = new PixelData[]{new PixelData(false, 20, TextColor.ANSI.BLACK, 0.5, ' ')};
    public static PixelData[] DARKNESS_2 = new PixelData[]{new PixelData(false, 20, TextColor.ANSI.BLACK, 0.7, ' ')};
    public static PixelData[] DARKNESS_3 = new PixelData[]{new PixelData(false, 20, TextColor.ANSI.BLACK, 0.9, ' ')};
    public static PixelData[] DARKNESS_FULL = new PixelData[]{new PixelData(false, 20, TextColor.ANSI.BLACK, 1, ' ')};
    public static PixelData[] WATER_WALL = new PixelData[]{new PixelData(true, 1, COLOR17, 1, '#'),
            new PixelData(false, 0, COLOR5, 1, ' ')};
    public static PixelData[] WATER_BACKGROUND_EMPTY = new PixelData[]{new PixelData(false, -10, COLOR13, 1, ' ')};
    public static PixelData[] WATER_BACKGROUND_1 = new PixelData[]{new PixelData(true, -9, COLOR5, 1, '`'),
            new PixelData(false, -10, COLOR13, 1, ' ')};
    public static PixelData[] WATER_BACKGROUND_2 = new PixelData[]{new PixelData(true, -9, COLOR5, 1, '~'),
            new PixelData(false, -10, COLOR13, 1, ' ')};
    public static PixelData[] NIGHT_TREE_1 = new PixelData[]{new PixelData(true, 5, COLOR22, 0.9, 'A')};
    //            new PixelData(false, 0, COLOR24, 1, ' ')};
    public static PixelData[] NIGHT_TREE_2 = new PixelData[]{new PixelData(true, 5, COLOR23, 0.9, 'A')};
    //            new PixelData(false, 0, COLOR24, 1, ' ')};
    public static PixelData[] GRASS_BACKGROUND_EMPTY = new PixelData[]{new PixelData(false, -10, COLOR24, 1, ' ')};
    public static PixelData[] GRASS_BACKGROUND_1 = new PixelData[]{new PixelData(true, -9, COLOR25, 1, '`'),
            new PixelData(false, -10, COLOR24, 1, ' ')};
    public static PixelData[] GRASS_BACKGROUND_2 = new PixelData[]{new PixelData(true, -9, COLOR25, 1, '\"'),
            new PixelData(false, -10, COLOR24, 1, ' ')};
    public static PixelData[] SAND_WALL = new PixelData[]{new PixelData(true, 1, COLOR6, 1, '#'),
            new PixelData(false, 0, COLOR3, 1, ' ')};
    public static PixelData[] SAND_BACKGROUND_EMPTY = new PixelData[]{new PixelData(false, -10, COLOR2, 1, ' ')};
    public static PixelData[] SAND_BACKGROUND_1 = new PixelData[]{new PixelData(true, -9, COLOR12, 1, ','),
            new PixelData(false, -10, COLOR2, 1, ' ')};
    public static PixelData[] SAND_BACKGROUND_2 = new PixelData[]{new PixelData(true, -9, COLOR12, 1, '+'),
            new PixelData(false, -10, COLOR2, 1, ' ')};
    public static PixelData[] SAND_BACKGROUND_3 = new PixelData[]{new PixelData(true, -9, COLOR3, 1, 'N'),
            new PixelData(false, -10, COLOR2, 1, ' ')};
    public static PixelData[] DUNGEON_WALL = new PixelData[]{new PixelData(true, 1, TextColor.ANSI.BLACK, 1, '*'),
            new PixelData(false, 0, COLOR21, 1, ' ')};
    public static PixelData[] DUNGEON_BACKGROUND_EMPTY = new PixelData[]{new PixelData(false, -10, COLOR20, 1, ' ')};
    public static PixelData[] DUNGEON_BACKGROUND_1 = new PixelData[]{new PixelData(true, -9, COLOR19, 1, '-'),
            new PixelData(false, -10, COLOR20, 1, ' ')};
    public static PixelData[] DUNGEON_BACKGROUND_2 = new PixelData[]{new PixelData(true, -9, COLOR19, 1, '='),
            new PixelData(false, -10, COLOR20, 1, ' ')};

    public static PixelData[] getPixelData(LogicPixel pixel) {
        switch (pixel) {
            case HERO:
                return HERO;
            case SWORDSMAN:
                return SWORDMAN;
            case ATTACK:
                return ATTACK;
            case LIGHT_1:
                return LIGHT_1;
            case LIGHT_2:
                return LIGHT_2;
            case LIGHT_3:
                return LIGHT_3;
            case DARKNESS_1:
                return DARKNESS_1;
            case DARKNESS_2:
                return DARKNESS_2;
            case DARKNESS_3:
                return DARKNESS_3;
            case DARKNESS_FULL:
                return DARKNESS_FULL;
            case WATER_WALL:
                return WATER_WALL;
            case WATER_BACKGROUND_EMPTY:
                return WATER_BACKGROUND_EMPTY;
            case WATER_BACKGROUND_1:
                return WATER_BACKGROUND_1;
            case WATER_BACKGROUND_2:
                return WATER_BACKGROUND_2;
            case NIGHT_TREE_1:
                return NIGHT_TREE_1;
            case NIGHT_TREE_2:
                return NIGHT_TREE_2;
            case GRASS_BACKGROUND_EMPTY:
                return GRASS_BACKGROUND_EMPTY;
            case GRASS_BACKGROUND_1:
                return GRASS_BACKGROUND_1;
            case GRASS_BACKGROUND_2:
                return GRASS_BACKGROUND_2;
            case SAND_WALL:
                return SAND_WALL;
            case SAND_BACKGROUND_EMPTY:
                return SAND_BACKGROUND_EMPTY;
            case SAND_BACKGROUND_1:
                return SAND_BACKGROUND_1;
            case SAND_BACKGROUND_2:
                return SAND_BACKGROUND_2;
            case SAND_BACKGROUND_3:
                return SAND_BACKGROUND_3;
            case DUNGEON_WALL:
                return DUNGEON_WALL;
            case DUNGEON_BACKGROUND_EMPTY:
                return DUNGEON_BACKGROUND_EMPTY;
            case DUNGEON_BACKGROUND_1:
                return DUNGEON_BACKGROUND_1;
            case DUNGEON_BACKGROUND_2:
                return DUNGEON_BACKGROUND_2;
            default:
                return null;
        }
    }


}
