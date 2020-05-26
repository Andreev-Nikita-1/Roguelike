package map.roomSystem;

import map.roomSystem.textures.RoomTextures;
import objects.*;
import renderer.PixelData;
import renderer.VisualPixel;
import util.Coord;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static map.roomSystem.Door.DoorState.*;
import static renderer.VisualPixel.*;

public class Door extends Passage implements DynamicVisualObject, InteractiveObject {
    private static Color DOOR_COLOR = new Color(21, 8, 3);
    private static final VisualPixel DOOR_CLOSED = new VisualPixel(
            new PixelData(true, 10, DOOR_COLOR, 1, (char) 0x01E3));
    private static final VisualPixel DOOR_OPEN_HORIZONTAL = new VisualPixel(
            new PixelData(true, 5, DOOR_COLOR, 1, (char) 0x01E6));
    private static final VisualPixel DOOR_OPEN_VERTICAl = new VisualPixel(
            new PixelData(true, 5, DOOR_COLOR, 1, (char) 0x01E5));


    protected Coord doorCoord;
    protected DoorState state = CLOSED;
    protected boolean highlighted = false;

    @Override
    public boolean passable(int width) {
        return width <= 1 && state == OPEN;
    }

    public Door(Room room1, Room room2, RoomTextures textures) {
        super(room1, room2, textures);
        setWidthAndBias(1, width / 2);
        setDepth((length - 1) / 2);
    }

    public Door(Room room1, Room room2, RoomTextures textures, int bias) {
        super(room1, room2, textures, 1, bias);
        setDepth((length - 1) / 2);
    }

    public Door(Room room1, Room room2, RoomTextures textures, int bias, int depth) {
        super(room1, room2, textures, 1, bias);
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
        map.unsubscribeFromCoords(this, doorCoord, 3);
        if (map.heroObject.interactiveObject == this) {
            map.heroObject.interactiveObject = null;
        }
    }

    @Override
    public Map<Coord, VisualPixel> getPixels(Coord leftUp, Coord rightDown) {
        Map<Coord, VisualPixel> pixels = new HashMap<>();
        if (doorCoord.between(leftUp, rightDown)) {
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
            if (highlighted) {
                //TODO
//                pixels.put(doorCoord, doorPixel.highlighted(Color.GREEN, 0.05));
                pixels.put(doorCoord, doorPixel.brighter(2.5));
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
