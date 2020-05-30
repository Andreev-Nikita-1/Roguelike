package renderer.inventoryWindow;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Class representing text for drawing in TextWindow
 */
public class InventoryText {
    String title;
    List<String> words;
    List<Color> wordsColors;
    List<String> info;
    List<List<Color>> infoWordsColors;

    public static final Color TEXT_COLOR = Color.WHITE;
    static final InventoryText EMPTY_TEXT = new InventoryText("", "", new ArrayList<>(), new ArrayList<>());
    static final InventoryText NO_WEAPON = new InventoryText("", "No weapon", new ArrayList<>(), new ArrayList<>());
    static final InventoryText NO_SHIELD = new InventoryText("", "No shield", new ArrayList<>(), new ArrayList<>());

    /**
     * @param title           - title on top
     * @param description     - text which goes next
     * @param wordsColors     - colors for every word in text
     * @param info            - info lines below, e.g. durability level for equipment
     * @param infoWordsColors - list which contains colors for every word in info line for all info lines
     */
    public InventoryText(String title,
                         String description,
                         List<Color> wordsColors,
                         List<String> info,
                         List<List<Color>> infoWordsColors) {
        this.title = title;
        this.words = Arrays.asList(description.split(" "));
        this.wordsColors = wordsColors;
        this.info = info;
        this.infoWordsColors = infoWordsColors;
    }


    /**
     * The same as previous, but text is colored by default
     */
    public InventoryText(String title,
                         String description,
                         List<String> info,
                         List<List<Color>> infoWordsColors) {
        this(title, description, Collections.nCopies(description.split(" ").length, TEXT_COLOR), info, infoWordsColors);
    }


    private Map<Integer, List<List<String>>> lines = new HashMap<>();

    /**
     * Splits text in lines, for every line to have length at most "width"
     */
    List<List<String>> lines(int width) {
        if (!lines.containsKey(width)) {
            List<List<String>> result = new ArrayList<>();
            List<String> line = new ArrayList<>();
            int currentLength = -1;
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                if (currentLength + 1 + word.length() > width) {
                    result.add(line);
                    line = new ArrayList<>();
                    line.add(word);
                    currentLength = word.length();
                } else {
                    line.add(word);
                    currentLength += 1 + word.length();
                }
            }
            result.add(line);
            lines.put(width, result);
        }
        return lines.get(width);
    }
}
