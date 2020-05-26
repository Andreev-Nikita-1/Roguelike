package map.roomSystem.textures;

import map.MapOfObjects;
import map.roomSystem.Background;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.awt.*;

public class ReachCarpet2 extends StoneWallsTextures {
    private static final Color BACKGROUND_COLOR = new Color(148, 60, 48);
    private static final Color SYMBOL_COLOR = new Color(144, 16, 0);

    private static VisualPixel BACK = new VisualPixel(
            new PixelData(false, -10, BACKGROUND_COLOR, 1, ' '));
    private static VisualPixel LEFTUP = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x023A)).combinedWith(BACK);
    private static VisualPixel UP = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x023B)).combinedWith(BACK);
    private static VisualPixel RIGHTUP = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x023C)).combinedWith(BACK);
    private static VisualPixel LEFTDOWN = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x023D)).combinedWith(BACK);
    private static VisualPixel DOWN = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x023E)).combinedWith(BACK);
    private static VisualPixel RIGHTDOWN = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x023F)).combinedWith(BACK);
    private static VisualPixel LEFT = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x0240)).combinedWith(BACK);
    private static VisualPixel RIGHT = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x0241)).combinedWith(BACK);
    private static VisualPixel INSIDE = new VisualPixel(
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x0217)).combinedWith(BACK);


    public ReachCarpet2(int seed) {
        super(seed);
    }

    @Override
    public Background createBackground(MapOfObjects map, Coord coord, int hight, int width) {
        VisualPixel[][] array = new VisualPixel[width][hight];
        if (hight == 1 || width == 1) {
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[0].length; j++) {
                    array[i][j] = INSIDE;
                }
            }
        } else {
            for (int i = 1; i < width - 1; i++) {
                for (int j = 1; j < hight - 1; j++) {
                    array[i][j] = INSIDE;
                }
            }
            for (int i = 1; i < width - 1; i++) {
                array[i][0] = UP;
                array[i][hight - 1] = DOWN;
            }
            for (int i = 1; i < hight - 1; i++) {
                array[0][i] = LEFT;
                array[width - 1][i] = RIGHT;
            }
            array[0][0] = LEFTUP;
            array[width - 1][0] = RIGHTUP;
            array[width - 1][hight - 1] = RIGHTDOWN;
            array[0][hight - 1] = LEFTDOWN;
        }
        return new Background(map, coord, array);
    }

}
