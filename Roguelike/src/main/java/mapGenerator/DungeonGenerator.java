package mapGenerator;

import map.MapOfObjects;
import map.roomSystem.*;
import objects.creatures.HeroObject;
import objects.creatures.Swordsman;
import objects.neighbourfoods.AccessNeighbourhood;
import objects.neighbourfoods.DistantDarkness;
import util.Coord;

public class DungeonGenerator extends MapGenerator {
    private int mapXSize;
    private int mapYSize;
    public static Room room1;

    public DungeonGenerator(int xSize, int ySize) {
        mapXSize = xSize;
        mapYSize = ySize;
    }

    @Override
    public MapOfObjects generateMap() {
        MapOfObjects map = new MapOfObjects(mapXSize, mapYSize);
        map.heroObject = (HeroObject) new HeroObject(map, new Coord(6, 6)).attachToMap();
        new Swordsman(map, new Coord(7, 6)).attachToMap();
        RoomSystem roomSystem = new RoomSystem(map);
        RoomTextures textures = new DungeonTextures();


        Room[][] rooms = new Room[mapXSize / 8 + 3][mapYSize / 8 + 3];
        for (int i = 3; i < mapXSize - 8; i += 8) {
            for (int j = 3; j < mapYSize - 8; j += 8) {
                Room room = new Room(map, new Coord(i, j), new Coord(5, 5), 3, textures);
                rooms[(i - 2) / 8][(j - 2) / 8] = room;
                roomSystem.addRoom(room);
                if (Math.random() < 0.2) {
                    new Swordsman(map, new Coord(i + 1, j + 1)).attachToMap();
                }
            }
        }
        for (int i = 0; i < rooms.length - 1; i++) {
            for (int j = 0; j < rooms[0].length - 1; j++) {
                Room room1 = rooms[i][j];
                Room room2 = rooms[i + 1][j];
                Room room3 = rooms[i][j + 1];
                if (room1 != null && room2 != null) {
                    int width = (int) (Math.random() * 4) + 1;
                    double rand = Math.random();
                    if (rand < 0.2) {
                        roomSystem.addPassage(new Door(room1, room2));
                    } else if (rand < 0.7) {
                        roomSystem.addPassage(new Corridor(room1, room2, width));
                    }
                }
                if (room1 != null && room3 != null) {
                    int width = (int) (Math.random() * 4) + 1;
                    double rand = Math.random();
                    if (rand < 0.2) {
                        roomSystem.addPassage(new Door(room1, room3));
                    } else if (rand < 0.7) {
                        roomSystem.addPassage(new Corridor(room1, room3, width));
                    }
                    if (room1.passages.isEmpty()) {
                        width = (int) (Math.random() * 4) + 1;
                        rand = Math.random();
                        if (rand < 0.4) {
                            roomSystem.addPassage(new Door(room1, room3));
                        } else {
                            roomSystem.addPassage(new Corridor(room1, room3, width));
                        }
                    }
                }
            }
        }

        roomSystem.attachToMap();
        new DistantDarkness(map, map.heroObject.getLocation(), 10).attachToMap();
        map.heroAccessNeighbourhood = (AccessNeighbourhood) new AccessNeighbourhood(map, map.getHeroLocation(), 5).attachToMap();

        return map;
    }
}
