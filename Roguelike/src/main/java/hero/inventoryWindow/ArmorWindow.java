package hero.inventoryWindow;

import basicComponents.AppLogic;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.items.Item;
import util.Coord;

import java.awt.*;

import static util.Util.convertColor;

public class ArmorWindow extends TileWindow {
    public ArmorWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    Item getItem(Coord position) {
        return (position.x == 0) ? AppLogic.currentGame.currentInventory.weapon : AppLogic.currentGame.currentInventory.shield;
    }

    @Override
    public InventoryText getText() {
        return InventoryText.emptyText;
    }

    private char leftScale(double level) {
        return (char) (0x0344 + (int) (level * 49));
    }

    private char rightScale(double level) {
        return (char) (0x037D + (int) (level * 49));
    }

    @Override
    void draw(TextGUIGraphics graphics, Coord inventoryWindowLocation) {
        super.draw(graphics, inventoryWindowLocation);
        graphics.setBackgroundColor(convertColor(new Color(100, 100, 100)));
        graphics.setForegroundColor(convertColor(Color.GREEN));
        if (AppLogic.currentGame.currentInventory.weapon != null) {
            graphics.setCharacter(inventoryWindowLocation.x + location.x - 1,
                    inventoryWindowLocation.y + location.y,
                    rightScale(AppLogic.currentGame.currentInventory.weapon.getDurabilityLevel()));
        } else {
            graphics.setCharacter(inventoryWindowLocation.x + location.x - 1,
                    inventoryWindowLocation.y + location.y,
                    ' ');
        }
        if (AppLogic.currentGame.currentInventory.shield != null) {
            graphics.setCharacter(inventoryWindowLocation.x + location.x + 2,
                    inventoryWindowLocation.y + location.y,
                    leftScale(AppLogic.currentGame.currentInventory.shield.getDurabilityLevel()));
        } else {
            graphics.setCharacter(inventoryWindowLocation.x + location.x + 2,
                    inventoryWindowLocation.y + location.y,
                    ' ');
        }

    }
}
