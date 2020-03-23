package map.shapes;

import map.Coord;

import java.util.ArrayList;
import java.util.List;

public class CustomStaticShape extends Shape {
    protected final List<Coord> shifts;

    @Override
    public List<Coord> getShifts() {
        return shifts;
    }

    public CustomStaticShape(List<Coord> shifts) {
        this.shifts = shifts;
    }

    public CustomStaticShape(int[][] array) {
        this.shifts = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] > 0) {
                    shifts.add(new Coord(i, j));
                }
            }
        }
    }
}
