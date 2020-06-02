package menuLogic;

import basicComponents.Controller;

import static menuLogic.Menu.*;

/**
 * LinkAction is used for going to another menu
 */
class LinkAction extends MenuAction {

    static LinkAction optionsAction = new LinkAction("options", optionsMenu);
    static LinkAction diedAction = new LinkAction("OK", mainMenu);
    static LinkAction okAction = new LinkAction("OK", activeGameMainMenu);

    LinkAction(String name, Menu linkMenu) {
        this.name = name;
        this.action = () -> Controller.drawMenu(linkMenu);
    }
}
