package map.roomSystem;

import com.googlecode.lanterna.TextColor;
import objects.*;
import renderer.VisualPixel;
import util.Coord;

import java.util.HashMap;
import java.util.Map;

import static map.roomSystem.Door.DoorState.*;
import static renderer.VisualPixel.*;

public class Door extends Passage implements DynamicVisualObject, InteractiveObject {
    protected Coord doorCoord;
    protected DoorState state = CLOSED;
    protected boolean highlighted = false;

    @Override
    public boolean passable(int width) {
        return width <= 1 && state == OPEN;
    }

    public Door(Room room1, Room room2) {
        super(room1, room2);
        setWidthAndBias(1, width / 2);
        setDepth((length - 1) / 2);
    }

    public Door(Room room1, Room room2, int bias) {
        super(room1, room2, 1, bias);
        setDepth((length - 1) / 2);
    }

    public Door(Room room1, Room room2, int bias, int depth) {
        super(room1, room2, 1, bias);
        setDepth(depth);
    }

    public void setDepth(int depth) {
        Coord shift = direction.vertical() ?
                new Coord(0, depth) : new Coord(depth, 0);
        doorCoord = location.shifted(shift);
    }

    public boolean closeDoor() {
        if (state == CLOSED) return true;
        map.getCoordLock(doorCoord).lock();
        try {
            if (map.setObject(this, doorCoord)) {
                state = CLOSED;
                return true;
            }
        } finally {
            map.getCoordLock(doorCoord).unlock();
        }
        return false;
    }


    public void openDoor() {
        if (state == OPEN) return;
        map.getCoordLock(doorCoord).lock();
        try {
            map.unsetObject(this, doorCoord);
            state = OPEN;
        } finally {
            map.getCoordLock(doorCoord).unlock();
        }
    }

    @Override
    public void interact() {
        switch (state) {
            case OPEN:
                closeDoor();
                break;
            case CLOSED:
                openDoor();
                break;
        }
    }

    @Override
    public void update() {
        if (doorCoord.near(map.heroObject.getLocation())
                && !doorCoord.equals(map.heroObject.getLocation())) {
            highlighted = true;
            map.heroObject.interactiveObject = this;
        } else {
            highlighted = false;
            if (map.heroObject.interactiveObject == this) {
                map.heroObject.interactiveObject = null;
            }
        }
    }

    @Override
    public Door attachToMap() {
        super.attachToMap();
        map.setObject(this, doorCoord);
        map.subscribeOnCoords(this, doorCoord, 3);
        update();
        return this;
    }

    @Override
    public void deleteFromMap() {
        super.deleteFromMap();
        map.unsetObject(this, doorCoord);
        map.subscribeOnCoords(this, doorCoord, 3);
        if (map.heroObject.interactiveObject == this) {
            map.heroObject.interactiveObject = null;
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixels = new HashMap<>();
        VisualPixel doorPixel;
        switch (state) {
            case OPEN:
                doorPixel = direction.horizontal() ? DOOR_OPEN_HORIZONTAL : DOOR_OPEN_VERTICAl;
                break;
            case CLOSED:
                doorPixel = DOOR_CLOSED;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        if (doorCoord.between(leftUp, rightDown)) {
            if (highlighted) {
                pixels.put(doorCoord, doorPixel.highlighted(TextColor.ANSI.GREEN, 0.1));
            } else {
                pixels.put(doorCoord, doorPixel);
            }
        }
        return pixels;
    }

    protected enum DoorState {
        CLOSED,
        OPEN
    }
}
