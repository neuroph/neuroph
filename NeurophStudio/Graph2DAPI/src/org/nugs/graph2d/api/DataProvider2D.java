package org.nugs.graph2d.api;

import java.awt.geom.Point2D;

/**
 * Provides methods to get data from some model that will be visualized
 * 
 * @author Vedrana Gajic
 */
public interface DataProvider2D {
    public Point2D[] getData();
    public Point2D[] getData(Attribute attribute1, Attribute attribute2);

    public String getXLabel();
    public String getYLabel();
}
