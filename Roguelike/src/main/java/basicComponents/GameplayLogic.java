package basicComponents;

import gameplayOptions.DirectedOption;
import gameplayOptions.GameplayOption;
import mapGenerator.DungeonGenerator;
import mapGenerator.MapGenerator;
import map.MapOfObjects;
import renderer.MapRenderer;


import static basicComponents.GameplayLogic.GameplayState.*;

public class GameplayLogic {
    public static GameplayState gameplayState = NOT_STARTED;

    public static MapOfObjects currentMap;
    public static MapRenderer currentMapRenderer;


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
                    util.Model.HeroAction.newBuilder()
                            .setHeroId(AppLogic.id)
                            .setType(util.Model.HeroAction.Type.INTERACT)
                            .setAction(util.Model.HeroAction.Action.ATTACK)
                            .setDirection(util.Model.HeroAction.Direction.DOWN)
                            .setEventTime(2281488)
                            .build()
            );
        }
        if (option instanceof DirectedOption) {
            util.Model.HeroAction.Action action = null;
            switch (((DirectedOption) option).action) {
                case WALK:
                    action = util.Model.HeroAction.Action.WALK;
                    break;
                case RUN:
                    action = util.Model.HeroAction.Action.RUN;
                    break;
                case ATTACK:
                    action = util.Model.HeroAction.Action.ATTACK;
                    currentMap.heroObjects[id].makeAttack(((DirectedOption) option), eventTine);
                    break;
            }
            util.Model.HeroAction.Direction direction = null;
            switch (((DirectedOption) option).direction) {
                case UP:
                    direction = util.Model.HeroAction.Direction.UP;
                    break;
                case DOWN:
                    direction = util.Model.HeroAction.Direction.DOWN;
                    break;
                case RIGHT:
                    direction = util.Model.HeroAction.Direction.RIGHT;
                    break;
                case LEFT:
                    direction = util.Model.HeroAction.Direction.LEFT;
                    break;
            }
            AppLogic.client.doHeroAction(
                    util.Model.HeroAction.newBuilder()
                            .setHeroId(AppLogic.id)
                            .setType(util.Model.HeroAction.Type.DIRECTED_ACTION)
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


