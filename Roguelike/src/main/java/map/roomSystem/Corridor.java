package map.roomSystem;

import map.MapOfObjects;
import map.roomSystem.textures.RoomTextures;

public class Corridor extends Passage {

    @Override
    public boolean passable(int width) {
        return this.width >= width;
    }

    public Corridor(Room room1, Room room2, RoomTextures textures) {
        super(room1, room2, textures);
    }

    public Corridor(Room room1, Room room2, RoomTextures textures, int width, int bias) {
        super(room1, room2, textures, width, bias);
    }

    public Corridor(Room room1, Room room2, RoomTextures textures, int width) {
        this(room1, room2, textures);
        int bias = (this.width - width) / 2;
        setWidthAndBias(width, bias);
    }
}
