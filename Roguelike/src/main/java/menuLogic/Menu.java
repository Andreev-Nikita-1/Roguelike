package menuLogic;

import java.util.ArrayList;
import java.util.List;

import static menuLogic.LinkAction.*;
import static menuLogic.RealAction.*;

public class Menu {

    public static Menu mainMenu = new Menu("MAIN MENU");
    public static Menu activeGameMainMenu = new Menu("MAIN MENU");
    public static Menu optionsMenu = new Menu("OPTIONS");
    public static Menu youDied = new Menu("YOU DIED");
    public static Menu success = new Menu("SUCCESS");

    public static void InitializeMenus() {
        mainMenu.addAction(newGameAction);
        mainMenu.addAction(loadGameAction);
        mainMenu.addAction(optionsAction);
        mainMenu.addAction(exit);

        activeGameMainMenu.addAction(continueGameAction);
        activeGameMainMenu.addAction(saveGameAction);
        activeGameMainMenu.addAction(loadGameAction);
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

    public Menu(String title) {
        this.title = title;
    }

    public void addAction(MenuAction action) {
        this.actions.add(action);
    }

    public List<MenuAction> getActions() {
        return actions;
    }

    public String getTitle() {
        return title;
    }


}

