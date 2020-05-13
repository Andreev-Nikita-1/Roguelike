package inventory;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import util.Coord;
import util.Direction;

import java.util.Arrays;
import java.util.List;

import static util.Direction.DOWN;
import static util.Direction.UP;

public class StatsWindow extends CursorWindow {
    public StatsWindow(Coord location) {
        super(location, new Coord(5, 6));
    }


    static char exp = (char) 61558;
    static char hp = (char) 57355;
    static char hp_cracked = (char) 57356;
    static char fist = (char) 9994;
    static char protection = (char) 57822;
    static char speed = (char) 10174;
    static char luck = (char) 10020;

    @Override
    protected boolean tryShift(Direction direction) {
        if (direction.horizontal()
                || direction == DOWN && cursorPosition.y == 5
                || direction == UP && cursorPosition.y == 0) {
            return false;
        }
        cursorPosition.shift(direction);
        return true;
    }

    @Override
    public InventoryText getText() {
        return InventoryText.emptyText;
    }

    @Override
    public void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
        graphics.setForegroundColor(new TextColor.RGB(255, 255, 255));
        List<Character> chars = Arrays.asList(exp, hp, fist, protection, speed, luck);
        for (int i = 0; i < 6; i++) {
            if (cursorPosition.y == i && active) {
                graphics.setBackgroundColor(new TextColor.RGB(50, 0, 0));
            } else {
                graphics.setBackgroundColor(new TextColor.RGB(0, 0, 0));
            }
            graphics.putString(1 + inventoryWindowLocation.x + location.x,
                    i + inventoryWindowLocation.y + location.y, " 100");
            graphics.setCharacter(inventoryWindowLocation.x + location.x,
                    i + inventoryWindowLocation.y + location.y, chars.get(i));
        }

    }
//        if(active)
//
//    {
//        graphics.setCharacter(inventoryWindowLocation.x + location.x,
//                cursorPosition.y + inventoryWindowLocation.y + location.y, 'A');
//    }
}

