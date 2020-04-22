package util;

public class Util {
    public static <T> T generate(double[] weights, T[] options) {
        double t = Math.random();
        for (int i = 0; i < weights.length; i++) {
            if (t < weights[i]) {
                return options[i];
            } else {
                t -= weights[i];
            }
        }
        return null;
    }

    public static <T> T generate(T[] options) {
        double t = Math.random();
        for (int i = 0; i < options.length; i++) {
            if (t < (double) 1 / options.length) {
                return options[i];
            } else {
                t -= 1 / options.length;
            }
        }
        return null;
    }
}
