package map.roomSystem.textures;

import map.roomSystem.Background;
import renderer.VisualPixel;
import util.Coord;

/**
 * Generates floor from VisualPixelGenerator, using seed
 */
public class RandomFloor extends StoneWallsTextures {
    private VisualPixelGenerator generator;

    public RandomFloor(int seed, VisualPixelGenerator generator) {
        super(seed);
        this.generator = generator;
    }

    @Override
    public Background createBackground(Coord coord, int hight, int width) {
        VisualPixel[][] array = new VisualPixel[width][hight];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = generator.generate(random.nextDouble());
            }
        }
        return new Background(coord, array);
    }
}
