package menuLogic;

import basicComponents.AppLogic;
import basicComponents.Controller;


/**
 * Real action, which run some Runnable
 */
class RealAction extends MenuAction {


    static RealAction continueGameAction = new RealAction("continue", AppLogic::applyContinueAction);
    static RealAction saveGameAction = new RealAction("save game", Controller::drawSaveGameDialog);
    static RealAction exit = new RealAction("exit", AppLogic::applyExitAction);
    static RealAction newGameAction = new RealAction("new game", AppLogic::applyNewGame);
    static RealAction loadGameAction = new RealAction("load game", Controller::drawSavesDialog);
    static RealAction loadMap = new RealAction("load map", Controller::drawMapsDialog);
    static RealAction zoomIn = new RealAction("zoom in", Controller::zoomIn);
    static RealAction zoomOut = new RealAction("zoom out", Controller::zoomOut);
    static RealAction zoomDefault = new RealAction("default", Controller::zoomDefault);

    static RealAction back = new RealAction("back", () -> Controller.drawMenu(Controller.getLastMenu()));

    RealAction(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }
}
