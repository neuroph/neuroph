package org.nugs.graph2d.api;

import org.jfree.data.xy.XYSeries;

/**
 * Provides methods to create 2D scatter graphs
 * C - graph panel class that will be created and returned by factory methods 
 * 
 * @author zoran
 */
public interface Scatter2DFactory<C> {
    // we should use Point2D[] instead XYSeries
    public C createScatter2D(XYSeries[] series, Graph2DProperties prop);
    public C createScatter2D(XYSeries[] series);
}
