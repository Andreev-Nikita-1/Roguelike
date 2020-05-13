package map.roomSystem;

public class Corridor extends Passage {

    @Override
    public boolean passable(int width) {
        return this.width >= width;
    }

    public Corridor(Room room1, Room room2) {
        super(room1, room2, 0);
    }

    public Corridor(Room room1, Room room2, int width, int bias) {
        super(room1, room2, 0, width, bias);
    }

    public Corridor(Room room1, Room room2, int width) {
        this(room1, room2);
        int bias = (this.width - width) / 2;
        setWidthAndBias(width, bias);
    }


    @Override
    public Corridor attachToMap() {
        super.attachToMap();
        return this;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
    }
}
