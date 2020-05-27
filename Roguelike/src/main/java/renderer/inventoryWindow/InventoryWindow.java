package renderer.inventoryWindow;

import basicComponents.AppLogic;
import basicComponents.Controller;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import hero.Inventory;
import util.Coord;
import util.Direction;

import java.awt.*;

import static util.Direction.*;
import static util.Util.convertColor;

public class InventoryWindow {
    static Coord size = new Coord(28, 15);
    public static Coord textWindowSize = new Coord(15, 11);

    private InventoryWindow() {
    }

    private static CursorWindow baggage = new BaggageWindow(new Coord(2, 8), Inventory.baggageSize);
    private static CursorWindow armor = new ArmorWindow(new Coord(7, 4), new Coord(2, 1));
    private static CursorWindow taken = new TakenWindow(new Coord(6, 2));
    private static CursorWindow stats = new StatsWindow(new Coord(2, 2));
    private static TextWindow textWindow = new TextWindow(new Coord(11, 2), textWindowSize);

    private static CursorWindow currentWindow;

    private static char leftUp = (char) 0x01F1;
    private static char up = (char) 0x01F4;
    private static char rightUp = (char) 0x01EE;
    private static char leftDown = (char) 0x01F0;
    private static char down = (char) 0x01F5;
    private static char rightDown = (char) 0x01EF;
    private static char left = (char) 0x01F3;
    private static char right = (char) 0x01F2;


    private static void drawBackground(TextGUIGraphics graphics) {
        int x = (Controller.getTerminalSizeX() - size.x) / 2;
        int y = (Controller.getTerminalSizeY() - size.y) / 2;
        Color backColor = new Color(60, 55, 55);
        Color foreColor = new Color(100, 100, 100);
        graphics.setBackgroundColor(convertColor(backColor));
        graphics.setForegroundColor(convertColor(foreColor));
        for (int i = 0; i < size.x; i++) {
            for (int j = 0; j < size.y; j++) {
                graphics.putString(i + x,
                        j + y, "" + (char) (0x0217));
            }
        }
        for (int i = 1; i < size.x - 1; i++) {
            graphics.setCharacter(i + x, y, up);
            graphics.setCharacter(i + x, y + size.y - 1, down);
        }
        for (int i = 1; i < size.y - 1; i++) {
            graphics.setCharacter(x, y + i, left);
            graphics.setCharacter(size.x - 1 + x, y + i, right);
        }
        graphics.setCharacter(x, y, leftUp);
        graphics.setCharacter(size.x - 1 + x, y, rightUp);
        graphics.setCharacter(size.x - 1 + x, y + size.y - 1, rightDown);
        graphics.setCharacter(x, y + size.y - 1, leftDown);
    }


    public static void draw(TextGUIGraphics graphics) {
        int x = (Controller.getTerminalSizeX() - size.x) / 2;
        int y = (Controller.getTerminalSizeY() - size.y) / 2;
        drawBackground(graphics);
        Coord terminal = new Coord(x, y);
        stats.draw(graphics, terminal);
        baggage.draw(graphics, terminal);
        taken.draw(graphics, terminal);
        armor.draw(graphics, terminal);
        textWindow.draw(graphics, terminal);
    }

    public static void deactivate() {
        currentWindow.active = false;
    }

    public static void activate() {
        currentWindow = stats;
        currentWindow.acceptCursor(new Coord(0, 0));
        currentWindow.active = true;
        textWindow.resetBias();
        textWindow.setText(currentWindow.getText());
    }

    private static boolean switchWindow(Direction direction, Coord cursorPosition) {
        Subwindow prevWindow = currentWindow;
        currentWindow.active = false;
        if (baggage.equals(currentWindow)) {
            if (direction == UP) {
                if (cursorPosition.x < 4) {
                    currentWindow = stats;
                    currentWindow.acceptCursor(new Coord(0, 4));
                } else {
                    currentWindow = armor;
                    if (cursorPosition.x < 6) {
                        currentWindow.acceptCursor(new Coord(0, 0));
                    } else {
                        currentWindow.acceptCursor(new Coord(1, 0));
                    }
                }
            }
        } else if (armor.equals(currentWindow)) {
            if (direction == UP) {
                currentWindow = taken;
                if (cursorPosition.x == 0) {
                    currentWindow.acceptCursor(new Coord(1, 0));
                } else {
                    currentWindow.acceptCursor(new Coord(2, 0));
                }
            }
            if (direction == LEFT) {
                currentWindow = stats;
                currentWindow.acceptCursor(new Coord(0, cursorPosition.y + 2));
            }
            if (direction == DOWN) {
                currentWindow = baggage;
                if (cursorPosition.x == 0) {
                    currentWindow.acceptCursor(new Coord(5, 0));
                } else {
                    currentWindow.acceptCursor(new Coord(6, 0));
                }
            }
        } else if (taken.equals(currentWindow)) {
            if (direction == DOWN) {
                currentWindow = armor;
                if (cursorPosition.x < 2) {
                    currentWindow.acceptCursor(new Coord(0, 0));
                } else {
                    currentWindow.acceptCursor(new Coord(1, 0));
                }
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
                    currentWindow.acceptCursor(new Coord(0, 0));
                }
            }
        }
        currentWindow.active = true;
        return prevWindow != currentWindow;
    }

    private static void moveCursor(Direction direction) {
        if (!currentWindow.tryShift(direction)) {
            if (!switchWindow(direction, currentWindow.cursorPosition)) {
                return;
            }
        }
        textWindow.resetBias();
        textWindow.setText(currentWindow.getText());
    }

    private static void moveItem(Direction direction) {
        if (currentWindow == baggage) {
            AppLogic.currentGame.currentInventory.shiftItemInBaggage(currentWindow.cursorPosition, direction);
        } else if (currentWindow == taken) {
            AppLogic.currentGame.currentInventory.shiftTakenItems(currentWindow.cursorPosition.x, direction);
        }
    }

    public static void handleKeyStroke(KeyStroke keyStroke) {
        boolean ctrl = keyStroke.isCtrlDown();
        boolean alt = keyStroke.isAltDown();
        boolean shift = keyStroke.isShiftDown();
        switch (keyStroke.getKeyType()) {
            case Enter:
                if (currentWindow == taken)
                    AppLogic.currentGame.currentInventory.takeOffItemInTaken(currentWindow.cursorPosition.x);
                if (currentWindow == armor) {
                    if (currentWindow.cursorPosition.x == 0)
                        AppLogic.currentGame.currentInventory.takeOffWeapon();
                    else
                        AppLogic.currentGame.currentInventory.takeOffShield();
                }
                if (currentWindow == baggage)
                    AppLogic.currentGame.currentInventory.putOnItemInBaggage(currentWindow.cursorPosition);
                textWindow.setText(currentWindow.getText());
                break;
            case ArrowUp:
                if (ctrl) textWindow.pgUp();
                else if (shift) {
                    moveItem(UP);
                    if (currentWindow.tryShift(UP)) {
                        textWindow.resetBias();
                        textWindow.setText(currentWindow.getText());
                    }
                } else {
                    moveCursor(UP);
                }
                break;
            case ArrowDown:
                if (ctrl) textWindow.pgDn();
                else if (shift) {
                    moveItem(DOWN);
                    if (currentWindow.tryShift(DOWN)) {
                        textWindow.resetBias();
                        textWindow.setText(currentWindow.getText());
                    }
                } else {
                    moveCursor(DOWN);
                }
                break;
            case ArrowLeft:
                if (ctrl) ;
                else if (shift) {
                    moveItem(LEFT);
                    if (currentWindow.tryShift(LEFT)) {
                        textWindow.resetBias();
                        textWindow.setText(currentWindow.getText());
                    }
                } else {
                    moveCursor(LEFT);
                }
                break;
            case ArrowRight:
                if (ctrl) ;
                else if (shift) {
                    moveItem(RIGHT);
                    if (currentWindow.tryShift(RIGHT)) {
                        textWindow.resetBias();
                        textWindow.setText(currentWindow.getText());
                    }
                } else {
                    moveCursor(RIGHT);
                }
                break;
        }
    }
}

