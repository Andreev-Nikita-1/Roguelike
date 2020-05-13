package inventory;

import basicComponents.Controller;
import basicComponents.GameplayLogic;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import util.Coord;
import util.Direction;

import static util.Direction.*;

public class InventoryWindow {
    static Coord size = new Coord(30, 11);

    private InventoryWindow() {
    }

    private static CursorWindow baggage = new BaggageWindow(new Coord(1, 8), new Coord(9, 2));
    private static CursorWindow armor = new ArmorWindow(new Coord(8, 3), new Coord(1, 3));
    private static CursorWindow taken = new TakenWindow(new Coord(7, 1));
    private static CursorWindow stats = new StatsWindow(new Coord(1, 1));
    private static TextWindow textWindow = new TextWindow(new Coord(11, 1), new Coord(18, 9));

    private static CursorWindow currentWindow;

    private static void switchWindow(Direction direction, Coord cursorPosition) {
        currentWindow.active = false;
        if (baggage.equals(currentWindow)) {
            if (direction == UP) {
                if (cursorPosition.x < 6) {
                    currentWindow = stats;
                    currentWindow.acceptCursor(new Coord(0, 5));
                } else {
                    currentWindow = armor;
                    currentWindow.acceptCursor(new Coord(0, 2));
                }
            }
        } else if (armor.equals(currentWindow)) {
            if (direction == UP) {
                currentWindow = taken;
                currentWindow.acceptCursor(new Coord(1, 0));
            }
            if (direction == LEFT) {
                currentWindow = stats;
                currentWindow.acceptCursor(new Coord(0, cursorPosition.y + 2));
            }
            if (direction == DOWN) {
                currentWindow = baggage;
                currentWindow.acceptCursor(new Coord(7, 0));
            }
        } else if (taken.equals(currentWindow)) {
            if (direction == DOWN) {
                currentWindow = armor;
                currentWindow.acceptCursor(new Coord(0, 0));
            }
            if (direction == LEFT) {
                currentWindow = stats;
                currentWindow.acceptCursor(new Coord(0, 0));
            }
        } else if (stats.equals(currentWindow)) {
            if (direction == DOWN) {
                currentWindow = baggage;
                currentWindow.acceptCursor(new Coord(0, 0));
            }
            if (direction == RIGHT) {
                if (cursorPosition.y <= 1) {
                    currentWindow = taken;
                    currentWindow.acceptCursor(new Coord(0, 0));
                } else {
                    currentWindow = armor;
                    currentWindow.acceptCursor(new Coord(0, Math.min(cursorPosition.y - 2, 2)));
                }
            }
        }
        currentWindow.active = true;
    }

    public static void draw(TextGUIGraphics graphics) {
        int x = (Controller.getTerminalSizeX() - size.x) / 2;
        int y = (Controller.getTerminalSizeY() - size.y) / 2;
        graphics.setBackgroundColor(new TextColor.RGB(55, 55, 55));
        graphics.setForegroundColor(new TextColor.RGB(255, 255, 255));
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                graphics.putString(i + x,
                        j + y, " ");
            }
        }
        stats.draw(graphics, new Coord(x, y));
        baggage.draw(graphics, new Coord(x, y));
        taken.draw(graphics, new Coord(x, y));
        armor.draw(graphics, new Coord(x, y));
        textWindow.setText(currentWindow.getText());
        textWindow.draw(graphics, new Coord(x, y));

    }

    public static void deactivate() {
        currentWindow.active = false;
    }

    public static void activate() {
        currentWindow = stats;
        currentWindow.active = true;
        currentWindow.acceptCursor(new Coord(0, 0));
    }


    private static void moveCursor(Direction direction) {
        if (!currentWindow.tryShift(direction)) {
            switchWindow(direction, currentWindow.cursorPosition);
        }
    }

    private static void moveItem(Direction direction) {
        if (currentWindow == baggage) {
            GameplayLogic.currentHero.shiftItemInBaggage(currentWindow.cursorPosition, direction);
        } else if (currentWindow == taken) {
            GameplayLogic.currentHero.shiftTakenItems(currentWindow.cursorPosition.x, direction);
        }
    }

    public static void handleKeyStroke(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case Enter:
                if (currentWindow == taken)
                    GameplayLogic.currentHero.takeOffItemInTaken(currentWindow.cursorPosition.x);
                if (currentWindow == armor)
                    GameplayLogic.currentHero.takeOffItemInArmor(currentWindow.cursorPosition.y);
                if (currentWindow == baggage)
                    GameplayLogic.currentHero.putOnItemInBaggage(currentWindow.cursorPosition);
                break;
            case ArrowUp:
                if (ctrl) textWindow.pgUp();
                else if (shift) {
                    moveItem(UP);
                    currentWindow.tryShift(UP);
                } else {
                    moveCursor(UP);
                }
                break;
            case ArrowDown:
                if (ctrl) textWindow.pgDn();
                else if (shift) {
                    moveItem(DOWN);
                    currentWindow.tryShift(DOWN);
                } else {
                    moveCursor(DOWN);
                }
                break;
            case ArrowLeft:
                if (ctrl) ;
                else if (shift) {
                    moveItem(LEFT);
                    currentWindow.tryShift(LEFT);
                } else {
                    moveCursor(LEFT);
                }
                break;
            case ArrowRight:
                if (ctrl) ;
                else if (shift) {
                    moveItem(RIGHT);
                    currentWindow.tryShift(RIGHT);
                } else {
                    moveCursor(RIGHT);
                }
                break;
        }
    }
}

