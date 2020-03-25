package map.shapes;

import map.Coord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Shape {
    public static Shape SINGLE_PIXEL_SHAPE = new Shape() {

        @Override
        public List<Coord> getShifts() {
            return new ArrayList<>(Arrays.asList(Coord.ZERO));
        }
    };
    public static Shape EMPTY_SHAPE = new Shape() {
        @Override
        public List<Coord> getShifts() {
            return new ArrayList<>();
        }
    };

    public abstract List<Coord> getShifts();
}
