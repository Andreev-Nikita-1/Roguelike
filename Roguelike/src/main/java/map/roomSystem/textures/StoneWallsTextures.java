package map.roomSystem.textures;

import map.roomSystem.Passage;
import map.roomSystem.Wall;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;

import java.awt.*;
import java.util.List;

/**
 * Textures, that creates stone walls
 */
public abstract class StoneWallsTextures extends RoomTextures {
    private static final Color SYMBOL_COLOR = new Color(40, 40, 40);
    private static final Color BACK_COLOR = new Color(20, 20, 20);

    private static final VisualPixel BACK = new VisualPixel(
            new PixelData(false, 0, BACK_COLOR, 1, ' '));

    private VisualPixelGenerator generator;

    StoneWallsTextures(int seed) {
        super(seed);
        generator = new VisualPixelGenerator(BACK, SYMBOL_COLOR, 0x01A7, 1, 5,
                new double[]{1.0 / 5, 1.0 / 5, 1.0 / 5, 1.0 / 5, 1.0 / 5});
    }

    @Override
    public Wall createWall(Coord coord, Direction direction, int length, int width, List<Passage> passages) {
        VisualPixel[][] array = direction.vertical() ? new VisualPixel[length][width] : new VisualPixel[width][length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = generator.generate(random.nextDouble());
            }
        }
        return new Wall(coord, cutOutPassages(array, coord, direction.vertical(), passages));
    }
}
