package hero.inventoryWindow;

import com.googlecode.lanterna.TextColor;

import java.util.*;

public class InventoryText {
    public String title;
    public List<String> words;
    public List<TextColor> wordsColors;
    public List<String> info;
    public List<TextColor> infoColors;

//    public static InventoryText emptyText = new InventoryText("", "", new ArrayList<>(), new ArrayList<>());

    private static TextColor def = new TextColor.RGB(255, 255, 255);
    public static volatile InventoryText emptyText = new InventoryText("TITLE",
//            Arrays.asList("A description which has to be in text window".split(" ")),
//            Arrays.asList(def, TextColor.ANSI.RED, def, def, def, def, def, GREEN, GREEN),
            "This text is supposed to be long enough not to fit into description frame. Unfortunately it does now, so i should type more for this be bigger",
            Arrays.asList(def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def, def),
            Arrays.asList("[u]-use", "[ENTER]-take"),
            Arrays.asList(new TextColor.RGB(50, 50, 50), def)
    );

    public InventoryText(String title, String description, List<TextColor> wordsColors, List<String> info, List<TextColor> infoColors) {
        this.title = title;
        this.words = Arrays.asList(description.split(" "));
        this.wordsColors = wordsColors;
        this.info = info;
        this.infoColors = infoColors;
    }


    public InventoryText(String title, String description, List<String> info, List<TextColor> infoColors) {
        this(title, description, Collections.nCopies(description.split(" ").length, def), info, infoColors);
    }


    private Map<Integer, List<List<String>>> lines = new HashMap<>();

    public List<List<String>> lines(int width) {
        if (!lines.containsKey(width)) {
            List<List<String>> result = new ArrayList<>();
            List<List<TextColor>> colorsResult = new ArrayList<>();
            List<String> line = new ArrayList<>();
            List<TextColor> colorsLine = new ArrayList<>();
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
