package menuLogic;

import basicComponents.AppLogic;
import basicComponents.Controller;

public class RealAction extends MenuAction {


    public static RealAction continueGameAction = new RealAction("continue", AppLogic::applyContinueAction);
    public static RealAction saveGameAction = new RealAction("save game", Controller::drawSaveGameDialog);
    public static RealAction exit = new RealAction("exit", AppLogic::applyExitAction);
    public static RealAction newGameAction = new RealAction("new game", AppLogic::applyNewGame);
    public static RealAction loadGameAction = new RealAction("load game", Controller::drawFileDialog);
    public static RealAction zoomIn = new RealAction("zoom in", Controller::zoomIn);
    public static RealAction zoomOut = new RealAction("zoom out", Controller::zoomOut);
    public static RealAction zoomDefault = new RealAction("default", Controller::zoomDefault);

    public static RealAction back = new RealAction("back", () -> Controller.drawMenu(Controller.lastMenu));

    public RealAction(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }
}
