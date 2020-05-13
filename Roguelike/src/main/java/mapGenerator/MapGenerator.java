package mapGenerator;

import inventory.Hero;
import map.MapOfObjects;

public abstract class MapGenerator {
    public abstract MapOfObjects generateMap(Hero hero);
}
