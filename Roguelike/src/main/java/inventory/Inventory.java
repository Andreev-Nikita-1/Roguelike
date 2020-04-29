package inventory;

import basicComponents.Controller;
import basicComponents.GameplayLogic;
import menuLogic.RealAction;

import java.util.HashMap;
import java.util.Map;

import static menuLogic.Menu.inventory;

public class Inventory {
    private Map<InventoryItem, RealAction> actionMap = new HashMap<>();
    private InventoryItem taken;

    public void addItem(InventoryItem item) {
        RealAction action = new RealAction(item.symbol + item.name,
                () -> {
                    if (taken != item) {
                        if (taken != null) {
                            taken.effectOff.accept(GameplayLogic.currentMap.heroObject);
                            taken.taken = false;
                        }
                        item.effectOn.accept(GameplayLogic.currentMap.heroObject);
                        item.taken = true;
                        taken = item;
                    } else {
                        item.effectOff.accept(GameplayLogic.currentMap.heroObject);
                        item.taken = false;
                        taken = null;
                    }
                    updateNames();
                    Controller.drawMenu(inventory);
                });
        actionMap.put(item, action);
        inventory.addAction(0, action);
    }

    private void updateNames() {
        for (InventoryItem item : actionMap.keySet()) {
            actionMap.get(item).setName(item.symbol + item.name + (item.taken ? ((char) 10003) : ' '));
        }
    }
}
