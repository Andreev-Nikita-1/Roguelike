package mapGenerator;

import hero.Hero;
import hero.items.Item;
import hero.items.Shield;
import hero.items.Weapon;
import map.MapOfObjects;
import map.roomSystem.*;
import map.roomSystem.textures.*;
import objects.Lighting;
import objects.creatures.HeroObject;
import objects.creatures.ScaryMonster;

import objects.stuff.Candle;
import objects.stuff.ItemHolder;
import util.Coord;
import util.Util;

import java.lang.reflect.InvocationTargetException;

public class DefaultGenerator extends MapGenerator {
    private int mapXSize;
    private int mapYSize;

    public DefaultGenerator(int xSize, int ySize) {
        mapXSize = xSize;
        mapYSize = ySize;
    }

    @Override
    public MapOfObjects generateMap() {
        MapOfObjects map = new MapOfObjects(mapXSize, mapYSize);
        map.heroObject = new HeroObject(new Coord(5 + 8 * ((mapXSize / 2) / 8), 5 + 8 * ((mapYSize / 2) / 8))).attachToMap(map);
        RoomSystem roomSystem = new RoomSystem();
        Room[][] rooms = new Room[mapXSize / 8 + 3][mapYSize / 8 + 3];
        for (int i = 3; i < mapXSize - 8; i += 8) {
            for (int j = 3; j < mapYSize - 8; j += 8) {
                int r = (int) Math.floor(Coord.lInftyNorm(map.heroObject.getLocation().relative(new Coord(i, j))) / 15);
                RoomTextures textures;
                if (r % 3 == 0) {
                    textures = new StoneFloor1((int) (Math.random() * 100));
                } else if (r % 3 == 1) {
                    textures = new ParquetFloor((int) (Math.random() * 100));
                } else {
                    textures = (Math.random() < 0.5) ? new RedCarpet1((int) (Math.random() * 100)) :
                            new RedCarpet2((int) (Math.random() * 100));
                }
                Room room = new Room(new Coord(i, j), new Coord(5, 5), 3, textures);
                rooms[(i - 2) / 8][(j - 2) / 8] = room;
                roomSystem.addRoom(room);
                if (Math.random() < 0.1) {
                    ScaryMonster.Type type = Util.generate(ScaryMonster.Type.values());
                    ScaryMonster.newMonster(new Coord(i + 1, j + 1), type).attachToMap(map);
                }
                if (Math.random() < 0.1) {
                    if (Math.random() < 0.4) {
                        new Candle(new Coord(i + 3, j + 3)).attachToMap(map);
                    } else {
                        Item item = null;
                        if (Math.random() < 0.5) {
                            Weapon.Type type = Util.generate(Weapon.Type.values());
                            item = new Weapon((int) (50 * Math.random()),
                                    (int) (20 * Math.random()),
                                    (int) (50 + 100 * Math.random()),
                                    type.name(),
                                    type
                            );
                        } else {
                            Shield.Type type = Util.generate(Shield.Type.values());
                            item = new Shield((int) (300 * Math.random()),
                                    (int) (5 + 20 * Math.random()),
                                    type.name(),
                                    type
                            );
                        }
                        new ItemHolder(new Coord(i + 3, j + 3), item).attachToMap(map);
                    }
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
                        roomSystem.addPassage(new Door(room1, room2,
                                new FusedBackground(room1.textures.getClass(), room2.textures.getClass(), (int) (Math.random() * 100),
                                        true, bias)
                                , bias));
                    } else if (rand < 0.7) {
                        roomSystem.addPassage(new Corridor(room1, room2,
                                new FusedBackground(room1.textures.getClass(), room2.textures.getClass(), (int) (Math.random() * 100),
                                        true, bias)
                                , width, bias));
                    }
                }
                if (room1 != null && room3 != null) {
                    int bias = (int) (Math.random() * 3);
                    int width = (int) (Math.random() * (4 - bias)) + 1;
                    double rand = Math.random();
                    if (rand < 0.2) {
                        roomSystem.addPassage(new Door(room1, room3,
                                new FusedBackground(room1.textures.getClass(), room3.textures.getClass(), (int) (Math.random() * 100),
                                        false, bias), bias));
                    } else if (rand < 0.7) {
                        roomSystem.addPassage(new Corridor(room1, room3,
                                new FusedBackground(room1.textures.getClass(), room3.textures.getClass(), (int) (Math.random() * 100),
                                        false, bias)
                                , width, bias));
                    }
                    if (room1.passages.isEmpty() && room3 != null) {
                        bias = (int) (Math.random() * 3);
                        width = (int) (Math.random() * (4 - bias)) + 1;
                        if (Math.random() < 0.5) {
                            roomSystem.addPassage(new Door(room1, room3,
                                    new FusedBackground(room1.textures.getClass(), room3.textures.getClass(), (int) (Math.random() * 100),
                                            false, bias), bias));
                        } else {
                            roomSystem.addPassage(new Corridor(room1, room3,
                                    new FusedBackground(room1.textures.getClass(), room3.textures.getClass(), (int) (Math.random() * 100),
                                            false, bias)
                                    , width, bias));
                        }
                    }
                }
            }
        }
        roomSystem.attachToMap(map);
        return map;
    }
}
