package org.nugs.graph2d.api;

import java.awt.geom.Point2D;

/**
 *
 * @author Vedrana Gajic
 */
public interface DataProvider2D {
    public Point2D[] getData();
    public Point2D[] getData(Attribute attribute1, Attribute attribute2);

    public String getXLabel();
    public String getYLabel();
}
