import com.googlecode.lanterna.gui2.AbstractInteractableComponent;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.gui2.InteractableRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;


public class GameplayComponent extends AbstractInteractableComponent<GameplayComponent> {
    public int a = 0;

    @Override
    protected InteractableRenderer<GameplayComponent> createDefaultRenderer() {
        return new MapRenderer();
    }

    @Override
    protected Result handleKeyStroke(KeyStroke keyStroke) {
        AppLogic.handleKeyStrokeOnMap(keyStroke);
        return Result.HANDLED;
    }
}
