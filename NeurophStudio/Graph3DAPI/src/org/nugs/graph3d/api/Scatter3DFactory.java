package org.nugs.graph3d.api;

/**
 *
 * @author zoran
 */
public interface Scatter3DFactory<C> {
    
    public C createScatter3D(Point3D[] points, Scatter3DProperties prop);
    public C createScatter3D(Point3D[] points);
    
}
