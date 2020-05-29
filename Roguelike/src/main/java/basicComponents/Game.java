package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import gameplayOptions.UseItemOption;
import hero.Hero;
import hero.Inventory;
import hero.items.Candles;
import hero.items.Shield;
import hero.items.Weapon;
import hero.stats.HeroStats;
import objects.stuff.Candle;
import org.json.JSONObject;
import hero.items.Item;
import hero.stats.StaminaRestorer;
import mapGenerator.DefaultGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;
import util.Pausable;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class Game {


    public AtomicBoolean paused = new AtomicBoolean(true);
    public List<Pausable> pausables = new CopyOnWriteArrayList<>();

    public MapOfObjects map;
    public MapRenderer mapRenderer;
    public Hero hero;

    public void start() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.start();
        }
    }

    public void pause() {
        paused.set(true);
        for (Pausable pausable : pausables) {
            pausable.pause();
        }
    }


    public void unpause() {
        paused.set(false);
        for (Pausable pausable : pausables) {
            pausable.unpause();
        }
    }


    public void kill() {
        paused.set(true);
        for (Pausable pausable : pausables) {
            pausable.kill();
        }
        pausables.clear();
    }

    public static JSONObject createNewGameSnapshot() {
        Game game = new Game();
        game.map = new DefaultGenerator(200, 200).generateMap();
        game.hero = new Hero(new Inventory(), new HeroStats());
        game.hero.inventory.taken[0] = new Candles();
        return game.getSnapshot();
    }

    public void handleOption(GameplayOption option, long eventTine) {
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


    public JSONObject getSnapshot() {
        return new JSONObject()
                .put("map", map.getSnapshot())
                .put("hero", hero.getSnapshot());
    }

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


