package org.neuroph.contrib.eval.classification;

public class Utils {

    private Utils() {
    }

    public static double average(double[] array) {
        double accumulator = 0;
        for (double element : array) {
            accumulator += element;
        }
        return accumulator / array.length;
    }
    
    public static int maxIdx(double[] array) {
        int maxIdx=0;
        for (int i=1;  i< array.length; i++) {
            if (array[i] > array[maxIdx])
                   maxIdx = i;
        }
        return maxIdx;
    }    
    
}
