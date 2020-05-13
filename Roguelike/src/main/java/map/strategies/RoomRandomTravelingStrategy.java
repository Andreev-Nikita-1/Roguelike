package map.strategies;

import map.MapOfObjects;
import map.roomSystem.Passage;
import map.roomSystem.Room;
import objects.creatures.Mob;
import util.Coord;

import java.util.ArrayList;
import java.util.List;

import static util.Util.generate;

public class RoomRandomTravelingStrategy extends RoomSwitchingStrategy {

    public RoomRandomTravelingStrategy(Mob owner) {
        super(owner, new RoomRandomIterator(owner.map, owner.getLocation()));
    }

    private static class RoomRandomIterator extends RoomPassageIterator {

        private boolean mustSwitchRooms;
        private int width = 1;

        public RoomRandomIterator(MapOfObjects map, Coord location) {
            Room room = map.closestRoom(location);
            currentRoom = room;
            mustSwitchRooms = false;
            next();
        }

        @Override
        public void next() {
            if (mustSwitchRooms) {
                currentRoom = currentPassage.otherSideRoom(currentRoom);
            } else {
                List<Passage> passables = new ArrayList<>();
                for (Passage passage : currentRoom.passages) {
                    if (passage.passable(width) && passage != currentPassage) {
                        passables.add(passage);
                    }
                }
                if (!passables.isEmpty()) {
                    currentPassage = generate(passables);
                }
            }
            mustSwitchRooms = !mustSwitchRooms;
        }
    }
}
