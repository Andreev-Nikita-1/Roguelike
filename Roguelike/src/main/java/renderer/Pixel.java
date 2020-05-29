package renderer;

import java.awt.Color;

import static java.lang.Thread.sleep;

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

    public Pixel inverse() {
        int r = symbolColor.getRed();
        int g = symbolColor.getGreen();
        int b = symbolColor.getBlue();
        Color symbolWB = new Color(255 - r, 255 - g, 255 - b);

        r = backgroundColor.getRed();
        g = backgroundColor.getGreen();
        b = backgroundColor.getBlue();
        Color backgroundWB = new Color(255 - r, 255 - g, 255 - b);


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
        return (int) Math.max(Math.min(255, 15 + 15 * Math.sin(testPhase / a + b) + x), 0);
    }


    Color testColor(int r, int g, int b) {
        double A = 0.299;
        double B = 0.587;
        double C = 0.114;
        double psi = testPhase / 50.0;
        double fi = 6 * Math.sin(testPhase / 50.0 / 6);
        double r1 = Math.cos(psi) * r + (Math.sqrt(B) / Math.sqrt(A)) * Math.sin(psi) * g;
        double g1 = -(Math.sqrt(A) / Math.sqrt(B)) * Math.sin(psi) * r + Math.cos(psi) * g;
        double g2 = Math.cos(fi) * g1 + (Math.sqrt(C) / Math.sqrt(B)) * Math.sin(fi) * b;
        double b1 = -(Math.sqrt(C) / Math.sqrt(B)) * Math.sin(fi) * g1 + Math.cos(fi) * b;
        return new Color((int) Math.min(255, Math.abs(r1)),
                (int) Math.min(255, Math.abs(g2)), (int) Math.min(255, Math.abs(b1)));
    }

    //TODO
    public static volatile int testPhase = 0;
    private static boolean run = false;

    public Pixel test() {
        if (!run) {
            new Thread(() -> {
                while (true) {
                    testPhase++;
                    try {
                        sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            run = true;
        }
        int r = symbolColor.getRed();
        int g = symbolColor.getGreen();
        int b = symbolColor.getBlue();
        Color symbolWB = testColor(r, g, b);

        r = backgroundColor.getRed();
        g = backgroundColor.getGreen();
        b = backgroundColor.getBlue();
        Color backgroundWB = testColor(r, g, b);


        return new Pixel(symbol, symbolWB, backgroundWB);
    }

    private Pixel noir;

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
