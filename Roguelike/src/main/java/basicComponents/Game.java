package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import gameplayOptions.UseItemOption;
import hero.Hero;
import hero.Inventory;
import hero.items.Candles;
import hero.stats.HeroStats;
import org.json.JSONObject;
import hero.items.Item;
import hero.stats.StaminaRestorer;
import mapGenerator.DefaultGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;
import util.Pausable;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Class, representong the game. Contains hero and map, and
 */
public class Game {


    /**
     * State of game
     */
    public AtomicBoolean paused = new AtomicBoolean(true);
    /**
     * Objects, which have to be paused, when pausing game
     */
    public List<Pausable> pausables = new CopyOnWriteArrayList<>();

    /**
     * Current map
     */
    public MapOfObjects map;
    /**
     * Current map renderer
     */
    public MapRenderer mapRenderer;
    /**
     * Current hero
     */
    public Hero hero;

    /**
     * Method to start the game
     */
    void start() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.start();
        }
    }

    /**
     * Method to pause the game
     */
    void pause() {
        paused.set(true);
        for (Pausable pausable : pausables) {
            pausable.pause();
        }
    }

    /**
     * Method to unpause game
     */
    void unpause() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.unpause();
        }
    }

    /**
     * Method to kill the game
     */
    void kill() {
        paused.set(true);
        for (Pausable pausable : pausables) {
            pausable.kill();
        }
        pausables.clear();
    }

    /**
     * Creates snapshot of new game
     */
    static JSONObject createNewGameSnapshot() {
        Game game = new Game();
        game.map = new DefaultGenerator(200, 200).generateMap();
        game.hero = new Hero(new Inventory(), new HeroStats());
        game.hero.inventory.taken[0] = new Candles();
        return game.getSnapshot();
    }

    /**
     * Handles gameplay option
     */
    void handleOption(GameplayOption option, long eventTine) {
        if (option instanceof UseItemOption) {
            Item item = hero.inventory.taken[((UseItemOption) option).num];
            if (item != null) item.use();
        }
        if (option == GameplayOption.INTERACT) {
            map.heroObject.interactWith();
        }
        if (option instanceof DirectedOption) {
            switch (((DirectedOption) option).action) {
                case WALK:
                case RUN:
                    map.heroObject.makeMovement((DirectedOption) option, eventTine);
                    break;
                case ATTACK:
                    map.heroObject.makeAttack(((DirectedOption) option), eventTine);
                    break;
            }
        }
    }

    /**
     * Takes snapshot of game
     */
    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("map", map.getSnapshot())
                .put("hero", hero.getSnapshot());
    }

    /**
     * Returns game, restored from snapshot
     */
    public static Game restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Game game = new Game();
        game.hero = Hero.restoreFromSnapshot(jsonObject.getJSONObject("hero"));
        game.hero.inventory.includeToGame(game);
        game.map = MapOfObjects.restoreFromSnapshot(jsonObject.getJSONObject("map"), game);
        game.map.heroObject.setHero(game.hero);
        game.hero.inventory.setMap(game.map);
        new StaminaRestorer(game.hero.stats).includeToGame(game);
        game.mapRenderer = new MapRenderer(game.map).fit();
        return game;
    }
}


