package renderer;

import com.googlecode.lanterna.TextColor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static basicComponents.AppLogic.HERO_SYMBOL;
import static com.googlecode.lanterna.TextColor.ANSI.RED;
import static renderer.Colors.*;

public class VisualPixel {
    public static final VisualPixel HERO = new VisualPixel(
            new PixelData(true, 10, HERO_COLOR, 1, HERO_SYMBOL));
    public static final VisualPixel SWORDSMAN = new VisualPixel(
            new PixelData(true, 10, SWORDMEN_COLOR, 1, (char) 555));
    public static final VisualPixel ATTACK = new VisualPixel(
            new PixelData(false, 19, RED, 0.3, ' '));
    public static final VisualPixel INTERACT = new VisualPixel(
            new PixelData(false, 19, TextColor.ANSI.GREEN, 0.1, ' '));
    public static final VisualPixel DOOR_CLOSED = new VisualPixel(
            new PixelData(true, 10, DOOR_SYMBOL_COLOR, 1, (char) 8782),
            new PixelData(false, 9, DOOR_BACK_COLOR, 1, ' ')
    );
    public static final VisualPixel DOOR_OPEN_HORIZONTAL = new VisualPixel(
            new PixelData(true, 5, DOOR_BACK_COLOR, 1, (char) 9601)
    );
    public static final VisualPixel DOOR_OPEN_VERTICAl = new VisualPixel(
            new PixelData(true, 5, DOOR_BACK_COLOR, 1, (char) 9615)
    );
    public static final VisualPixel HEART = new VisualPixel(
            new PixelData(true, 5, RED, 1, (char) 57355)
    );
    public static final VisualPixel HEART_CRACKED = new VisualPixel(
            new PixelData(true, 5, RED, 1, (char) 57356)
    );
    public static final VisualPixel STUFF = new VisualPixel(
            new PixelData(true, 5, COLOR7, 1, (char) 9975)
    );
    public static final VisualPixel LIGHT_1 = new VisualPixel(
            new PixelData(false, 2, COLOR3, 0.2, ' '));
    public static final VisualPixel LIGHT_2 = new VisualPixel(
            new PixelData(false, 2, COLOR3, 0.1, ' '));
    public static final VisualPixel LIGHT_3 = new VisualPixel(
            new PixelData(false, 2, COLOR3, 0.05, ' '));
    public static final VisualPixel DARKNESS_1 = new VisualPixel(
            new PixelData(false, 20, TextColor.ANSI.BLACK, 0.5, ' '));
    public static final VisualPixel DARKNESS_2 = new VisualPixel(
            new PixelData(false, 20, TextColor.ANSI.BLACK, 0.7, ' '));
    public static final VisualPixel DARKNESS_3 = new VisualPixel(
            new PixelData(false, 20, TextColor.ANSI.BLACK, 0.9, ' '));
    public static final VisualPixel DARKNESS_FULL = new VisualPixel(
            new PixelData(false, 20, TextColor.ANSI.BLACK, 1, ' '));
    public static final VisualPixel NIGHT_TREE_1 = new VisualPixel(
            new PixelData(true, 5, COLOR22, 0.9, (char) 9195));
    public static final VisualPixel NIGHT_TREE_2 = new VisualPixel(
            new PixelData(true, 5, COLOR23, 0.9, (char) 9195));
    public static final VisualPixel GRASS_BACKGROUND_EMPTY = new VisualPixel(
            new PixelData(false, -10, COLOR24, 1, ' '));
    public static final VisualPixel GRASS_BACKGROUND_1 = new VisualPixel(
            new PixelData(true, -9, COLOR25, 1, '`'),
            new PixelData(false, -10, COLOR24, 1, ' '));
    public static final VisualPixel GRASS_BACKGROUND_2 = new VisualPixel(
            new PixelData(true, -9, COLOR25, 1, '\"'),
            new PixelData(false, -10, COLOR24, 1, ' '));
    public static final VisualPixel DUNGEON_WALL = new VisualPixel(
            new PixelData(true, 1, TextColor.ANSI.BLACK, 1, '*'),
            new PixelData(false, 0, COLOR21, 1, ' '));
    public static final VisualPixel DUNGEON_BACKGROUND_EMPTY = new VisualPixel(
            new PixelData(false, -10, COLOR20, 1, ' '));
    public static final VisualPixel DUNGEON_BACKGROUND_1 = new VisualPixel(
            new PixelData(true, -9, COLOR19, 1, '-'),
            new PixelData(false, -10, COLOR20, 1, ' '));
    public static final VisualPixel DUNGEON_BACKGROUND_2 = new VisualPixel(
            new PixelData(true, -9, COLOR19, 1, '='),
            new PixelData(false, -10, COLOR20, 1, ' '));


    private List<PixelData> pixelDataList;

    public VisualPixel(List<PixelData> pixelData) {
        this.pixelDataList = pixelData;
    }

    public VisualPixel(PixelData... pixelDatas) {
        pixelDataList = new ArrayList<>(Arrays.asList(pixelDatas));
    }

    public List<PixelData> getPixelDataList() {
        return pixelDataList;
    }


    private Map<VisualPixel, VisualPixel> combinedWith = new HashMap<>();

    public VisualPixel combinedWith(VisualPixel other) {
        if (!combinedWith.containsKey(other)) {
            combinedWith.put(other, new VisualPixel(Stream
                    .concat(this.getPixelDataList().stream(), other.getPixelDataList().stream())
                    .collect(Collectors.toList())));
        }
        return combinedWith.get(other);
    }

    private Map<TextColor, Map<Double, VisualPixel>> highlighted = new HashMap<>();

    public VisualPixel highlighted(TextColor color, double transparency) {
        if (!highlighted.containsKey(color) || !highlighted.get(color).containsKey(transparency)) {
            if (!highlighted.containsKey(color)) {
                highlighted.put(color, new HashMap<>());
            }
            highlighted.get(color).put(transparency,
                    new VisualPixel(this.getPixelDataList()
                            .stream()
                            .map(pixelData -> pixelData.layOn(color, transparency))
                            .collect(Collectors.toList())));
        }
        return highlighted.get(color).get(transparency);

    }
}

