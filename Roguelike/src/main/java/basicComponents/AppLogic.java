package basicComponents;

import com.googlecode.lanterna.input.KeyStroke;
import gameplayOptions.DirectedOption;
import gameplayOptions.UseItemOption;
import gameplayOptions.GameplayOption;
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
     * Handles key, when on map or in inventory window
     */
    static void handleKeyStrokeOnMap(KeyStroke keyStroke) {
        if (gameplayState == PLAYING) {
            switch (keyStroke.getKeyType()) {
                case Escape:
                    currentGame.pause();
                    gameplayState = PAUSED;
                    Controller.drawMenu(activeGameMainMenu);
                    break;
                case Tab:
                    openInventory();
                    break;
                default:
                    GameplayOption option = getGameplayOption(keyStroke);
                    currentGame.handleOption(option, keyStroke.getEventTime());
                    break;
            }
        } else if (gameplayState == INVENTORY) {
            switch (keyStroke.getKeyType()) {
                case Escape:
                case Tab:
                    closeInventory();
                    break;
                default:
                    InventoryWindow.handleKeyStroke(keyStroke);
                    break;
            }
        }
    }

    /**
     * Returns gameplay option from key
     */
    private static GameplayOption getGameplayOption(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case Character:
                char character = keyStroke.getCharacter();
                if (character == ' ') {
                    return GameplayOption.INTERACT;
                }
                if (character >= '1' && character <= '4') {
                    return new UseItemOption(character - '1');
                }
            case ArrowDown:
                if (alt) return DirectedOption.ATTACK_DOWN;
                if (shift) return DirectedOption.RUN_DOWN;
                else return DirectedOption.WALK_DOWN;
            case ArrowUp:
                if (alt) return DirectedOption.ATTACK_UP;
                if (shift) return DirectedOption.RUN_UP;
                else return DirectedOption.WALK_UP;
            case ArrowLeft:
                if (alt) return DirectedOption.ATTACK_LEFT;
                if (shift) return DirectedOption.RUN_LEFT;
                else return DirectedOption.WALK_LEFT;
            case ArrowRight:
                if (alt) return DirectedOption.ATTACK_RIGHT;
                if (shift) return DirectedOption.RUN_RIGHT;
                else return DirectedOption.WALK_RIGHT;
            default:
                return GameplayOption.NOTHING;
        }
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
     * Restores and starts game from snapshot
     */
    private static void restoreGame(JSONObject jsonObject) {
        try {
            currentGame = Game.restoreFromSnapshot(jsonObject);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        currentGame.start();
        gameplayState = PLAYING;
    }

    /**
     * Starts new game
     */
    public static void applyNewGame() {
        if (currentGame != null) {
            currentGame.kill();
        }
        restoreGame(Game.createNewGameSnapshot());
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

    /**
     * Loads and restores game from save
     */
    static void loadGame(String name) {
        if (currentGame != null) {
            currentGame.kill();
        }
        File[] saves = new File("src/main/resources/saves").listFiles();
        for (File file : saves) {
            if (file.getName().equals(name + ".json")) {
                FileReader reader = null;
                try {
                    reader = new FileReader(file);
                    JSONParser parser = new JSONParser();
                    restoreGame(new JSONObject(parser.parse(reader).toString()));
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns list of saves
     */
    static List<SaveInfo> getSaves() {
        List<SaveInfo> saveInfos = new ArrayList<>();
        File[] saves = new File("src/main/resources/saves").listFiles();
        for (File file : saves) {
            Map<String, String> map = new HashMap<>();
            long lastModified = file.lastModified();
            Date date = new Date(lastModified);
            String dateText = new SimpleDateFormat("HH:mm:ss").format(date);
            String[] nums = dateText.split(":");
            dateText = "";
            dateText += tightNumber(Integer.parseInt(nums[0]) + 100).substring(1) + ":";
            dateText += tightNumber(Integer.parseInt(nums[1]) + 100).substring(1) + ":";
            dateText += tightNumber(Integer.parseInt(nums[2]) + 100).substring(1);
            saveInfos.add(new SaveInfo(lastModified, dateText, file.getName().substring(0, file.getName().length() - 5)));
        }
        saveInfos.sort((info1, info2) -> (info1.time > info2.time) ? -1 : 1);
        return saveInfos;
    }

    /**
     * Class for save name and date
     */
    public static class SaveInfo {
        long time;
        String date;
        String name;

        SaveInfo(long time, String date, String name) {
            this.time = time;
            this.date = date;
            this.name = name;
        }
    }
}



