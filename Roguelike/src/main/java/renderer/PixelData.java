package renderer;

import com.googlecode.lanterna.TextColor;

public class PixelData implements Comparable<PixelData> {
    public final boolean isSymbol;
    public final int level;
    public final TextColor color;
    public final double transparency;
    public final char symbol;

    PixelData(boolean isSymbol,
              int level,
              TextColor color,
              double transparency,
              char symbol
    ) {
        this.isSymbol = isSymbol;
        this.level = level;
        this.color = color;
        this.transparency = transparency;
        this.symbol = symbol;
    }

    @Override
    public int compareTo(PixelData o) {
        return level - o.level;
    }
}
