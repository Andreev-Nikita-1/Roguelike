package inventory;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public class VerticalScale {

    public volatile double level = 1;
    private int r;
    private int g;
    private int b;

    public VerticalScale(double level, TextColor color) {
        this.level = level;
        r = color.toColor().getRed();
        g = color.toColor().getGreen();
        b = color.toColor().getBlue();
    }

    public TextCharacter getCharacter() {
        if (level == 0) {
            return new TextCharacter(' ',
                    new TextColor.RGB((int) (r * level), (int) (g * level), (int) (b * level)),
                    new TextColor.RGB(0, 0, 0));
        }
        if (level >= 1 - 1.0 / 9) {
            return new TextCharacter(' ',
                    new TextColor.RGB((int) (r * level), (int) (g * level), (int) (b * level)),
                    new TextColor.RGB((int) (r * level), (int) (g * level), (int) (b * level)));
        } else {
            return new TextCharacter((char) (9601 + 9 * level),
                    new TextColor.RGB((int) (r * level), (int) (g * level), (int) (b * level)),
                    new TextColor.RGB(0, 0, 0));
        }
    }


}
