package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import gameplayOptions.UseItemOption;
import hero.Inventory;
import hero.items.Candles;
import hero.items.Shield;
import hero.items.Weapon;
import hero.stats.HeroStats;
import renderer.inventoryWindow.InventoryWindow;
import hero.items.Item;
import hero.stats.StaminaRestorer;
import mapGenerator.DungeonGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import util.Pausable;


import java.util.concurrent.atomic.AtomicInteger;

import static basicComponents.Game.GameplayState.*;

public class Game {
    public GameplayState gameplayState = NOT_STARTED;

    public MapOfObjects currentMap;
    public MapRenderer currentMapRenderer;
    public Inventory currentInventory;


    private static int xSize = 200;
    private static int ySize = 200;


    public void createMap(MapGenerator mapGenerator) {
        currentInventory = new Inventory(new HeroStats(0, 10, 100, new AtomicInteger(100), 100, 50, 10, 100, 50, 50, 0));
        currentInventory.taken[0] = new Candles();
        currentInventory.taken[0].setOwner(currentInventory);
        ((Pausable) currentInventory.taken[0]).includeToGame();
        new StaminaRestorer(currentInventory.stats).includeToGame();
        currentInventory.weapon = new Weapon(10, 50, 50, "Super knife", Weapon.Type.KNIFE);
        currentInventory.weapon.setOwner(currentInventory);
        currentInventory.shield = new Shield(500, 50, "Hyper shield", Shield.Type.SHIELD1);
        currentInventory.shield.setOwner(currentInventory);
        currentMap = mapGenerator.generateMap(currentInventory);
        currentMapRenderer = new MapRenderer(this).fit();
        currentInventory.setMap(currentMap);
    }

    public void start() {
        Pausable.startGame();
        gameplayState = PLAYING;
    }

    public void pause() {
        Pausable.pauseGame();
        gameplayState = PAUSED;
    }

    public void openInventory() {
        Pausable.pauseGame();
        InventoryWindow.activate();
        gameplayState = INVENTORY;
    }

    public void closeInventory() {
        Pausable.unpauseGame();
        InventoryWindow.deactivate();
        gameplayState = PLAYING;
    }

    public void unpause() {
        Pausable.unpauseGame();
        gameplayState = PLAYING;
    }


    public void createMapLevel1() {
        gameplayState = MAP_GENERATING;
        createMap(new DungeonGenerator(xSize, ySize));
        start();
    }

    public void handleOption(GameplayOption option, long eventTine) {
        if (option instanceof UseItemOption) {
            Item item = currentInventory.taken[((UseItemOption) option).num];
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

    public void handleKeyStrokeInInventory(KeyStroke keyStroke) {
        InventoryWindow.handleKeyStroke(keyStroke);
    }

    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED, INVENTORY
    }

}


