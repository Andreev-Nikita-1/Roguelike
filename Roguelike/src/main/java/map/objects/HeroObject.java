package map.objects;

import basicComponents.MapOfFirmObjects;
import hero.HeroLogic;
import map.*;

import java.util.HashMap;
import java.util.Map;

public class HeroObject extends Object implements MovableObject, AttackingObject {

    public Coord getLocation() {
        return location;
    }


    @Override
    public void act() {
    }

    @Override
    public void takeDamage(Damage damage) {
        HeroLogic.takeDamage(damage);
    }

    @Override
    public Map<Coord, LogicPixel> getPixels() {
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.HERO);
        return map;
    }


    @Override
    public void move(Direction direction) {
        Coord shift = Coord.fromDirection(direction);
        if (MapOfFirmObjects.inside(location.shifted(shift)) && !MapOfFirmObjects.isTaken(location.shifted(shift))) {
            shift(shift);
        }
    }

    @Override
    public void attack(Direction direction) {
        HeroLogic.attack(direction);
    }
}
