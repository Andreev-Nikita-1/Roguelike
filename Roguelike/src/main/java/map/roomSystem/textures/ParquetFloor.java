package map.roomSystem.textures;

import map.MapOfObjects;
import map.roomSystem.Background;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.awt.*;

public class ParquetFloor extends StoneWallsTextures {
    private static final Color SYMBOL_COLOR = new Color(96, 42, 0);
    private static final Color BACKGROUND_COLOR = new Color(132, 90, 72);
    private static VisualPixel VERTICAL_PLANKS = new VisualPixel(
            new PixelData(false, -10, BACKGROUND_COLOR, 1, ' '),
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x01D8)
    );
    private static VisualPixel HORIZONTAL_PLANKS = new VisualPixel(
            new PixelData(false, -10, BACKGROUND_COLOR, 1, ' '),
            new PixelData(true, -9, SYMBOL_COLOR, 1, (char) 0x01D9)
    );

    public ParquetFloor(int seed) {
        super(seed);
    }

    @Override
    public Background createBackground(Coord coord, int hight, int width) {
        VisualPixel[][] array = new VisualPixel[width][hight];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = ((i + j) % 2 == 0) ? VERTICAL_PLANKS : HORIZONTAL_PLANKS;
            }
        }
        return new Background(coord, array);
    }
}
