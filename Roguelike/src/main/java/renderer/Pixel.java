package renderer;

import com.googlecode.lanterna.TextColor;

class Pixel {
    public final char symbol;
    public final TextColor symbolColor;
    public final TextColor backgroundColor;

    public Pixel(char symbol, TextColor symbolColor, TextColor backgroundColor) {
        this.symbol = symbol;
        this.symbolColor = symbolColor;
        this.backgroundColor = backgroundColor;
    }
}
