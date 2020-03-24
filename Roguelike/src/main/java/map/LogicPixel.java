package map;

public enum LogicPixel {
    HERO,
    LIGHT, DARKNESS_1, DARKNESS_2, DARKNESS_FULL,
    SAND_WALL, SAND_BACKGROUND_EMPTY, SAND_BACKGROUND_1, SAND_BACKGROUND_2, SAND_BACKGROUND_3,
    GRASS_WALL_1, GRASS_WALL_2, GRASS_BACKGROUND_EMPTY, GRASS_BACKGROUND_1, GRASS_BACKGROUND_2,
    WATER_WALL, WATER_BACKGROUND_EMPTY, WATER_BACKGROUND_1, WATER_BACKGROUND_2,
    DUNGEON_WALL, DUNGEON_BACKGROUND_EMPTY, DUNGEON_BACKGROUND_1, DUNGEON_BACKGROUND_2;


    public static LogicPixel generatePixel(double[] weights, LogicPixel[] pixels) {
        double t = Math.random();
        for (int i = 0; i < weights.length; i++) {
            if (t < weights[i]) {
                return pixels[i];
            } else {
                t -= weights[i];
            }
        }
        return null;
    }
}

