package org.neuroph.training;

/**
 *
 * @author zoran
 */
public class Stats {

    private String name;
    
    public double min = Double.POSITIVE_INFINITY,
                  max = Double.NEGATIVE_INFINITY,
                  count = 0,
                  mean = 0,
                  variance = 0,
                  standardDeviation = 0;

    public Stats(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " Statistics: Min=" + min + ", Max=" + max + ",  Mean=" + mean + ", Variance=" + variance + ", Standard Deviation=" + standardDeviation + '}';
    }
    
    
    
    
}
