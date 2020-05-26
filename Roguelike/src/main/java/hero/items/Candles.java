package hero.items;

import hero.inventoryWindow.InventoryText;
import util.TimeIntervalActor;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class Candles extends Item implements TimeIntervalActor {
    private final Color COLOR = new Color(255, 215, 131);

    private int currentLevel = 50000;
    private int maxLevel = 50000;
    private int reservedCandles = 1;
    private boolean switchedOn = true;

    private AtomicBoolean active = new AtomicBoolean(true);

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    public Candles() {
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
            if (reservedCandles == 0) {
                owner.heroMap.lighting.setRadius((currentLevel >= 500) ? 15 : 3 + currentLevel * 12.0 / 500);
                owner.heroMap.lighting.setDarknessLevel((currentLevel >= 200) ? 0 : 1 - currentLevel / 200.0);
            } else {
                owner.heroMap.lighting.setRadius((currentLevel >= 500) ? 15 : 10 + currentLevel * 5.0 / 500);
                owner.heroMap.lighting.setDarknessLevel(0);
            }
        } else {
            owner.heroMap.lighting.setRadius(3);
        }
    }

    @Override
    public synchronized int act() {
        if (switchedOn) {
            currentLevel--;
            if (currentLevel <= 0) {
                if (reservedCandles == 0) {
                    switchOff();
                } else {
                    reservedCandles--;
                    currentLevel = maxLevel;
                }
            }
            updateLighting();
        }
        return 10;
    }


    @Override
    public Color getColor() {
        return COLOR;
    }

    @Override
    public char getSymbol() {
        double x = currentLevel / (double) maxLevel;
        if (x == 0) return (char) 0x00D8;
        if (switchedOn)
            return (char) (0x00B3 + x * 30);
        else
            return (char) (0x00D9 + x * 30);
    }

    @Override
    public synchronized void use() {
        if (!switchedOn && currentLevel > 0) {
            switchOn();
        } else if (switchedOn) {
            switchOff();
        }
    }


    public volatile InventoryText text = new InventoryText("LAMP",
            "This candle illuminates your path",
            Arrays.asList(),
            Arrays.asList()
    );

    @Override
    public InventoryText getText() {
        text.info = Arrays.asList();
        text.infoColors = Arrays.asList();
        return text;
    }

    @Override
    public void applyTakenEffect(boolean status) {
        if (status) {
            if (!switchedOn)
                switchOn();
        } else {
            if (switchedOn)
                switchOff();
        }
    }

}
