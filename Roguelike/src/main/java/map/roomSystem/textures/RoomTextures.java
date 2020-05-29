package map.roomSystem.textures;

import map.MapOfObjects;
import map.roomSystem.Background;
import map.roomSystem.Passage;
import map.roomSystem.Wall;
import org.json.JSONArray;
import org.json.JSONObject;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;

public abstract class RoomTextures {

    protected int seed;
    protected Random random;

    public RoomTextures(int seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public abstract Wall createWall(Coord coord, Direction direction, int length, int width, List<Passage> passages);

    public abstract Background createBackground(Coord coord, int hight, int width);

    public static VisualPixel[][] cutOutPassages(VisualPixel[][] array, Coord coord, boolean horizontal, List<Passage> passages) {
        for (Passage passage : passages) {
            Coord relativeLocation = passage.location.relative(coord);
            if (horizontal) {
                for (int x = relativeLocation.x; x < relativeLocation.x + passage.width; x++) {
                    for (int y = 0; y < array[0].length; y++) {
                        try {
                            array[x][y] = null;
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
            } else {
                for (int y = relativeLocation.y; y < relativeLocation.y + passage.width; y++) {
                    for (int x = 0; x < array.length; x++) {
                        try {
                            array[x][y] = null;
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
            }
        }
        return array;
    }

    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("seed", seed)
                .put("class", this.getClass().getName());
    }

    public static RoomTextures restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (RoomTextures) Class
                .forName(jsonObject.getString("class"))
                .getConstructor(int.class)
                .newInstance(jsonObject.getInt("seed"));
    }

    public static RoomTextures restoreSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (RoomTextures) Class
                .forName(jsonObject.getString("class"))
                .getMethod("restoreFromSnapshot", JSONObject.class)
                .invoke(null, jsonObject);
    }
}
