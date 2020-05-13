package menuLogic;

import basicComponents.AppLogic;
import basicComponents.Controller;

import java.util.concurrent.locks.Condition;

public class RealAction extends MenuAction {


    public static RealAction continueGameAction = new RealAction("continue", AppLogic::applyContinueAction);
    public static RealAction exit = new RealAction("exit", AppLogic::applyExitAction);
    public static RealAction level1Action = new RealAction("level 1", AppLogic::applyLevel1Action);
    public static RealAction level2Action = new RealAction("level 2", AppLogic::applyLevel2Action);
    public static RealAction level3Action = new RealAction("level 3", AppLogic::applyLevel3Action);
    public static RealAction zoomIn = new RealAction("zoom in", Controller::zoomIn);
    public static RealAction zoomOut = new RealAction("zoom out", Controller::zoomOut);
    public static RealAction zoomDefault = new RealAction("defaut", Controller::zoomDefault);


    public static RealAction runServer = new RealAction("create server", AppLogic::runServer);
    public static RealAction joinGame = new RealAction("join game", AppLogic::joinGame);

    public RealAction(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }
}
