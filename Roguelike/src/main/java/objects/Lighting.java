package objects;

import basicComponents.Game;
import objects.creatures.Mob;
import renderer.VisualPixel;
import util.Coord;
import util.Direction;
import util.SomeLightingCodeFromInternet;
import util.TimeIntervalActor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import static util.Coord.*;

public class Lighting extends MapObject implements TimeIntervalActor, DynamicVisualObject {

    private double radius;
    private SomeLightingCodeFromInternet code;
    private Set<Coord> visible = new HashSet<>();

    public volatile boolean lighted = true;

    public Lighting(int radius) {
        super();
        this.radius = radius;
        Function<Coord, Boolean> blocksLight = c -> !(map.inside(c) && (!map.isTaken(c) || (map.getObject(c) instanceof Mob)));
        Consumer<Coord> setVisible = c -> visible.add(c);
        Function<Coord, Integer> getDistance = c -> (int) Coord.euqlidean(c).doubleValue();
        code = new SomeLightingCodeFromInternet(blocksLight, setVisible, getDistance);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setDarknessLevel(double darknessLevel) {
        this.darknessLevel = darknessLevel;
    }


    private double darknessLevel = 0;
    private double phase = 0;
    private double amplitude = 0.02;

    public void turnOnDarkness() {
        lighted = false;
        amplitude = 0.0;
        darknessLevel = 1.0;
    }

    public void turnOffDarkness() {
        lighted = true;
        amplitude = 0.02;
    }

    private double transparency(double dist) {
        double t = Math.max(1.0 / ((dist / radius) * (dist / radius) + 1), 0.5);
        t = 2 - 2 * t;
        if (t < 1)
            t = Math.min(Math.max(t + amplitude * Math.sin(phase / 5.0), 0), 1);
        return 1 - ((1 - t) * (1 - darknessLevel));
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        visible.clear();
        code.Compute(new Coord(map.getHeroLocation()), (int) Math.ceil(radius));
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
                            Coord neighbour = c.shifted(direction);
                            if (visible.contains(neighbour)
                                    && (map.inside(neighbour)
                                    && (!map.isTaken(neighbour) || (map.getObject(neighbour) instanceof Mob)))) {
                                dist = Coord.euqlidean(c.relative(map.getHeroLocation()));
                            }
                        }
                    } else {
                        dist = Coord.euqlidean(c.relative(map.getHeroLocation()));
                    }
                }
                pixelMap.put(c, VisualPixel.darkness(transparency(dist)));
            }
        }
        return pixelMap;
    }

    @Override
    public int act() {
        if (lighted) {
            phase += 2.0 * Math.random();
            return 75;
        } else {
            if (darknessLevel >= 0.99) {
                darknessLevel -= 0.0005;
            } else if (darknessLevel > 0.01) {
                darknessLevel -= 0.005 * darknessLevel * darknessLevel;
            }
            return 30;
        }
    }

    private AtomicBoolean paused = new AtomicBoolean(true);

    @Override
    public void setActive(boolean active) {
        this.paused.set(active);
    }

    @Override
    public boolean isActive() {
        return paused.get();
    }

    @Override
    public Game getGame() {
        return map.game;
    }

}
