package map.strategies;

import map.roomSystem.Room;
import objects.creatures.Mob;
import util.Coord;

import java.util.ArrayList;
import java.util.List;

/**
 * Mob will walk around in one room
 */
public class RoomPatrolStrategy extends LocalTargetSwitchingStrategy {

    private List<Coord> knots;
    private int ind = 0;

    RoomPatrolStrategy(Mob owner) {
        super(owner, owner.map.closestRoom(owner.getLocation()).center());
        Room room = owner.map.closestRoom(owner.getLocation());
        knots = new ArrayList<>();
        knots.add(room.center().shifted(new Coord(-room.size.x / 3, -room.size.y / 3)));
        knots.add(room.center().shifted(new Coord(room.size.x / 3, -room.size.y / 3)));
        knots.add(room.center().shifted(new Coord(room.size.x / 3, room.size.y / 3)));
        knots.add(room.center().shifted(new Coord(-room.size.x / 3, room.size.y / 3)));
    }

    @Override
    public void chooseNextTarget() {
        target = knots.get(ind++);
        ind %= 4;
    }
}
