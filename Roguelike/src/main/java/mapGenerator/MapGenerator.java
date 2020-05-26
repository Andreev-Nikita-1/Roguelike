package mapGenerator;

import hero.Inventory;
import map.MapOfObjects;

public abstract class MapGenerator {
    public abstract MapOfObjects generateMap(Inventory inventory);
}
