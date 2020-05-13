package objects;

import map.MapOfObjects;
import objects.creatures.Creature;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;
import util.SomeLightingCodeFromInternet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static util.Coord.*;

public class InternetCodeBasedLighting extends MapObject implements VisualObject {

    private int radius;
    private SomeLightingCodeFromInternet code;
    private Set<Coord> visible = new HashSet<>();

    public InternetCodeBasedLighting(MapOfObjects map, int radius) {
        super(map);
        this.radius = radius;
        Function<Coord, Boolean> blocksLight = c -> !(map.inside(c) && (!map.isTaken(c) || (map.getObject(c) instanceof Creature)));
        Consumer<Coord> setVisible = c -> visible.add(c);
        Function<Coord, Integer> getDistance = c -> (int) Coord.euqlideanScaled(c).doubleValue();

        code = new SomeLightingCodeFromInternet(blocksLight, setVisible, getDistance);
    }


    private double transparency(double dist) {
        double t = Math.max(1.0 / ((dist / radius) * (dist / radius) + 1), 0.5);
        t = 2 - 2 * t;
        return t;
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        visible.clear();
        code.Compute(new Coord(map.getHeroLocation()), radius);
        Map<Coord, VisualPixel> pixelMap = new HashMap<>();
        for (int i = leftUp.x; i < rightDown.x; i++) {
            for (int j = leftUp.y; j < rightDown.y; j++) {
                Coord c = new Coord(i, j);
                double dist = 1000;
                if (!visible.contains(c)
                        && !visible.contains(c.shifted(UP))
                        && !visible.contains(c.shifted(DOWN))
                        && !visible.contains(c.shifted(Coord.LEFT))
                        && !visible.contains(c.shifted(Coord.RIGHT))) {
                } else {
                    if (!visible.contains(c)) {
                        for (Direction direction : Direction.getDirections()) {
                            Coord neighbour = c.shifted(Coord.fromDirection(direction));
                            if (visible.contains(neighbour)
                                    && (map.inside(neighbour)
                                    && (!map.isTaken(neighbour) || (map.getObject(neighbour) instanceof Creature)))) {
                                dist = Coord.euqlideanScaled(c.relative(map.getHeroLocation()));
                            }
                        }
                    } else {
                        dist = Coord.euqlideanScaled(c.relative(map.getHeroLocation()));
                    }
                }
                pixelMap.put(c, VisualPixel.darkness(transparency(dist)));
            }
        }
        return pixelMap;
    }
}
