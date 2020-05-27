package renderer.inventoryWindow;


import java.awt.*;
import java.util.*;
import java.util.List;

public class InventoryText {
    public String title;
    public List<String> words;
    public List<Color> wordsColors;
    public List<String> info;
    public List<List<Color>> infoWordsColors;

    public static final Color TEXT_COLOR = Color.WHITE;
    public static final InventoryText EMPTY_TEXT = new InventoryText("", "", new ArrayList<>(), new ArrayList<>());
    public static final InventoryText NO_WEAPON = new InventoryText("", "No weapon", new ArrayList<>(), new ArrayList<>());
    public static final InventoryText NO_SHIELD = new InventoryText("", "No shield", new ArrayList<>(), new ArrayList<>());

    public InventoryText(String title, String description, List<Color> wordsColors, List<String> info, List<List<Color>> infoWordsColors) {
        this.title = title;
        this.words = Arrays.asList(description.split(" "));
        this.wordsColors = wordsColors;
        this.info = info;
        this.infoWordsColors = infoWordsColors;
    }


    public InventoryText(String title, String description, List<String> info, List<List<Color>> infoWordsColors) {
        this(title, description, Collections.nCopies(description.split(" ").length, TEXT_COLOR), info, infoWordsColors);
    }


    private Map<Integer, List<List<String>>> lines = new HashMap<>();

    public List<List<String>> lines(int width) {
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
