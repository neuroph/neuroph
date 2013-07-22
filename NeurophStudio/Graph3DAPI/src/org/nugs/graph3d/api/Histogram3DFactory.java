package org.nugs.graph3d.api;

/**
 *
 * @author zoran
 */
public interface Histogram3DFactory<C> {
    public C createHistogram3D(Point3D[] points, Histogram3DProperties prop);
    public C createHistogram3D(Point3D[] points);
}
