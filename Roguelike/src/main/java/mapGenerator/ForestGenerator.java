package mapGenerator;

import map.LogicPixel;
import map.MapOfObjects;
import map.objects.*;
import util.Coord;

import static map.LogicPixel.*;
import static util.Util.generate;

public class ForestGenerator extends MapGenerator {

    private int mapXSize;
    private int mapYSize;
    private int chunkSizeX = 40;
    private int chunkSizeY = 20;
    private double mobProba = 0.3;

    public ForestGenerator(int xSize, int ySize) {
        mapXSize = xSize;
        mapYSize = ySize;
    }

    @Override
    public MapOfObjects generateMap() {
        MapOfObjects map = new MapOfObjects(mapXSize, mapYSize);
        int xHero = mapXSize / 2;
        int yHero = mapYSize / 2;


        LogicPixel[][] background = background();
        new Background(map, background).attachToMap();
        map.heroObject = new HeroObject(map, new Coord(xHero, yHero), 45).attachToMap();
        map.heroObject.dependingObjects.add(new LightSphere(map.heroObject, 22).attachToMap());

        for (int i = 0; i < mapXSize; i += chunkSizeX) {
            for (int j = 0; j < mapYSize; j += chunkSizeY) {
                double treesProportion = treesProportion(i + chunkSizeX / 2 - xHero, j + chunkSizeY / 2 - yHero);
                LogicPixel[][] array = chunkForest(Math.min(chunkSizeX, mapXSize - i), Math.min(chunkSizeY, mapYSize - j), treesProportion);
                if (xHero >= i && xHero < i + chunkSizeX &&
                        yHero >= j && yHero < j + chunkSizeY) {
                    array[xHero - i][yHero - j] = null;
                }
                if (Math.random() < mobProba) {
                    int a = (int) (Math.random() * Math.min(chunkSizeX, mapXSize - i - 1));
                    int b = (int) (Math.random() * Math.min(chunkSizeY, mapYSize - j - 1));
                    if (i + a != xHero || j + b != yHero) {
                        array[a][b] = null;
                        new Swordsman(map, new Coord(i + a, j + b)).attachToMap();
                    }
                }
                new Walls(map, new Coord(i, j), array).attachToMap();
            }
        }

        return map;
    }

    private double treesProportion(int distanceX, int distanceY) {
        return (Math.pow(distanceX, 2) + Math.pow(2 * distanceY, 2)) /
                (Math.pow(mapXSize, 2) + Math.pow(2 * mapYSize, 2));
    }

    private LogicPixel[][] chunkForest(int xSize, int ySize, double treesProportion) {
        LogicPixel[][] array = new LogicPixel[xSize][ySize];
        double[] weights = new double[]{1 - treesProportion, 0.6 * treesProportion, 0.4 * treesProportion};
        LogicPixel[] options = new LogicPixel[]{null, NIGHT_TREE_1, NIGHT_TREE_2};
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = generate(weights, options);
            }
        }
        return array;
    }

    private LogicPixel[][] background() {
        LogicPixel[][] array = new LogicPixel[mapXSize][mapYSize];
        double[] weights = new double[]{0.6, 0.2, 0.2};
        LogicPixel[] options = new LogicPixel[]{GRASS_BACKGROUND_EMPTY, GRASS_BACKGROUND_1, GRASS_BACKGROUND_2};
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = generate(weights, options);
            }
        }
        return array;
    }
}
