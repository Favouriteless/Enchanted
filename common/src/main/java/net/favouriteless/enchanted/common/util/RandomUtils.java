package net.favouriteless.enchanted.common.util;

import java.util.Random;

public class RandomUtils {

    public static final Random RANDOM = new Random();

    public static int nextInt() {
        return RANDOM.nextInt();
    }

    public static int nextInt(int upper) {
        return RANDOM.nextInt(upper);
    }

    public static int nextInt(int lower, int upper) {
        return RANDOM.nextInt(lower, upper);
    }

    public static long nextLong() {
        return RANDOM.nextLong();
    }

    public static long nextLong(long upper) {
        return RANDOM.nextLong(upper);
    }

    public static long nextLong(long lower, long upper) {
        return RANDOM.nextLong(lower, upper);
    }

    public static float nextFloat() {
        return RANDOM.nextInt();
    }

    public static float nextFloat(float upper) {
        return RANDOM.nextFloat(upper);
    }

    public static float nextFloat(float lower, float upper) {
        return RANDOM.nextFloat(lower, upper);
    }

    public static double nextDouble() {
        return RANDOM.nextDouble();
    }

    public static double nextDouble(double upper) {
        return RANDOM.nextDouble(upper);
    }

    public static double nextDouble(double lower, double upper) {
        return RANDOM.nextDouble(lower, upper);
    }

    public static double nextGaussian() {
        return RANDOM.nextGaussian();
    }

}
