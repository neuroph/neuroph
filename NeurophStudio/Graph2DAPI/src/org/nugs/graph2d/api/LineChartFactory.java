package org.nugs.graph2d.api;

import java.awt.geom.Point2D;

/**
 * Provides methods to create 2D line charts
 * C - graph panel class that will be created and returned by factory methods 
 * 
 * @author zoran
 */
public interface LineChartFactory<C> {    
    public C createLineChart(Point2D[] points);
    public C createLineChart(Point2D[] points, Graph2DProperties prop);    
}
