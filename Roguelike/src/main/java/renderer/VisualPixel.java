package renderer;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class consists of pixelData instances
 */
public class VisualPixel {

    private List<PixelData> pixelDataList;

    public VisualPixel(List<PixelData> pixelData) {
        this.pixelDataList = pixelData;
    }

    public VisualPixel(PixelData... pixelDatas) {
        pixelDataList = new ArrayList<>(Arrays.asList(pixelDatas));
    }

    List<PixelData> getPixelDataList() {
        return pixelDataList;
    }


    private static Map<Integer, VisualPixel> attack = new HashMap<>();

    /**
     * Returns VisualPixel instance for attack, which transparency depends on attack power
     */
    public static VisualPixel attack(int power) {
        if (!attack.containsKey(power)) {
            attack.put(power,
                    new VisualPixel(
                            new PixelData(false, 19, Color.RED, 1 - 1.0 / (1 + power / 10.0), ' ')));
        }
        return attack.get(power);
    }

    private static Map<Double, VisualPixel> darkness = new HashMap<>();


    /**
     * Returns VisualPixel instance for darkness, which transparency depends on darkness level
     */
    public static VisualPixel darkness(double transparency) {
        if (!darkness.containsKey(transparency)) {
            darkness.put(transparency, new VisualPixel(new PixelData(false, 20, Color.BLACK, transparency, ' ')));
        }
        return darkness.get(transparency);
    }

    private Map<VisualPixel, VisualPixel> combinedWith = new HashMap<>();

    /**
     * Concatenate two lists of PixelData
     */
    public VisualPixel combinedWith(VisualPixel other) {
        if (!combinedWith.containsKey(other)) {
            combinedWith.put(other, new VisualPixel(Stream
                    .concat(this.getPixelDataList().stream(), other.getPixelDataList().stream())
                    .collect(Collectors.toList())));
        }
        return combinedWith.get(other);
    }

    private Map<Color, Map<Double, VisualPixel>> highlighted = new HashMap<>();

    /**
     * Return copy of this, highlighted with given color
     */
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

    /**
     * Return copy of this, with brighter color
     */
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

