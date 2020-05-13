package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import gameplayOptions.UseItemOption;
import inventory.Hero;
import inventory.InventoryWindow;
import inventory.items.Item;
import mapGenerator.DungeonGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;
import com.googlecode.lanterna.input.KeyStroke;


import java.util.concurrent.atomic.AtomicInteger;

import static basicComponents.GameplayLogic.GameplayState.*;

public class GameplayLogic {
    public static GameplayState gameplayState = NOT_STARTED;

    public static MapOfObjects currentMap;
    public static MapRenderer currentMapRenderer;
    public static Hero currentHero;


    private static int xSize = 200;
    private static int ySize = 200;

    private GameplayLogic() {
    }

    public static void createMap(MapGenerator mapGenerator) {
        currentHero = new Hero(0, 0, 0, new AtomicInteger(100), 0, 50, 10, 100, 50, 0);
        currentMap = mapGenerator.generateMap(currentHero);
        currentMapRenderer = new MapRenderer(currentMap).fit();
        currentHero.setMap(currentMap);
        currentMap.start();
    }

    public static void pause() {
        currentMap.pause();
        gameplayState = PAUSED;
    }

    public static void openInventory() {
        currentMap.pause();
        InventoryWindow.activate();
        gameplayState = INVENTORY;
    }

    public static void closeInventory() {
        currentMap.unpause();
        InventoryWindow.deactivate();
        gameplayState = PLAYING;
    }

    public static void unpause() {
        currentMap.unpause();
        gameplayState = PLAYING;
    }


    public static void createMapLevel1() {
        gameplayState = MAP_GENERATING;
        createMap(new DungeonGenerator(xSize, ySize));
        gameplayState = PLAYING;
    }

    public static void handleOption(GameplayOption option, long eventTine) {
        if (option instanceof UseItemOption) {
            Item item = currentHero.taken[((UseItemOption) option).num];
            if (item != null) item.use();
        }
        if (option == GameplayOption.INTERACT) {
            currentMap.heroObject.interactWith();
        }
        if (option instanceof DirectedOption) {
            switch (((DirectedOption) option).action) {
                case WALK:
                case RUN:
                    currentMap.heroObject.makeMovement((DirectedOption) option, eventTine);
                    break;
                case ATTACK:
                    currentMap.heroObject.makeAttack(((DirectedOption) option), eventTine);
                    break;
            }
        }
    }

    public static void handleKeyStrokeInInventory(KeyStroke keyStroke) {
        InventoryWindow.handleKeyStroke(keyStroke);
    }

    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED, INVENTORY
    }

}


