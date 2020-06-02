import basicComponents.Controller;
import menuLogic.Menu;


public class Main {
    /**
     * Entry point of the application
     */
    public static void main(String[] args) {
        Menu.InitializeMenus();
        Controller.update();
        Controller.drawMenu(Menu.mainMenu);
        Thread thread = new Thread(Controller::run);
        thread.start();
    }
}