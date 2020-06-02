package basicComponents;

import mapGenerator.DefaultGenerator;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import renderer.inventoryWindow.InventoryWindow;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;


import static basicComponents.AppLogic.GameplayState.*;
import static menuLogic.Menu.*;
import static util.Util.tightNumber;

/**
 * Class, responsible for application logic
 */
public class AppLogic {
    static final String MAIN_WINDOW_TITLE = "GAME";
    public static volatile boolean active = true;
    public static Game currentGame = new Game();
    public static GameplayState gameplayState = NOT_STARTED;

    private AppLogic() {
    }

    /**
     * Opens inventory
     */
    static void openInventory() {
        currentGame.pause();
        InventoryWindow.activate();
        gameplayState = INVENTORY;
    }

    /**
     * Closes inventory
     */
    static void closeInventory() {
        InventoryWindow.deactivate();
        currentGame.unpause();
        gameplayState = PLAYING;
    }

    /**
     * Ends game, when hero is killed
     */
    public static void endGame() {
        currentGame.kill();
        gameplayState = KILLED;
        Controller.drawMenu(youDied);
    }

    /**
     * Continues game
     */
    public static void applyContinueAction() {
        currentGame.unpause();
        gameplayState = PLAYING;
    }

    /**
     * Stops application
     */
    public static void applyExitAction() {
        System.exit(0);
    }

    /**
     * Possible states of the application
     */
    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED, INVENTORY, KILLED
    }

    /**
     * Starts new game
     */
    public static void applyNewGame() {
        if (currentGame != null) {
            currentGame.kill();
        }
        currentGame = Game.newGame(new DefaultGenerator(200, 200));
        currentGame.start();
        gameplayState = PLAYING;
    }

    /**
     * Saves game in file "name.json"
     */
    static void saveGame(String name) {
        String snapshot = currentGame.getSnapshot().toString();
        try {
            new File("src/main/resources/saves").mkdir();
            FileWriter file = new FileWriter("src/main/resources/saves/" + name + ".json");
            file.write(snapshot);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static JSONObject findInDirectory(File filepath, String name) throws IOException, ParseException {
        File[] saves = filepath.listFiles();
        for (File file : saves) {
            if (file.getName().equals(name + ".json")) {
                FileReader reader = new FileReader(file);
                JSONParser parser = new JSONParser();
                return new JSONObject(parser.parse(reader).toString());
            }
        }
        return null;
    }

    /**
     * Loads and restores game from save
     */
    static void loadGame(String name) {
        if (currentGame != null) {
            currentGame.kill();
        }
        try {
            JSONObject gameSnapshot = findInDirectory(new File("src/main/resources/saves"), name);
            currentGame = Game.restoreFromSnapshot(gameSnapshot);
        } catch (IOException | ParseException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        currentGame.start();
        gameplayState = PLAYING;
    }

    /**
     * Loads and creates game from map
     */
    static void loadMap(String name) {
        if (currentGame != null) {
            currentGame.kill();
        }
        JSONObject mapSnapshot;
        try {
            mapSnapshot = findInDirectory(new File("src/main/resources/maps"), name);
            currentGame = Game.createFromMapSnapshot(mapSnapshot);
        } catch (IOException | ParseException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        currentGame.start();
        gameplayState = PLAYING;
    }

    /**
     * Returns list of files (saves or maps)
     */
    static List<FileInfo> getFiles(File filepath) {
        List<FileInfo> fileInfos = new ArrayList<>();
        filepath.mkdir();
        File[] saves = filepath.listFiles();
        for (File file : saves) {
            long lastModified = file.lastModified();
            Date date = new Date(lastModified);
            String dateText = new SimpleDateFormat("HH:mm:ss").format(date);
            String[] nums = dateText.split(":");
            dateText = "";
            dateText += tightNumber(Integer.parseInt(nums[0]) + 100).substring(1) + ":";
            dateText += tightNumber(Integer.parseInt(nums[1]) + 100).substring(1) + ":";
            dateText += tightNumber(Integer.parseInt(nums[2]) + 100).substring(1);
            fileInfos.add(new FileInfo(lastModified, dateText, file.getName().substring(0, file.getName().length() - 5)));
        }
        fileInfos.sort((info1, info2) -> (info1.time > info2.time) ? -1 : 1);
        return fileInfos;
    }

    /**
     * Class for save name and date
     */
    static class FileInfo {
        long time;
        String date;
        String name;

        FileInfo(long time, String date, String name) {
            this.time = time;
            this.date = date;
            this.name = name;
        }
    }
}



