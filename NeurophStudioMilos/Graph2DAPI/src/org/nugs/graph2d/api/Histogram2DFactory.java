package org.nugs.graph2d.api;

/**
 *
 * @author zoran
 */
public interface Histogram2DFactory<C> {
    
    public C createHistogram2D(double[] values, int numberOfBins);
    
    public C createHistogram2D(double[] values, int numberOfBins,Graph2DProperties prop);
}
