package inventory;

import objects.creatures.HeroObject;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class InventoryItem {
    public static final char SWORDS = (char) 9876;
    public static final char SKATES = (char) 9976;
    public static final char CROSS = (char) 9841;

    public static final InventoryItem WEAPON = new InventoryItem(SWORDS, "WEAPON: power+=10",
            (heroObject -> {
                heroObject.power += 10;
            }),
            (heroObject -> {
                heroObject.power -= 10;
            }));
    public static final InventoryItem SPEED = new InventoryItem(SKATES, "BOOTS: speed+=10",
            (heroObject -> {
                heroObject.updateSpeed(heroObject.speed + 10);
            }),
            (heroObject -> {
                heroObject.updateSpeed(heroObject.speed - 10);
            }));

    public static final InventoryItem FORTITUDE = new InventoryItem(CROSS, "CROSS: fortitude+=10",
            (heroObject -> {
                heroObject.fortitude += 10;
            }),
            (heroObject -> {
                heroObject.fortitude -= 10;
            }));


    public char symbol;
    public String name;
    public Consumer<HeroObject> effectOn;
    public Consumer<HeroObject> effectOff;
    public boolean taken = false;

    public InventoryItem(char symbol, String name, Consumer<HeroObject> effectOn, Consumer<HeroObject> effectOff) {
        this.symbol = symbol;
        this.name = name;
        this.effectOn = effectOn;
        this.effectOff = effectOff;
    }

}
