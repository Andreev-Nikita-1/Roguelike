package hero;

import hero.items.*;
import hero.stats.HeroStats;
import map.MapOfObjects;
import util.Coord;
import util.Direction;

import static util.Coord.LEFT;
import static util.Coord.UP;

public class Inventory {
    public HeroStats stats;


    public static final Coord baggageSize = new Coord(8, 5);
    public Item[][] baggage = new Item[baggageSize.x][baggageSize.y];
    public Item[] taken = new Item[4];
    public Weapon weapon;
    public Shield shield;

    public MapOfObjects heroMap;


    public void setMap(MapOfObjects map) {
        heroMap = map;
    }


    public Inventory(HeroStats heroStats) {
        this.stats = heroStats;
        heroStats.setOwner(this);
    }

    private void updateStats() {

    }

    public void take(Item item) {
        item.setOwner(this);
        if (item.baggagePlace != null) {
            Coord place = item.baggagePlace;
            if (baggage[place.x][place.y] == null) {
                baggage[place.x][place.y] = item;
                return;
            }
        }
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 9; i++) {
                if (baggage[i][j] == null) {
                    baggage[i][j] = item;
                    item.baggagePlace = new Coord(i, j);
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
        if (next.between(Coord.ZERO, baggageSize.shifted(UP).shifted(LEFT))) {
            Item other = baggage[next.x][next.y];
            baggage[next.x][next.y] = item;
            item.baggagePlace = next;
            baggage[current.x][current.y] = other;
            if (other != null)
                other.baggagePlace = new Coord(current);
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
        }
        if (item instanceof Weapon) {
            baggage[c.x][c.y] = weapon;
            weapon = (Weapon) item;
        } else if (item instanceof Shield) {
            baggage[c.x][c.y] = shield;
            shield = (Shield) item;
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
                baggage[c.x][c.y] = taken[3];
                taken[3] = item;
            }
        }
        if (baggage[c.x][c.y] != null) {
            baggage[c.x][c.y].applyTakenEffect(false);
        }
        item.applyTakenEffect(true);
    }

    public void takeOffItemInTaken(int num) {
        Item item = taken[num];
        if (item != null) {
            take(item);
            item.applyTakenEffect(false);
            taken[num] = null;
        }
    }

    public void takeOffWeapon() {
        if (weapon != null) {
            take(weapon);
            weapon.applyTakenEffect(false);
            weapon = null;
        }
    }

    public void takeOffShield() {
        if (shield != null) {
            take(shield);
            shield.applyTakenEffect(false);
            shield = null;
        }
    }
}
