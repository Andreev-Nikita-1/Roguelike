package map.roomSystem;

import map.MapOfObjects;
import renderer.VisualPixel;
import util.Coord;
import util.Util;

import java.util.Arrays;
import java.util.List;

import static renderer.VisualPixel.*;

public class DungeonTextures extends RoomTextures {

    @Override
    public Wall createWall(MapOfObjects map, Coord coord, boolean horizontal, int length, int width, List<Passage> passages) {
        VisualPixel[][] array = horizontal ? new VisualPixel[length][width] : new VisualPixel[width][length];
        for (int i = 0; i < array.length; i++) {
            Arrays.fill(array[i], DUNGEON_WALL);
        }
        return new Wall(map, coord, cutOutPassages(array, coord, horizontal, passages));
    }


    @Override
    public Background createBackground(MapOfObjects map, Coord coord, int hight, int width) {
        VisualPixel[][] array = new VisualPixel[width][hight];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = Util.generate(new double[]{0.6, 0.2, 0.2}, new VisualPixel[]{
                        DUNGEON_BACKGROUND_EMPTY, DUNGEON_BACKGROUND_1, DUNGEON_BACKGROUND_2});
            }
        }
        return new Background(map, coord, array);
    }
}
