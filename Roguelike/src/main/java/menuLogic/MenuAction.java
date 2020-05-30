package menuLogic;

/**
 * Class, representing menu action
 */
public abstract class MenuAction {

    protected String name;
    protected Runnable action;

    public String getName() {
        return name;
    }

    public Runnable getAction() {
        return action;
    }
}
