package renderer;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class PixelStack {

    private List<PixelData> staticObjectsStack = new ArrayList<>();
    private List<PixelData> currentStack = new ArrayList<>();
    private Pixel staticPixel = null;
    private boolean changed = false;

    public void insert(PixelData pixelData) {
        currentStack.add(pixelData);
        changed = true;
    }

    public void insertStaticPixel(PixelData pixelData) {
        staticObjectsStack.add(pixelData);
    }

    public void reset() {
        changed = false;
        currentStack = new ArrayList<>(staticObjectsStack);
    }

    public void fitStaticPixel() {
        staticPixel = overlay(staticObjectsStack);
    }

    public Pixel getPixel() {
        if (!changed) {
            return staticPixel;
        } else {
            return overlay(currentStack);
        }
    }

    private static Pixel overlay(List<PixelData> stack) {
        double r1 = 0, g1 = 0, b1 = 0;
        double r2 = 0, g2 = 0, b2 = 0;
        double k1 = 1;
        double k2 = 1;
        char symbol = ' ';
        boolean symbolChosen = false;
        Collections.sort(stack);
        for (int i = stack.size() - 1; i >= 0; i--) {
            PixelData pixelData = stack.get(i);
            int r = pixelData.color.getRed();
            int g = pixelData.color.getGreen();
            int b = pixelData.color.getBlue();
            double t = pixelData.transparency;
            r1 += t * k1 * r;
            g1 += t * k1 * g;
            b1 += t * k1 * b;
            k1 *= (1 - t);
            if (!symbolChosen && pixelData.isSymbol) {
                symbol = pixelData.symbol;
                symbolChosen = true;
            }
            if (!pixelData.isSymbol) {
                r2 += t * k2 * r;
                g2 += t * k2 * g;
                b2 += t * k2 * b;
                k2 *= (1 - t);
            }
        }
        Color symbolColor = new Color((int) r1, (int) g1, (int) b1);
        Color backgroundColor = new Color((int) r2, (int) g2, (int) b2);
        return new Pixel(symbol, symbolColor, backgroundColor);
    }
}
