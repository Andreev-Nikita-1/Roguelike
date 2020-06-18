package mapGenerator;

import renderer.VisualPixel;
import map.MapOfObjects;

import static renderer.VisualPixel.*;
import static util.Util.generate;

public class TestGenerator extends MapGenerator {

    private int mapXSize;
    private int mapYSize;
    private int chunkSizeX = 40;
    private int chunkSizeY = 20;
    private double mobProba = 0.3;

    public TestGenerator(int xSize, int ySize) {
        mapXSize = xSize;
        mapYSize = ySize;
    }

    @Override
    public MapOfObjects generateMap() {
//        MapOfObjects map = new MapOfObjects(mapXSize, mapYSize);
//        int xHero = mapXSize / 2;
//        int yHero = mapYSize / 2;
//        map.heroObject = (HeroObject) new HeroObject(map, new Coord(xHero, yHero)).attachToMap();
//
////        new DistantDarkness(map, map.heroObject.getLocation(), 10).attachToMap();
////        new testAcess(map, map.heroObject.getLocation(), 10).attachToMap();
//        RoomSystem roomSystem = new RoomSystem(map);
//        RoomTextures textures = new DungeonTextures();
//        Room room1 = new Room(map, new Coord(8, 8), new Coord(5, 5), 1, textures);
//        Room room2 = new Room(map, new Coord(7, 5), new Coord(7, 2), 0, textures);
//        Room room3 = new Room(map, new Coord(7, 14), new Coord(7, 1), 0, textures);
//        Room room4 = new Room(map, new Coord(4, 2), new Coord(3, 13), 0, textures);
//        Room room5 = new Room(map, new Coord(14, 5), new Coord(3, 10), 0, textures);
//        Room room6 = new Room(map, new Coord(8, 2), new Coord(9, 2), 1, textures);
//        Room room7 = new Room(map, new Coord(18, 0), new Coord(1, 15), 0, 1, 1, 1, textures);
//        Room room8 = new Room(map, new Coord(0, 0), new Coord(17, 1), 0, 1, 0, 1, textures);
//        Room room9 = new Room(map, new Coord(1, 16), new Coord(4, 3), 1, textures);
//        Room room10 = new Room(map, new Coord(6, 16), new Coord(9, 3), 1, textures);
//        Room room11 = new Room(map, new Coord(16, 16), new Coord(4, 3), 1, 1, 1, 0, textures);
//        Room room12 = new Room(map, new Coord(0, 2), new Coord(2, 13), 1, 1, 0, 2, textures);
//        roomSystem.addRoom(room1);
//        roomSystem.addRoom(room2);
//        roomSystem.addRoom(room3);
//        roomSystem.addRoom(room4);
//        roomSystem.addRoom(room5);
//        roomSystem.addRoom(room6);
//        roomSystem.addRoom(room7);
//        roomSystem.addRoom(room8);
//        roomSystem.addRoom(room9);
//        roomSystem.addRoom(room10);
//        roomSystem.addRoom(room11);
//        roomSystem.addRoom(room12);
//
//
//        roomSystem.addPassage(new Corridor(room1, room4, 3));
//        roomSystem.addPassage(new Corridor(room1, room5, 3));
//        roomSystem.addPassage(new Door(room1, room2));
//        roomSystem.addPassage(new Door(room4, room6, 1));
//        roomSystem.addPassage(new Door(room6, room7, 0));
//        roomSystem.addPassage(new Door(room7, room11));
//        roomSystem.addPassage(new Door(room6, room5));
//        roomSystem.addPassage(new Door(room8, room12, 1));
//        roomSystem.addPassage(new Door(room9, room12));
//        roomSystem.addPassage(new Door(room3, room10));
//        roomSystem.addPassage(new Door(room4, room12, 9, 0));
//        roomSystem.addPassage(new Corridor(room11, room10, 1, 2));
//        roomSystem.addPassage(new Corridor(room9, room10, 1, 2));
//        roomSystem.addPassage(new Corridor(room2, room5));
//        roomSystem.addPassage(new Corridor(room2, room4));
//        roomSystem.addPassage(new Corridor(room3, room5));
//        roomSystem.addPassage(new Corridor(room3, room4));
//        roomSystem.addPassage(new Corridor(room8, room7));
//
//        roomSystem.attachToMap();
//
//        new Swordsman(map, new Coord(11, 11)).attachToMap();
//

//


//
//        VisualPixel[][] background = background();
//        new Background(map, Coord.ZERO, background).attachToMap();
////        map.heroObject.dependingObjects.add(new LightSphere(map.heroObject, 22).attachToMap());
////        map.heroObject.dependingObjects.add(new DataCloud(map.heroObject, "HELLO").attachToMap());
//
//        for (int i = 0; i < mapXSize; i += chunkSizeX) {
//            for (int j = 0; j < mapYSize; j += chunkSizeY) {
//                double treesProportion = treesProportion(i + chunkSizeX / 2 - xHero, j + chunkSizeY / 2 - yHero);
//                VisualPixel[][] array = chunkForest(Math.min(chunkSizeX, mapXSize - i), Math.min(chunkSizeY, mapYSize - j), treesProportion);
//                if (xHero >= i && xHero < i + chunkSizeX &&
//                        yHero >= j && yHero < j + chunkSizeY) {
//                    array[xHero - i][yHero - j] = null;
////                    array[xHero - i][yHero - j + 1] = null;
////                    new Swordsman(map, new Coord(xHero, yHero-1)).attachToMap();
//                }
//                if (Math.random() < mobProba) {
//                    int a = (int) (Math.random() * Math.min(chunkSizeX, mapXSize - i - 1));
//                    int b = (int) (Math.random() * Math.min(chunkSizeY, mapYSize - j - 1));
//                    if (i + a != xHero || j + b != yHero) {
//                        array[a][b] = null;
//                        new Swordsman(map, new Coord(i + a, j + b)).attachToMap();
//                    }
//                }
//                new Wall(map, new Coord(i, j), array).attachToMap();
//            }
//        }


        return null;
    }

    private double treesProportion(int distanceX, int distanceY) {
        return (Math.pow(distanceX, 2) + Math.pow(2 * distanceY, 2)) /
                (Math.pow(mapXSize, 2) + Math.pow(2 * mapYSize, 2));
    }

    private VisualPixel[][] chunkForest(int xSize, int ySize, double treesProportion) {
        VisualPixel[][] array = new VisualPixel[xSize][ySize];
        double[] weights = new double[]{1 - treesProportion, 0.6 * treesProportion, 0.4 * treesProportion};
        VisualPixel[] options = new VisualPixel[]{null, NIGHT_TREE_1, NIGHT_TREE_2};
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = generate(weights, options);
            }
        }
        return array;
    }

    private VisualPixel[][] background() {
        VisualPixel[][] array = new VisualPixel[mapXSize][mapYSize];
        double[] weights = new double[]{0.6, 0.2, 0.2};
        VisualPixel[] options = new VisualPixel[]{GRASS_BACKGROUND_EMPTY, GRASS_BACKGROUND_1, GRASS_BACKGROUND_2};
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = generate(weights, options);
            }
        }
        return array;
    }
}
