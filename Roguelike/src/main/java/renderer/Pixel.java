package renderer;

import com.googlecode.lanterna.TextColor;

import static java.lang.Thread.sleep;

public class Pixel {
    public final char symbol;
    public final TextColor symbolColor;
    public final TextColor backgroundColor;

    public Pixel(char symbol, TextColor symbolColor, TextColor backgroundColor) {
        this.symbol = symbol;
        this.symbolColor = symbolColor;
        this.backgroundColor = backgroundColor;
    }

    private Pixel darkness;

    public Pixel darkness(boolean shouldBeApplied) {
        if (!shouldBeApplied) return this;
        if (darkness == null) {
            int x = (symbolColor.toColor().getRed() + symbolColor.toColor().getGreen() + symbolColor.toColor().getBlue()) / 3;
            TextColor symbolWB = new TextColor.RGB((int) (x * 27.0 / 100.0),
                    (int) (x * 33.0 / 100.0),
                    (int) (x * 40.0 / 100.0));

            x = (backgroundColor.toColor().getRed() + backgroundColor.toColor().getGreen() + backgroundColor.toColor().getBlue()) / 3;
            TextColor backgroundWB = new TextColor.RGB((int) (x * 27.0 / 100.0),
                    (int) (x * 33.0 / 100.0),
                    (int) (x * 40.0 / 100.0));
            darkness = new Pixel(symbol, symbolWB, backgroundWB);
        }
        return darkness;
    }

    public Pixel inverse() {
        int r = symbolColor.toColor().getRed();
        int g = symbolColor.toColor().getGreen();
        int b = symbolColor.toColor().getBlue();
        TextColor symbolWB = new TextColor.RGB(255 - r, 255 - g, 255 - b);

        r = backgroundColor.toColor().getRed();
        g = backgroundColor.toColor().getGreen();
        b = backgroundColor.toColor().getBlue();
        TextColor backgroundWB = new TextColor.RGB(255 - r, 255 - g, 255 - b);


        return new Pixel(symbol, symbolWB, backgroundWB);
    }


    private static int f(int x, int c) {
        double a = 0, b = 0;
        switch (c) {
            case 0:
                a = 4.0;
                b = 0.0;
                break;
            case 1:
                a = 5.0;
                b = 2.0;
                break;
            case 2:
                a = 6.0;
                b = 4.0;
                break;
        }
        return (int) Math.max(Math.min(255, 15 + 15 * Math.sin(redLevel / a + b) + x), 0);
    }

    //TODO
    public static volatile int redLevel = 0;
    private static boolean run = false;

    public Pixel test() {
        if (!run) {
            new Thread(() -> {
                while (true) {
                    redLevel++;
                    try {
                        sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            run = true;
        }
        int r = symbolColor.toColor().getRed();
        int g = symbolColor.toColor().getGreen();
        int b = symbolColor.toColor().getBlue();
        TextColor symbolWB = new TextColor.RGB(f(r, 0), f(g, 1), f(b, 2));

        r = backgroundColor.toColor().getRed();
        g = backgroundColor.toColor().getGreen();
        b = backgroundColor.toColor().getBlue();
        TextColor backgroundWB = new TextColor.RGB(f(r, 0), f(g, 1), f(b, 2));


        return new Pixel(symbol, symbolWB, backgroundWB);
    }

    private Pixel noir;

    public Pixel noir(boolean shouldBeApplied) {
        if (!shouldBeApplied) return this;
        if (noir == null) {
            int x = (symbolColor.toColor().getRed() + symbolColor.toColor().getGreen() + symbolColor.toColor().getBlue()) / 3;
            TextColor symbolWB = new TextColor.RGB(x, x, x);

            x = (backgroundColor.toColor().getRed() + backgroundColor.toColor().getGreen() + backgroundColor.toColor().getBlue()) / 3;
            TextColor backgroundWB = new TextColor.RGB(x, x, x);

            noir = new Pixel(symbol, symbolWB, backgroundWB);
        }
        return noir;
    }
}
