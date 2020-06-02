package map.roomSystem.textures;

import map.roomSystem.Background;
import org.json.JSONObject;
import renderer.VisualPixel;
import util.Coord;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * Generates background, consisting of two background of different textures. Used for passages background
 */
public class FusedBackground extends StoneWallsTextures {
    private RoomTextures first;
    private RoomTextures second;
    private boolean horizontal;
    private int bias;

    public FusedBackground(Class first, Class second, int seed, boolean horizontal, int bias) {
        super(seed);
        try {
            this.first = (RoomTextures) first.getConstructor(int.class).newInstance(seed);
            this.second = (RoomTextures) second.getConstructor(int.class).newInstance(seed + 1);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.horizontal = horizontal;
        this.bias = bias;
    }

    @Override
    public Background createBackground(Coord coord, int hight, int width) {
        Map<Coord, VisualPixel> firstMap;
        Map<Coord, VisualPixel> secondMap;

        if (horizontal) {
            firstMap = first.createBackground(coord, hight, bias).getPixels(null, null);
            secondMap = second.createBackground(coord.shifted(new Coord(bias, 0)), hight, width - bias).getPixels(null, null);
        } else {
            firstMap = first.createBackground(coord, bias, width).getPixels(null, null);
            secondMap = second.createBackground(coord.shifted(new Coord(0, bias)), hight - bias, width).getPixels(null, null);
        }
        for (Coord c : secondMap.keySet()) {
            firstMap.put(c, secondMap.get(c));
        }
        return new Background(coord, firstMap);
    }


    @Override
    public JSONObject getSnapshot() {
        JSONObject snapshot = super.getSnapshot();
        snapshot.put("horizontal", horizontal);
        snapshot.put("bias", bias);
        snapshot.put("first", first.getClass().getName());
        snapshot.put("second", second.getClass().getName());
        return snapshot;
    }


    public static RoomTextures restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (RoomTextures) Class
                .forName(jsonObject.getString("class"))
                .getConstructor(Class.class, Class.class, int.class, boolean.class, int.class)
                .newInstance(Class.forName(jsonObject.getString("first")),
                        Class.forName(jsonObject.getString("second")),
                        jsonObject.getInt("seed"),
                        jsonObject.getBoolean("horizontal"),
                        jsonObject.getInt("bias"));
    }
}
