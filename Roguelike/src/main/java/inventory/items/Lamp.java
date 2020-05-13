package inventory.items;

import com.googlecode.lanterna.TextColor;
import inventory.InventoryText;
import inventory.VerticalScale;

import java.util.Arrays;


public class Lamp extends DynamicItem implements Takeable {
    private int fuel = 5000;
    private int maxFuel = 1000;
    private boolean switchedOn = true;


    public VerticalScale scale = new VerticalScale(1, new TextColor.RGB(255, 200, 50));


    public Lamp() {
        symbol = 'L';
    }

    private void switchOn() {
        switchedOn = true;
        owner.heroMap.lighting.turnOffDarkness();
        updateLighting();
    }

    private void switchOff() {
        switchedOn = false;
        owner.heroMap.lighting.turnOnDarkness();
        updateLighting();
    }

    private void updateLighting() {
        if (switchedOn) {
            owner.heroMap.lighting.setRadius((fuel >= 500) ? 15 : 3 + fuel * 12.0 / 500);
            owner.heroMap.lighting.setDarknessLevel((fuel >= 200) ? 0 : 1 - fuel / 200.0);
        } else {
            owner.heroMap.lighting.setRadius(3);
        }
    }

    @Override
    public synchronized int act() {
        if (switchedOn) {
            fuel--;
            scale.level = (double) fuel / maxFuel;
            updateLighting();
            if (fuel <= 0) {
                switchOff();
            }
        }
        return 10;
    }


    @Override
    public TextColor getColor() {
        return null;
    }

    @Override
    public synchronized void use() {
        if (!switchedOn && fuel > 0) {
            switchOn();
        } else if (switchedOn) {
            switchOff();
        }
    }


    public volatile InventoryText text = new InventoryText("LAMP",
            "This kerosene lamp illuminates the surroundings",
            Arrays.asList(String.valueOf(scale.getCharacter().getCharacter())),
            Arrays.asList(scale.getCharacter().getForegroundColor())
    );

    @Override
    public InventoryText getText() {
        text.info = Arrays.asList(String.valueOf(scale.getCharacter().getCharacter()));
        text.infoColors = Arrays.asList(scale.getCharacter().getForegroundColor());
        return text;
    }

    @Override
    public void setTakenStatus(boolean status) {
        if (status) {
            if (!switchedOn)
                switchOn();
        } else {
            if (switchedOn)
                switchOff();
        }
    }

}
