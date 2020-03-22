import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;

public class MapRenderer implements InteractableRenderer<GameplayComponent> {

    @Override
    public TerminalPosition getCursorLocation(GameplayComponent component) {
        return null;
    }

    @Override
    public TerminalSize getPreferredSize(GameplayComponent component) {
        return null;
    }

    @Override
    public void drawComponent(TextGUIGraphics graphics, GameplayComponent component) {
        graphics.setCharacter(Controller.x, Controller.y, '+');
    }
}
