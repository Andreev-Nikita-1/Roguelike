package map.strategies;


import map.roomSystem.Passage;
import map.roomSystem.Room;
import objects.creatures.Creature;
import util.Coord;


public abstract class RoomSwitchingStrategy extends LocalTargetSwitchingStrategy {
    protected RoomPassageIterator iterator;


    public RoomSwitchingStrategy(Creature owner, RoomPassageIterator iterator) {
        super(owner, iterator.initialTarget());
        this.iterator = iterator;
    }

    @Override
    public void chooseNextTarget() {
        target = iterator.nextTarget();
    }

    protected abstract static class RoomPassageIterator {
        public Room currentRoom;
        public Passage currentPassage;

        public Coord initialTarget() {
            return currentRoom.center();
        }

        public Coord nextTarget() {
            if (currentPassage == null) {
                return currentRoom.center();
            } else {
                next();
                return currentPassage.entryLocation(currentRoom);
            }
        }

        public abstract void next();
    }
}
