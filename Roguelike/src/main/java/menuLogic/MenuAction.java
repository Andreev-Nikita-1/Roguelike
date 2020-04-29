package menuLogic;

public abstract class MenuAction {

    protected String name;
    protected Runnable action;

    public String getName() {
        return name;
    }

    public Runnable getAction() {
        return action;
    }

    public void setName(String name) {
        this.name = name;
    }
}
