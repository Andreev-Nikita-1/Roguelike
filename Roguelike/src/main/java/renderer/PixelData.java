package renderer;

import java.awt.Color;

public class PixelData implements Comparable<PixelData> {
    public final boolean isSymbol;
    public final int level;
    public final Color color;
    public final double transparency;
    public final char symbol;

    public PixelData(boolean isSymbol,
                     int level,
                     Color color,
                     double transparency,
                     char symbol
    ) {
        this.isSymbol = isSymbol;
        this.level = level;
        this.color = color;
        this.transparency = transparency;
        this.symbol = symbol;
    }

    public PixelData layOn(Color color, double transparency) {
        int r = (int) ((double) this.color.getRed() * (1 - transparency) + (double) color.getRed() * transparency);
        int g = (int) ((double) this.color.getGreen() * (1 - transparency) + (double) color.getGreen() * transparency);
        int b = (int) ((double) this.color.getBlue() * (1 - transparency) + (double) color.getBlue() * transparency);
        Color newColor = new Color(r, g, b);
        return new PixelData(isSymbol, level, newColor, this.transparency, symbol);
    }

    public PixelData changeBrightness(double brightness) {
        int r = (int) Math.min((this.color.getRed() * brightness), 255);
        int g = (int) Math.min((this.color.getGreen() * brightness), 255);
        int b = (int) Math.min((this.color.getBlue() * brightness), 255);
        Color newColor = new Color(r, g, b);
        return new PixelData(isSymbol, level, newColor, this.transparency, symbol);
    }

    @Override
    public int compareTo(PixelData o) {
        return level - o.level;
    }
}
