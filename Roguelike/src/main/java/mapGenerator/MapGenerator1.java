package mapGenerator;

public class MapGenerator1 {
//    public static void forestGenerate(int xSize, int ySize) {

//    public static void desertGenerate(int xSize, int ySize) {
//        int xHero = xSize / 2;
//        int yHero = ySize / 2;
//        MapOfObjects.initialize(xSize, ySize);
//        SettingType settingType = SettingType.SAND;
//        heroObject = new HeroObject(new Coord(xHero, yHero), 45);
//        new Background(settingType);
//        while (true) {
//            int a = (int) (Math.random() * xSize);
//            int b = (int) (Math.random() * ySize);
//            if (a != xHero || b != yHero) {
//                new Swordsman(new Coord(a, b)).startAfter(null);
//                break;
//            }
//        }
//        synchronized (MapOfObjects.mapLock) {
//            MapOfObjects.mapLock.notifyAll();
//        }
//    }
//
//    public static void dungeonGenerate(int xSize, int ySize) {
//        MapOfObjects.objectsList = new CopyOnWriteArrayList<>();
//        int xRoomSize = 40;
//        int yRoomSize = 20;
//        int n = xSize / xRoomSize;
//        int m = ySize / yRoomSize;
//        MapOfObjects.initialize(xSize, ySize);
//        SettingType settingType = SettingType.DUNGEON;
//        int xHero = (n / 2) * xRoomSize + xRoomSize / 2;
//        int yHero = (m / 2) * yRoomSize + yRoomSize / 2;
//        heroObject = new HeroObject(new Coord(xHero, yHero), 45);
//        heroObject.dependingObjects.add(new DistantDarkness(50));
//        String barrier = "BARRIER";
//        RoomConfig[][] configs = generateRoomSquare(n, m);
//        for (int i = 0; i < xSize; i += xRoomSize) {
//            for (int j = 0; j < ySize; j += yRoomSize) {
//                int[][] array = generateRoom(xRoomSize, yRoomSize, (int) (0.4 * xRoomSize), (int) (0.4 * yRoomSize), 4, 2,
//                        configs[i / xRoomSize][j / yRoomSize]);
//                Renderer.percent = ((double) m * (i / xRoomSize) + (j / yRoomSize)) / (n * m) / 5;
//                new Walls(new Coord(i, j), new CustomStaticShape(array), settingType);
//                if ((i / xRoomSize) % 2 == 0 && (j / yRoomSize) % 3 == 0) {
//                    final int i1 = i;
//                    final int j1 = j;
//                    if (i1 + xRoomSize / 2 != xHero || j1 + yRoomSize / 2 != yHero)
//                        new Thread(() -> new Swordsman(new Coord(i1 + xRoomSize / 2, j1 + yRoomSize / 2)).startAfter(barrier)).start();
//                }
//            }
//        }
//        new Background(settingType);
//        synchronized (barrier) {
//            barrier.notifyAll();
//        }
//    }
//
//    private static RoomConfig[][] generateRoomSquare(int n, int m) {
//        RoomConfig[][] rooms = new RoomConfig[n][m];
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                if (i == 0 && j == 0) {
//                    Direction closed = generate(new double[]{0.25, 0.25, 0.25, 0.25}, Direction.values());
//                    rooms[i][j] = new RoomConfig(closed);
//                } else if (i == 0) {
//                    if (!rooms[i][j - 1].right) {
//                        rooms[i][j] = new RoomConfig(LEFT);
//                    } else {
//                        Direction closed = generate(new double[]{0.3333, 0.3333, 0.3333},
//                                new Direction[]{UP, RIGHT, DOWN});
//                        rooms[i][j] = new RoomConfig(closed);
//                    }
//                } else if (j == 0) {
//                    if (!rooms[i - 1][j].down) {
//                        rooms[i][j] = new RoomConfig(UP);
//                    } else {
//                        Direction closed = generate(new double[]{0.3333, 0.3333, 0.3333},
//                                new Direction[]{LEFT, RIGHT, DOWN});
//                        rooms[i][j] = new RoomConfig(closed);
//                    }
//                } else {
//                    if (rooms[i - 1][j].down && rooms[i][j - 1].right) {
//                        Direction closed = generate(new double[]{0.5, 0.5},
//                                new Direction[]{RIGHT, DOWN});
//                        rooms[i][j] = new RoomConfig(closed);
//                    } else {
//                        Direction[] closed = new Direction[2];
//                        if (!rooms[i - 1][j].down) closed[0] = UP;
//                        if (!rooms[i][j - 1].right) closed[1] = LEFT;
//                        rooms[i][j] = new RoomConfig(closed);
//                    }
//                }
//            }
//        }
//        return rooms;
//    }
//
//    private static int[][] generateRoom(int xSize, int ySize, int xDoor, int yDoor, int xThickness, int yThickness, RoomConfig config) {
//        int[][] array = new int[xSize][ySize];
//        for (int i = 0; i < xSize; i++) {
//            for (int t = 0; t < yThickness; t++) {
//                if (!config.left || i < xDoor || i > xSize - xDoor - 1)
//                    array[i][t] = 1;
//                if (!config.right || i < xDoor || i > xSize - xDoor - 1)
//                    array[i][ySize - 1 - t] = 1;
//            }
//        }
//        for (int i = 0; i < ySize; i++) {
//            for (int t = 0; t < xThickness; t++) {
//                if (!config.up || i < yDoor || i > ySize - yDoor - 1)
//                    array[t][i] = 1;
//                if (!config.down || i < yDoor || i > ySize - yDoor - 1)
//                    array[xSize - 1 - t][i] = 1;
//            }
//        }
//        return array;
//    }
//
//    private static class RoomConfig {
//        public boolean left = true;
//        public boolean up = true;
//        public boolean right = true;
//        public boolean down = true;
//
//        public RoomConfig(Direction... directions) {
//            for (Direction direction : directions) {
//                if (direction == null) continue;
//                switch (direction) {
//                    case UP:
//                        up = false;
//                        break;
//                    case DOWN:
//                        down = false;
//                        break;
//                    case LEFT:
//                        left = false;
//                        break;
//                    case RIGHT:
//                        right = false;
//                        break;
//                }
//            }
//        }
//    }
//
//
//
//
//    public enum SettingType {
//        GRASS, SAND, WATER, DUNGEON
//    }
}
