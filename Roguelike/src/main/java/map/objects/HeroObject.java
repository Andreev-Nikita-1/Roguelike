package map.objects;

import map.MapOfObjects;
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
    public Map<Coord, LogicPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, LogicPixel> map = new HashMap<>();
        map.put(location, LogicPixel.HERO);
//        {
//            map.put(location.shifted(Coord.DOWN), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.UP), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.LEFT), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.RIGHT), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.DOWN).shifted(Coord.LEFT), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.UP).shifted(Coord.RIGHT), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.LEFT).shifted(Coord.UP), LogicPixel.LIGHT);
//            map.put(location.shifted(Coord.RIGHT).shifted(Coord.DOWN), LogicPixel.LIGHT);
//
//        }
//
        for (int i = leftUp.x; i < rightDown.x; i++) {
            for (int j = leftUp.y; j < rightDown.y; j++) {
                int d = (int) (Math.pow((location.x - i), 2) + Math.pow(2 * (location.y - j), 2));

                if (d > 150) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_FULL);
                } else if (d > 75) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_2);
                } else if (d > 15) {
                    map.put(new Coord(i, j), LogicPixel.DARKNESS_1);
                }
            }
        }


        return map;
    }


    @Override
    public void move(Direction direction) {
        Coord shift = Coord.fromDirection(direction);
        if (MapOfObjects.inside(location.shifted(shift)) && !MapOfObjects.isTaken(location.shifted(shift))) {
            shift(shift);
        }
    }

    @Override
    public void attack(Direction direction) {
        HeroLogic.attack(direction);
    }
}
