package inventory.items;

public interface Takeable {
    void setTakenStatus(boolean taken);

    default Type getType() {
        return Type.USABLE;
    }

    enum Type {
        USABLE, WEAPON, SHIELD, BOOTS
    }
}
