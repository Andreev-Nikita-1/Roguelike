package map.roomSystem;

import map.MapOfObjects;
import renderer.VisualPixel;
import util.Coord;
import util.Util;

import java.util.List;
import java.util.Random;

import static renderer.VisualPixel.*;

public class SeedBasedTextures extends RoomTextures {
    private VisualPixel[] wallPixels;
    private double[] wallWeights;
    private VisualPixel[] backgroundPixels;
    private double[] backgroundWeights;

    protected int seed;
    protected Random random;

    public static class DungeonTextures extends SeedBasedTextures {
        private static VisualPixel[] dungeonWallPixels = new VisualPixel[]{DUNGEON_WALL};
        private static double[] dungeonWallWeights = new double[]{1};
        private static VisualPixel[] dungeonBackgroundPixels = new VisualPixel[]{DUNGEON_BACKGROUND_EMPTY, DUNGEON_BACKGROUND_1, DUNGEON_BACKGROUND_2};
        private static double[] dungeonBackgroundWeights = new double[]{0.6, 0.2, 0.2};

        public DungeonTextures(int seed) {
            super(seed, dungeonWallPixels, dungeonWallWeights, dungeonBackgroundPixels, dungeonBackgroundWeights);
        }
    }

    public SeedBasedTextures(int seed,
                             VisualPixel[] wallPixels, double[] wallWeights,
                             VisualPixel[] backgroundPixels, double[] backgroundWeights) {
        this.seed = seed;
        random = new Random(seed);
        this.wallPixels = wallPixels;
        this.wallWeights = wallWeights;
        this.backgroundPixels = backgroundPixels;
        this.backgroundWeights = backgroundWeights;
    }

    public SeedBasedTextures copy() {
        return new SeedBasedTextures(seed, wallPixels, wallWeights, backgroundPixels, backgroundWeights);
    }

    @Override
    public Wall createWall(MapOfObjects map, Coord coord, boolean horizontal, int length, int width, List<Passage> passages) {
        VisualPixel[][] array = horizontal ? new VisualPixel[length][width] : new VisualPixel[width][length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = Util.generate(wallWeights, wallPixels, random.nextDouble());
            }
        }
        return new Wall(map, coord, cutOutPassages(array, coord, horizontal, passages));
    }


    @Override
    public Background createBackground(MapOfObjects map, Coord coord, int hight, int width) {
        VisualPixel[][] array = new VisualPixel[width][hight];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = Util.generate(backgroundWeights, backgroundPixels, random.nextDouble());
            }
        }
        return new Background(map, coord, array);
    }
}
