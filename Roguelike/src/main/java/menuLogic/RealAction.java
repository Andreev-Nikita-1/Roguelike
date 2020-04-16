package menuLogic;

import basicComponents.AppLogic;

public class RealAction extends MenuAction {


    public static RealAction continueGameAction = new RealAction("continue", AppLogic::applyContinueAction);
    public static RealAction exit = new RealAction("exit", AppLogic::applyExitAction);
    public static RealAction level1Action = new RealAction("level 1", AppLogic::applyLevel1Action);
    public static RealAction level2Action = new RealAction("level 2", AppLogic::applyLevel2Action);
    public static RealAction level3Action = new RealAction("level 3", AppLogic::applyLevel3Action);

    public RealAction(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }
}
