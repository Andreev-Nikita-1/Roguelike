package map.roomSystem.textures;

import renderer.PixelData;
import renderer.VisualPixel;
import util.Util;

import java.awt.Color;

public class VisualPixelGenerator {
    double[] weights;
    VisualPixel[] pixels;

    public VisualPixelGenerator(VisualPixel back, Color color, int start, int level, int n, double[] weights) {
        this.weights = weights;
        pixels = new VisualPixel[n];
        for (int i = 0; i < n; i++) {
            pixels[i] = new VisualPixel(new PixelData(true, level, color, 1, (char) (start + i))).combinedWith(back);
        }
    }


    public VisualPixel generate(double random) {
        return Util.generate(weights, pixels, random);
    }
}
