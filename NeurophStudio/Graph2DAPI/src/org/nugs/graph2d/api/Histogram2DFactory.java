package org.nugs.graph2d.api;

/**
 * Provides methods to create 2D histograms
 * C - graph panel class that will be created and returned by factory methods 
 * 
 * @author zoran
 */
public interface Histogram2DFactory<C> {
    
    public C createHistogram2D(double[] values, int numberOfBins);
    
    public C createHistogram2D(double[] values, int numberOfBins,Graph2DProperties prop);
}
