package menuLogic;

import basicComponents.Controller;

import static menuLogic.Menu.*;

public class LinkAction extends MenuAction {

    public static LinkAction newGameAction = new LinkAction("new game", levelSelector);
    public static LinkAction optionsAction = new LinkAction("options", optionsMenu);

    public static LinkAction back(Menu predMenu) {
        return new LinkAction("back", predMenu);
    }

    public LinkAction(String name, Menu linkMenu) {
        this.name = name;
        this.action = () -> Controller.drawMenu(linkMenu);
    }
}
