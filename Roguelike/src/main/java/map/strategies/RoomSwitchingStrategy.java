package map.strategies;


import map.roomSystem.Passage;
import map.roomSystem.Room;
import objects.creatures.Mob;
import util.Coord;


/**
 * Strategy, that leads mob from one room to another
 */
public abstract class RoomSwitchingStrategy extends LocalTargetSwitchingStrategy {
    RoomPassageIterator iterator;


    RoomSwitchingStrategy(Mob owner, RoomPassageIterator iterator) {
        super(owner, iterator.initialTarget());
        this.iterator = iterator;
    }

    @Override
    public void chooseNextTarget() {
        target = iterator.nextTarget();
    }

    /**
     * Class, that alternates rooms and passages locations for having correct route
     */
    protected abstract static class RoomPassageIterator {
        Room currentRoom;
        Passage currentPassage;

        Coord initialTarget() {
            return currentRoom.center();
        }

        Coord nextTarget() {
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
