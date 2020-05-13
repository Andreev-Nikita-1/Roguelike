package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import inventory.Inventory;
import mapGenerator.DungeonGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;


import static basicComponents.GameplayLogic.GameplayState.*;

public class GameplayLogic {
    public static GameplayState gameplayState = NOT_STARTED;

    public static MapOfObjects currentMap;
    public static MapRenderer currentMapRenderer;

    public static Inventory inventory = new Inventory();

    private static int xSize = 200;
    private static int ySize = 200;

    private GameplayLogic() {
    }

    public static void createMap(MapGenerator mapGenerator) {
        currentMap = mapGenerator.generateMap();
        currentMapRenderer = new MapRenderer(currentMap).fit();
        currentMap.start();
    }

    public static void pause() {
        currentMap.pause();
        gameplayState = PAUSED;
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

    public static void handleOption(int id, GameplayOption option, long eventTine) {
        if (option == GameplayOption.INTERACT) {
            AppLogic.client.doHeroAction(
                    Model.HeroAction.newBuilder()
                            .setHeroId(AppLogic.id)
                            .setType(Model.HeroAction.Type.INTERACT)
                            .setAction(Model.HeroAction.Action.ATTACK)
                            .setDirection(Model.HeroAction.Direction.DOWN)
                            .setEventTime(2281488)
                            .build()
            );
        }
        if (option instanceof DirectedOption) {
            Model.HeroAction.Action action = null;
            switch (((DirectedOption) option).action) {
                case WALK:
                    action = Model.HeroAction.Action.WALK;
                    break;
                case RUN:
                    action = Model.HeroAction.Action.RUN;
                    break;
                case ATTACK:
                    action = Model.HeroAction.Action.ATTACK;
                    currentMap.heroObjects[id].makeAttack(((DirectedOption) option), eventTine);
                    break;
            }
            Model.HeroAction.Direction direction = null;
            switch (((DirectedOption) option).direction) {
                case UP:
                    direction = Model.HeroAction.Direction.UP;
                    break;
                case DOWN:
                    direction = Model.HeroAction.Direction.DOWN;
                    break;
                case RIGHT:
                    direction = Model.HeroAction.Direction.RIGHT;
                    break;
                case LEFT:
                    direction = Model.HeroAction.Direction.LEFT;
                    break;
            }
            AppLogic.client.doHeroAction(
                    Model.HeroAction.newBuilder()
                            .setHeroId(AppLogic.id)
                            .setType(Model.HeroAction.Type.DIRECTED_ACTION)
                            .setAction(action)
                            .setDirection(direction)
                            .setEventTime(2281488)
                            .build()
            );
        }
    }

    public enum GameplayState {
        NOT_STARTED, MAP_GENERATING, PLAYING, PAUSED
    }

}


