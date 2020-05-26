package map.roomSystem.textures;

import renderer.PixelData;
import renderer.VisualPixel;

import java.awt.*;

public class StoneFloor1 extends RandomFloor {
//        private static final Color BACK_COLOR = new Color(45, 34, 22);
//    private static final Color SYMBOL_COLOR = new Color(57, 45, 30);
//    private static final Color BACK_COLOR = new Color(60, 24, 10);
//    private static final Color SYMBOL_COLOR = new Color(38, 24, 13);
    private static final Color BACK_COLOR = new Color(90, 66, 50);
    private static final Color SYMBOL_COLOR = new Color(130, 92, 84);


    private static final VisualPixel BACK = new VisualPixel(
            new PixelData(false, -10, BACK_COLOR, 1, ' '));

    public StoneFloor1(int seed) {
        super(seed, new VisualPixelGenerator(BACK, SYMBOL_COLOR, 0x01AC, -9, 5,
                new double[]{0.125, 0.25, 0.25, 0.125, 0.25}));
    }

}
