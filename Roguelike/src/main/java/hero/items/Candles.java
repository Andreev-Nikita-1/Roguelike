package hero.items;

import basicComponents.Game;
import org.json.JSONObject;
import renderer.inventoryWindow.InventoryText;
import util.Coord;
import util.TimeIntervalActor;
import util.Util;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import static renderer.inventoryWindow.InventoryText.TEXT_COLOR;


public class Candles extends Item implements TimeIntervalActor {
    private final Color COLOR = new Color(255, 215, 131);

    private int currentLevel = 10000;
    private int maxLevel = 10000;
    private int reservedCandles = 1;
    private boolean switchedOn = true;

    private AtomicBoolean active = new AtomicBoolean(true);

    public void takeOneCandle() {
        reservedCandles++;
    }

    @Override
    public void setActive(boolean active) {
        this.active.set(active);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public Game getGame() {
        return ownerInventory.heroMap.game;
    }

    public Candles() {
    }

    private Candles(int currentLevel, int maxLevel, int reservedCandles, boolean switchedOn) {
        this.currentLevel = currentLevel;
        this.maxLevel = maxLevel;
        this.reservedCandles = reservedCandles;
        this.switchedOn = switchedOn;
    }

    private void switchOn() {
        switchedOn = true;
        ownerInventory.heroMap.lighting.turnOffDarkness();
        updateLighting();
    }

    private void switchOff() {
        switchedOn = false;
        ownerInventory.heroMap.lighting.turnOnDarkness();
        updateLighting();
    }

    private void updateLighting() {
        if (switchedOn) {
            if (reservedCandles == 0) {
                ownerInventory.heroMap.lighting.setRadius((currentLevel >= 500) ? 15 : 3 + currentLevel * 12.0 / 500);
                ownerInventory.heroMap.lighting.setDarknessLevel((currentLevel >= 200) ? 0 : 1 - currentLevel / 200.0);
            } else {
                ownerInventory.heroMap.lighting.setRadius((currentLevel >= 500) ? 15 : 10 + currentLevel * 5.0 / 500);
                ownerInventory.heroMap.lighting.setDarknessLevel(0);
            }
        } else {
            ownerInventory.heroMap.lighting.setRadius(3);
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


    @Override
    public InventoryText getText() {
        return new InventoryText("CANDLE",
                "Candles will illuminate your path",
                Arrays.asList("left: " + Util.tightNumber(reservedCandles)),
                Arrays.asList(Collections.nCopies(5, TEXT_COLOR),
                        Arrays.asList(TEXT_COLOR, TEXT_COLOR)));
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

    @Override
    public JSONObject getSnapshot() {
        return super.getSnapshot()
                .put("currentLevel", currentLevel)
                .put("maxLevel", maxLevel)
                .put("reserved", reservedCandles)
                .put("switchedOn", switchedOn);
    }

    public static Candles restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (Candles) new Candles(
                jsonObject.getInt("currentLevel"),
                jsonObject.getInt("maxLevel"),
                jsonObject.getInt("reserved"),
                jsonObject.getBoolean("switchedOn")
        ).setBaggagePlace(new Coord(jsonObject.getInt("xBaggage"),
                jsonObject.getInt("yBaggage")));
    }
}
