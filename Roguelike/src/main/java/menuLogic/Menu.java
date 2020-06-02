package menuLogic;

import java.util.ArrayList;
import java.util.List;

import static menuLogic.LinkAction.*;
import static menuLogic.RealAction.*;

/**
 * Class representing menu, containing option names and actions
 */
public class Menu {

    public static Menu mainMenu = new Menu("MAIN MENU");
    public static Menu activeGameMainMenu = new Menu("MAIN MENU");
    public static Menu optionsMenu = new Menu("OPTIONS");
    public static Menu youDied = new Menu("YOU DIED");
    public static Menu success = new Menu("SUCCESS");

    /**
     * Method that construct all menus with commands
     */
    public static void InitializeMenus() {
        mainMenu.addAction(loadGameAction);
        mainMenu.addAction(loadMap);
        mainMenu.addAction(newGameAction);
        mainMenu.addAction(optionsAction);
        mainMenu.addAction(exit);

        activeGameMainMenu.addAction(continueGameAction);
        activeGameMainMenu.addAction(saveGameAction);
        activeGameMainMenu.addAction(loadGameAction);
        activeGameMainMenu.addAction(loadMap);
        activeGameMainMenu.addAction(newGameAction);
        activeGameMainMenu.addAction(optionsAction);
        activeGameMainMenu.addAction(exit);

        youDied.addAction(diedAction);

        success.addAction(okAction);

        optionsMenu.addAction(zoomIn);
        optionsMenu.addAction(zoomOut);
        optionsMenu.addAction(zoomDefault);
        optionsMenu.addAction(back);

    }

    private String title;
    private List<MenuAction> actions = new ArrayList<>();

    Menu(String title) {
        this.title = title;
    }

    void addAction(MenuAction action) {
        this.actions.add(action);
    }

    public List<MenuAction> getActions() {
        return actions;
    }

    public String getTitle() {
        return title;
    }


}

