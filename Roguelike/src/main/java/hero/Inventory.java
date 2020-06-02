package hero;

import basicComponents.Game;
import items.*;
import map.MapOfObjects;
import org.json.JSONArray;
import org.json.JSONObject;
import util.Coord;
import util.Direction;
import util.Pausable;

import java.lang.reflect.InvocationTargetException;

import static util.Coord.LEFT;
import static util.Coord.UP;

/**
 * Class for inventory. Contains all found items
 */
public class Inventory {
    public static final Coord baggageSize = new Coord(8, 5);
    /**
     * Baggage contains items, that is not taken
     */
    public Item[][] baggage = new Item[baggageSize.x][baggageSize.y];
    /**
     * Taken items can ve used by hero or affects stats
     */
    public Item[] taken = new Item[4];
    /**
     * Hero weapon
     */
    public Weapon weapon;
    /**
     * Hero shield
     */
    public Shield shield;

    public MapOfObjects heroMap;


    public void setMap(MapOfObjects map) {
        heroMap = map;
    }

    /**
     * Places item to baggage
     */
    public void take(Item item) {
        item.setOwnerInventory(this);
        if (item.baggagePlace != null) {
            Coord place = item.baggagePlace;
            if (baggage[place.x][place.y] == null) {
                baggage[place.x][place.y] = item;
                return;
            }
        }
        for (int j = 0; j < baggageSize.y; j++) {
            for (int i = 0; i < baggageSize.x; i++) {
                if (baggage[i][j] == null) {
                    baggage[i][j] = item.setBaggagePlace(new Coord(i, j));
                    return;
                }
            }
        }
    }

    /**
     * Swaps two next items in baggage
     */
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

    /**
     * Swaps two next taken items
     */
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

    /**
     * Takes item from baggage
     */
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

    /**
     * Puts taken item in baggage
     */
    public void takeOffItemInTaken(int num) {
        Item item = taken[num];
        if (item != null) {
            take(item);
            item.applyTakenEffect(false);
            taken[num] = null;
        }
    }

    /**
     * Puts taken weapon to baggage
     */
    public void takeOffWeapon() {
        if (weapon != null) {
            take(weapon);
            weapon.applyTakenEffect(false);
            weapon = null;
        }
    }

    /**
     * Puts taken shield to baggage
     */
    public void takeOffShield() {
        if (shield != null) {
            take(shield);
            shield.applyTakenEffect(false);
            shield = null;
        }
    }

    /**
     * Includes all pausable items to the game
     */
    public void includeToGame(Game game) {
        for (Item item : taken) {
            if (item instanceof Pausable) {
                ((Pausable) item).includeToGame(game);
            }
        }
        for (Item[] col : baggage) {
            for (Item item : col) {
                if (item instanceof Pausable) {
                    ((Pausable) item).includeToGame(game);
                }
            }
        }
        if (weapon instanceof Pausable) {
            ((Pausable) weapon).includeToGame(game);
        }
        if (shield instanceof Pausable) {
            ((Pausable) shield).includeToGame(game);
        }
    }

    /**
     * Takes candle
     */
    public void takeCandle() {
        for (Item item : taken) {
            if (item instanceof Candles) {
                ((Candles) item).takeOneCandle();
            }
        }
        for (Item[] col : baggage) {
            for (Item item : col) {
                if (item instanceof Candles) {
                    ((Candles) item).takeOneCandle();
                }
            }
        }
    }

    /**
     * Takes snapshot of inventory, recursively taking snapshots of items
     */
    public JSONObject getSnapshot() {
        JSONObject jsonObject = new JSONObject();
        JSONArray takenJson = new JSONArray();
        JSONArray baggageJson = new JSONArray();
        for (int i = 0; i < taken.length; i++) {
            takenJson.put((taken[i] == null) ? "empty" : taken[i].getSnapshot());
        }
        for (int i = 0; i < baggageSize.x; i++) {
            JSONArray col = new JSONArray();
            for (int j = 0; j < baggageSize.y; j++) {
                col.put((baggage[i][j] == null) ? "empty" : baggage[i][j].getSnapshot());
            }
            baggageJson.put(col);
        }
        jsonObject.put("weapon", (weapon == null) ? "empty" : weapon.getSnapshot());
        jsonObject.put("shield", (shield == null) ? "empty" : shield.getSnapshot());
        jsonObject.put("taken", takenJson);
        jsonObject.put("baggage", baggageJson);
        return jsonObject;
    }


    /**
     * Restores inventory from snapshot
     */
    public static Inventory restoreFromSnapshot(JSONObject jsonObject) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Inventory inventory = new Inventory();
        if (!jsonObject.get("weapon").toString().equals("empty")) {
            inventory.weapon = Weapon.restoreFromSnapshot(jsonObject.getJSONObject("weapon"));
            inventory.weapon.setOwnerInventory(inventory);
        }
        if (!jsonObject.get("shield").toString().equals("empty")) {
            inventory.shield = Shield.restoreFromSnapshot(jsonObject.getJSONObject("shield"));
            inventory.shield.setOwnerInventory(inventory);
        }
        for (int i = 0; i < inventory.taken.length; i++) {
            String itemStr = jsonObject.getJSONArray("taken").get(i).toString();
            if (!itemStr.equals("empty")) {
                Item item = Item.restoreSnapshot(new JSONObject(itemStr));
                inventory.taken[i] = item;
                item.setOwnerInventory(inventory);
            }
        }
        JSONArray baggageArray = jsonObject.getJSONArray("baggage");
        for (int i = 0; i < baggageSize.x; i++) {
            JSONArray col = baggageArray.getJSONArray(i);
            for (int j = 0; j < baggageSize.y; j++) {
                String itemStr = col.get(j).toString();
                if (!itemStr.equals("empty")) {
                    Item item = Item.restoreSnapshot(new JSONObject(itemStr));
                    inventory.take(item);
                }
            }
        }
        return inventory;
    }
}
