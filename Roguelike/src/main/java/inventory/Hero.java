package inventory;

import inventory.items.*;
import map.MapOfObjects;
import util.Coord;
import util.Direction;
import util.Pausable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Hero implements Pausable {
    public int level;
    public int exp;
    public int maxExp;
    public AtomicInteger health;
    public int maxHealth;
    public int fortitude;
    public int power;
    public int attackDelay;
    public int speed;
    public int luck;

    public ScheduledExecutorService scheduler;

    public Item[][] baggage = new Item[9][2];
    public Item[] taken = new Item[3];
    public Item[] armor = new Item[3];

    private List<DynamicItem> dynamicItems = new ArrayList<>();

    public MapOfObjects heroMap;


    public void setMap(MapOfObjects map) {
        heroMap = map;
    }


    public Hero(int level, int exp, int maxExp, AtomicInteger health, int maxHealth, int fortitude, int power, int attackDelay, int speed, int luck) {
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
        this.health = health;
        this.maxHealth = maxHealth;
        this.fortitude = fortitude;
        this.power = power;
        this.attackDelay = attackDelay;
        this.speed = speed;
        this.luck = luck;
        scheduler = new ScheduledThreadPoolExecutor(7);
        taken[0] = new Lamp();
        dynamicItems.add((DynamicItem) taken[0]);
        taken[0].setOwner(this);
    }

    public void take(Item item) {
        item.setOwner(this);
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 9; i++) {
                if (baggage[i][j] == null) {
                    baggage[i][j] = item;
                    if (item instanceof DynamicItem) {
                        dynamicItems.add((DynamicItem) item);
                    }
                    return;
                }
            }
        }
    }

    public void shiftItemInBaggage(Coord current, Direction direction) {
        Item item = baggage[current.x][current.y];
        if (item == null) {
            return;
        }
        Coord next = current.shifted(direction);
        if (next.between(Coord.ZERO, new Coord(8, 1))) {
            Item other = baggage[next.x][next.y];
            baggage[next.x][next.y] = item;
            baggage[current.x][current.y] = other;
        }
    }

    public void shiftTakenItems(int num, Direction direction) {
        if (taken[num] == null
                || direction.vertical()
                || num == 0 && direction == Direction.LEFT
                || num == taken.length - 1 && direction == Direction.RIGHT) return;
        if (direction == Direction.LEFT) num--;
        Item temp = taken[num];
        taken[num] = taken[num + 1];
        taken[num + 1] = temp;
    }

    public void putOnItemInBaggage(Coord c) {
        Item item = baggage[c.x][c.y];
        if (item == null) {
            return;
        } else if (item instanceof Takeable) {
            int num = -1;
            if (((Takeable) item).getType() == Takeable.Type.WEAPON) num = 0;
            if (((Takeable) item).getType() == Takeable.Type.SHIELD) num = 1;
            if (((Takeable) item).getType() == Takeable.Type.BOOTS) num = 2;
            if (num != -1) {
                baggage[c.x][c.y] = armor[num];
                armor[num] = item;
            } else {
                boolean success = false;
                for (int i = 0; i < taken.length; i++) {
                    if (taken[i] == null) {
                        baggage[c.x][c.y] = null;
                        taken[i] = item;
                        success = true;
                        break;
                    }
                }
                if (!success) {
                    baggage[c.x][c.y] = taken[2];
                    taken[2] = item;
                }
            }
            if (baggage[c.x][c.y] != null) {
                ((Takeable) baggage[c.x][c.y]).setTakenStatus(false);
            }
            ((Takeable) item).setTakenStatus(true);
        }
    }

    public void takeOffItemInTaken(int num) {
        Item item = taken[num];
        if (item != null) {
            take(item);
            ((Takeable) item).setTakenStatus(false);
            taken[num] = null;
        }
    }

    public void takeOffItemInArmor(int num) {
        Item item = armor[num];
        if (item != null) {
            take(item);
            ((Takeable) item).setTakenStatus(false);
            armor[num] = null;
        }
    }


    //TODO
    @Override
    public Pausable start() {
        for (DynamicItem dynamicItem : dynamicItems) {
            dynamicItem.start();
        }
        return this;
    }

    @Override
    public void pause() {
        for (DynamicItem dynamicItem : dynamicItems) {
            dynamicItem.pause();
        }
    }

    @Override
    public void unpause() {
        for (DynamicItem dynamicItem : dynamicItems) {
            dynamicItem.unpause();
        }
    }

    @Override
    public void kill() {
        for (DynamicItem dynamicItem : dynamicItems) {
            dynamicItem.kill();
        }
    }
}
