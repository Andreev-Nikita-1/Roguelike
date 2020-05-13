package mapGenerator;

import inventory.Hero;
import map.MapOfObjects;
import map.roomSystem.*;
import objects.Lighting;
import objects.creatures.HeroObject;
import objects.creatures.Swordsman;

import util.Coord;

public class DungeonGenerator extends MapGenerator {
    private int mapXSize;
    private int mapYSize;

    public DungeonGenerator(int xSize, int ySize) {
        mapXSize = xSize;
        mapYSize = ySize;
    }

    @Override
    public MapOfObjects generateMap(Hero hero) {
        MapOfObjects map = new MapOfObjects(mapXSize, mapYSize);
        map.heroObject = new HeroObject(map, new Coord(6, 6), hero).attachToMap();
        new Swordsman(map, new Coord(7, 6)).attachToMap();
        RoomSystem roomSystem = new RoomSystem(map);

        Room[][] rooms = new Room[mapXSize / 8 + 3][mapYSize / 8 + 3];
        for (int i = 3; i < mapXSize - 8; i += 8) {
            for (int j = 3; j < mapYSize - 8; j += 8) {
                Room room = new Room(map, new Coord(i, j), new Coord(5, 5), 3,
                        new SeedBasedTextures.DungeonTextures((int) (Math.random() * 100)));
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
                    int bias = (int) (Math.random() * 3);
                    int width = (int) (Math.random() * (4 - bias)) + 1;
                    double rand = Math.random();
                    if (rand < 0.2) {
                        roomSystem.addPassage(new Door(room1, room2, bias));
                    } else if (rand < 0.7) {
                        roomSystem.addPassage(new Corridor(room1, room2, bias, width));
                    }
                }
                if (room1 != null && room3 != null) {
                    int bias = (int) (Math.random() * 3);
                    int width = (int) (Math.random() * (4 - bias)) + 1;
                    double rand = Math.random();
                    if (rand < 0.2) {
                        roomSystem.addPassage(new Door(room1, room3));
                    } else if (rand < 0.7) {
                        roomSystem.addPassage(new Corridor(room1, room3, width));
                    }
                    if (room1.passages.isEmpty()) {
                        bias = (int) (Math.random() * 3);
                        width = (int) (Math.random() * (4 - bias)) + 1;
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
        map.lighting = (Lighting) new Lighting(map, 7).attachToMap();
        return map;
    }
}
