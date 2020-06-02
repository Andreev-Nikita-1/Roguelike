package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import gameplayOptions.UseItemOption;
import hero.Hero;
import hero.Inventory;
import items.Candles;
import hero.stats.HeroStats;
import mapGenerator.MapGenerator;
import objects.Lighting;
import org.json.JSONObject;
import items.Item;
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
 * Class, representing the game. Contains hero and map
 */
public class Game {


    /**
     * State of game
     */
    private AtomicBoolean paused = new AtomicBoolean(true);

    public boolean isPaused() {
        return paused.get();
    }

    /**
     * Objects, which have to be paused, when pausing game
     */
    private List<Pausable> pausables = new CopyOnWriteArrayList<>();

    public void addPausable(Pausable pausable) {
        pausables.add(pausable);
    }

    public void removePausable(Pausable pausable) {
        pausables.remove(pausable);
    }

    /**
     * Current map
     */
    private MapOfObjects map;
    /**
     * Current map renderer
     */
    private MapRenderer mapRenderer;

    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    /**
     * Current hero
     */
    private Hero hero;

    public Hero getHero() {
        return hero;
    }

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


    private static Game constructGame(Hero hero, MapOfObjects map) {
        Game game = new Game();
        game.hero = hero;
        hero.inventory.includeToGame(game);
        game.map = map;
        map.lighting = (Lighting) new Lighting(3).attachToMap(map);
        map.setGame(game);
        map.includeToGame(game);
        game.map.heroObject.setHero(game.hero);
        game.hero.inventory.setMap(game.map);
        new StaminaRestorer(game.hero.stats).includeToGame(game);
        game.mapRenderer = new MapRenderer(game.map).fit();
        return game;
    }

    /**
     * Creates new game
     */
    static Game newGame(MapGenerator mapGenerator) {
        Hero hero = new Hero(new Inventory(), new HeroStats());
        hero.inventory.taken[0] = new Candles();
        hero.inventory.taken[0].setOwnerInventory(hero.inventory);
        MapOfObjects map = mapGenerator.generateMap();
        return constructGame(hero, map);
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
    static Game restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return constructGame(Hero.restoreFromSnapshot(jsonObject.getJSONObject("hero")),
                MapOfObjects.restoreFromSnapshot(jsonObject.getJSONObject("map")));
    }

    /**
     * Creates game, restored from map snapshot
     */
    static Game createFromMapSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Hero hero = new Hero(new Inventory(), new HeroStats());
        hero.inventory.taken[0] = new Candles();
        hero.inventory.taken[0].setOwnerInventory(hero.inventory);
        return constructGame(hero,
                MapOfObjects.restoreFromSnapshot(jsonObject));
    }
}


