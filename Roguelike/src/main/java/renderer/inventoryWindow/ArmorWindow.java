package renderer.inventoryWindow;

import basicComponents.AppLogic;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import hero.items.Item;
import hero.items.Shield;
import hero.items.Weapon;
import util.Coord;

import java.awt.*;

import static util.Util.convertColor;
import static util.Util.greenRedScale;


/**
 * Window for taken equipment
 */
public class ArmorWindow extends TileWindow {
    ArmorWindow(Coord location, Coord size) {
        super(location, size);
    }

    @Override
    Item getItem(Coord position) {
        return (position.x == 0) ? AppLogic.currentGame.hero.inventory.weapon : AppLogic.currentGame.hero.inventory.shield;
    }

    @Override
    public InventoryText getText() {
        if (cursorPosition.x == 0) {
            Weapon weapon = AppLogic.currentGame.hero.inventory.weapon;
            if (weapon != null) return weapon.getText();
            return InventoryText.NO_WEAPON;
        } else {
            Shield shield = AppLogic.currentGame.hero.inventory.shield;
            if (shield != null) return shield.getText();
            return InventoryText.NO_SHIELD;
        }
    }

    /**
     * Scales for representing durability of equipment
     */


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
        Weapon weapon = AppLogic.currentGame.hero.inventory.weapon;
        if (weapon != null) {
            graphics.setForegroundColor(convertColor(greenRedScale(weapon.getDurabilityLevel())));
            graphics.setCharacter(inventoryWindowLocation.x + location.x - 1,
                    inventoryWindowLocation.y + location.y,
                    rightScale(weapon.getDurabilityLevel()));
        } else {
            graphics.setCharacter(inventoryWindowLocation.x + location.x - 1,
                    inventoryWindowLocation.y + location.y,
                    ' ');
        }
        Shield shield = AppLogic.currentGame.hero.inventory.shield;
        if (shield != null) {
            graphics.setForegroundColor(convertColor(greenRedScale(shield.getDurabilityLevel())));
            graphics.setCharacter(inventoryWindowLocation.x + location.x + 2,
                    inventoryWindowLocation.y + location.y,
                    leftScale(shield.getDurabilityLevel()));
        } else {
            graphics.setCharacter(inventoryWindowLocation.x + location.x + 2,
                    inventoryWindowLocation.y + location.y,
                    ' ');
        }

    }
}
