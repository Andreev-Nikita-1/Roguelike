package map.strategies;

import map.roomSystem.Passage;
import map.roomSystem.Room;
import objects.creatures.Mob;

import java.util.Iterator;
import java.util.List;


/**
 * Strategy, that leads mob along a given rout cyclically
 */
public class RoomCycleTravelingStrategy extends RoomSwitchingStrategy {
    public RoomCycleTravelingStrategy(Mob owner, List<Room> roomList, List<Passage> passageList) {
        super(owner, new RoomCycleIterator(roomList, passageList, true));
    }

    public RoomCycleTravelingStrategy(Mob owner, List<Room> roomList, List<Passage> passageList, boolean startFromRoom) {
        super(owner, new RoomCycleIterator(roomList, passageList, startFromRoom));
    }

    private static class RoomCycleIterator extends RoomPassageIterator {
        private boolean mustSwitchRooms;
        private List<Room> roomList;
        private Iterator<Room> roomIterator;
        private List<Passage> passageList;
        private Iterator<Passage> passageIterator;

        RoomCycleIterator(List<Room> roomList, List<Passage> passageList, boolean mustSwitchRooms) {
            this.mustSwitchRooms = mustSwitchRooms;
            this.roomList = roomList;
            this.passageList = passageList;
            roomIterator = roomList.iterator();
            passageIterator = passageList.iterator();
            currentRoom = roomIterator.next();
            currentPassage = passageIterator.next();
        }

        @Override
        public void next() {
            if (mustSwitchRooms) {
                if (!roomIterator.hasNext()) {
                    roomIterator = roomList.iterator();
                }
                currentRoom = roomIterator.next();
            } else {
                if (!passageIterator.hasNext()) {
                    passageIterator = passageList.iterator();
                }
                currentPassage = passageIterator.next();
            }
            mustSwitchRooms = !mustSwitchRooms;
        }
    }
}
