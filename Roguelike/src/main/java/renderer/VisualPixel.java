package renderer;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VisualPixel {


    public static Color COLOR1 = new Color(0, 0, 102);
    public static Color COLOR2 = new Color(255, 255, 204);
    public static Color COLOR3 = new Color(255, 255, 153);
    public static Color COLOR4 = new Color(225, 255, 175);
    public static Color COLOR5 = new Color(204, 255, 204);
    public static Color COLOR6 = new Color(102, 51, 0);
    public static Color COLOR7 = new Color(204, 153, 0);
    public static Color COLOR8 = new Color(153, 153, 102);
    public static Color COLOR9 = new Color(204, 51, 0);
    public static Color COLOR10 = new Color(153, 255, 102);
    public static Color COLOR11 = new Color(102, 153, 0);
    public static Color COLOR12 = new Color(0, 153, 51);
    public static Color COLOR13 = new Color(0, 153, 255);
    public static Color COLOR14 = new Color(26, 13, 0);
    public static Color COLOR15 = new Color(204, 51, 0);
    public static Color COLOR16 = new Color(0, 153, 255);
    public static Color COLOR17 = new Color(255, 255, 255);
    public static Color COLOR18 = new Color(0, 100, 0);
    public static Color COLOR19 = new Color(95, 77, 57);
    public static Color COLOR20 = new Color(60, 40, 30);
    public static Color COLOR21 = new Color(10, 9, 8);
    public static Color COLOR22 = new Color(19, 21, 22);
    public static Color COLOR23 = new Color(22, 32, 21);
    public static Color COLOR24 = new Color(30, 48, 52);
    public static Color COLOR25 = new Color(26, 37, 39);
    public static Color HERO_COLOR = Color.BLACK;
    public static Color SWORDMEN_COLOR = Color.BLACK;
    public static final VisualPixel HERO = new VisualPixel(
            //TODO
            new PixelData(true, 10, HERO_COLOR, 1, (char) 0x0146));
    public static final VisualPixel SWORDSMAN = new VisualPixel(
            new PixelData(true, 10, SWORDMEN_COLOR, 1, (char) 0x017C));

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


    private static Map<Integer, VisualPixel> attack = new HashMap<>();

    public static VisualPixel attack(int power) {
        if (!attack.containsKey(power)) {
            attack.put(power,
                    new VisualPixel(
                            new PixelData(false, 19, Color.RED, 1 - 1.0 / (1 + power / 10.0), ' ')));
        }
        return attack.get(power);
    }

    private static Map<Double, VisualPixel> darkness = new HashMap<>();

    public static VisualPixel darkness(double transparency) {
        if (!darkness.containsKey(transparency)) {
            darkness.put(transparency, new VisualPixel(new PixelData(false, 20, Color.BLACK, transparency, ' ')));
        }
        return darkness.get(transparency);
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

    private Map<Color, Map<Double, VisualPixel>> highlighted = new HashMap<>();

    public VisualPixel highlighted(Color color, double transparency) {
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

    private Map<Double, VisualPixel> brighter = new HashMap<>();

    public VisualPixel brighter(double brightness) {
        if (!brighter.containsKey(brightness)) {
            brighter.put(brightness,
                    new VisualPixel(this.getPixelDataList()
                            .stream()
                            .map(pixelData -> pixelData.changeBrightness(brightness))
                            .collect(Collectors.toList())));
        }
        return brighter.get(brightness);
    }


}

