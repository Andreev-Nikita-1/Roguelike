package util;


import com.googlecode.lanterna.TextColor;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Util {
    public static <T> T generate(double[] weights, T[] options, double random) {
        double t = random;
        for (int i = 0; i < weights.length; i++) {
            if (t < weights[i]) {
                return options[i];
            } else {
                t -= weights[i];
            }
        }
        return null;
    }

    public static <T> T generate(double[] weights, T[] options) {
        return generate(weights, options, Math.random());
    }

    public static <T> T generate(T[] options) {
        double[] weights = new double[options.length];
        Arrays.fill(weights, 1.0 / weights.length);
        return generate(weights, options, Math.random());
    }

    public static <T> T generate(T[] options, double random) {
        double[] weights = new double[options.length];
        Arrays.fill(weights, 1.0 / weights.length);
        return generate(weights, options, random);
    }


    public static <T> T generate(List<T> options) {
        return (T) generate(options.toArray());
    }


    public static String tightNumber(int n) {
        if (n < 100) {
            return "" + (char) (0x0286 + n);
        } else {
            return tightNumber(n / 100) + "" + ((n % 100 < 10) ? (char) (0x0273 + n % 100) : (char) (0x0286 + n % 100));
        }
    }

    public static TextColor convertColor(Color color) {
        return new TextColor.RGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color greenRedScale(double level) {
        int r = (int) (255 * (1 - level));
        int g = (int) (255 * level);
        return new Color(r, g, 0);
    }

    public static String horizontalScale(int length, double level) {
        if (length == 1) {
            return "" + (char) (0x00F7 + level * 50);
        } else {
            double threshold = 1 - 1.0 / length;
            if (level <= threshold) {
                return horizontalScale(length - 1, Math.min(1, level * (length / (double) (length - 1)))) + (char) 0x00F7;
            } else {
                return new String(new char[length - 1]).replace('\0', (char) 0x0129) +
                        horizontalScale(1, (level - threshold) * length);
            }
        }
    }
}
