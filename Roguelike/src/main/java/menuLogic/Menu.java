package menuLogic;

import java.util.ArrayList;
import java.util.List;

import static menuLogic.LinkAction.*;
import static menuLogic.RealAction.*;

public class Menu {

    public static Menu mainMenu = new Menu("MAIN MENU");

    public static Menu levelSelector = new Menu("SELECT LEVEL");

    public static Menu optionsMenu = new Menu("OPTIONS");

    public static void InitializeMenus() {
        mainMenu.addAction(newGameAction);
        mainMenu.addAction(optionsAction);
        mainMenu.addAction(exit);

        levelSelector.addAction(level1Action);
        levelSelector.addAction(back(mainMenu));


        optionsMenu.addAction(zoomIn);
        optionsMenu.addAction(zoomOut);
        optionsMenu.addAction(zoomDefault);
        optionsMenu.addAction(back(mainMenu));

    }

    private String title;
    private List<MenuAction> actions = new ArrayList<>();

    public Menu(String title) {
        this.title = title;
    }

    public void addAction(int index, MenuAction action) {
        this.actions.add(index, action);
    }

    public void deleteAction(int index) {
        this.actions.remove(index);
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

