package menuLogic;

import basicComponents.Controller;

import static menuLogic.Menu.*;

public class LinkAction extends MenuAction {

    public static LinkAction optionsAction = new LinkAction("options", optionsMenu);
    public static LinkAction diedAction = new LinkAction("OK", mainMenu);
    public static LinkAction okAction = new LinkAction("OK", activeGameMainMenu);

    public LinkAction(String name, Menu linkMenu) {
        this.name = name;
        this.action = () -> Controller.drawMenu(linkMenu);
    }
}
