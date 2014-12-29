package org.neuroph.contrib.utils;

public class ArrayUtils {

    private ArrayUtils() {
    }

    public static double average(double[] array) {
        double accumulator = 0;
        for (double element : array) {
            accumulator += element;
        }
        return accumulator / array.length;
    }
}
