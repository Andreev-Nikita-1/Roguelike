package renderer;

import java.awt.Color;

import static java.lang.Thread.sleep;

/**
 * Class, containing information about character, foreground and background colors
 */
public class Pixel {
    public final char symbol;
    public final Color symbolColor;
    public final Color backgroundColor;

    public Pixel(char symbol, Color symbolColor, Color backgroundColor) {
        this.symbol = symbol;
        this.symbolColor = symbolColor;
        this.backgroundColor = backgroundColor;
    }

    private Pixel darkness;


    /**
     * Effect, which turns on when lighting is in darkness state
     */
    public Pixel darkness(boolean shouldBeApplied) {
        if (!shouldBeApplied) return this;
        if (darkness == null) {
            int x = (symbolColor.getRed() + symbolColor.getGreen() + symbolColor.getBlue()) / 3;
            Color symbolWB = new Color((int) (x * 24.0 / 100.0),
                    (int) (x * 32.0 / 100.0),
                    (int) (x * 45.0 / 100.0));

            x = (backgroundColor.getRed() + backgroundColor.getGreen() + backgroundColor.getBlue()) / 3;
            Color backgroundWB = new Color((int) (x * 24.0 / 100.0),
                    (int) (x * 32.0 / 100.0),
                    (int) (x * 45.0 / 100.0));
            darkness = new Pixel(symbol, symbolWB, backgroundWB);
        }
        return darkness;
    }

    private Pixel noir;

    /**
     * Effect, which turns on when game is paused
     */
    public Pixel noir(boolean shouldBeApplied) {
        if (!shouldBeApplied) return this;
        if (noir == null) {
            int x = (symbolColor.getRed() + symbolColor.getGreen() + symbolColor.getBlue()) / 3;
            Color symbolWB = new Color(x, x, x);

            x = (backgroundColor.getRed() + backgroundColor.getGreen() + backgroundColor.getBlue()) / 3;
            Color backgroundWB = new Color(x, x, x);

            noir = new Pixel(symbol, symbolWB, backgroundWB);
        }
        return noir;
    }
}
