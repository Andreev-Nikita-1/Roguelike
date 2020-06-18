package renderer;

import com.googlecode.lanterna.TextColor;

public class PixelData implements Comparable<PixelData> {
    public final boolean isSymbol;
    public final int level;
    public final TextColor color;
    public final double transparency;
    public final char symbol;

    public PixelData(boolean isSymbol,
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

    public PixelData layOn(TextColor color, double transparency) {
        int r = (int) ((double) this.color.toColor().getRed() * (1 - transparency) + (double) color.toColor().getRed() * transparency);
        int g = (int) ((double) this.color.toColor().getGreen() * (1 - transparency) + (double) color.toColor().getGreen() * transparency);
        int b = (int) ((double) this.color.toColor().getBlue() * (1 - transparency) + (double) color.toColor().getBlue() * transparency);
        TextColor newColor = new TextColor.RGB(r, g, b);
        return new PixelData(isSymbol, level, newColor, this.transparency, symbol);
    }

    @Override
    public int compareTo(PixelData o) {
        return level - o.level;
    }
}
