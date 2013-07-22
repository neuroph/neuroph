package org.nugs.graph3d.api;

import org.jzy3d.plot3d.builder.Mapper;

/**
 *
 * @author zoran
 */
public interface Surface3DFactory<C> {
    // we should use Point3D[] instead Mapper here
    public C createSurface(Mapper mapper, Surface3DProperties prop);
    public C createSurface(Point3D[] points);
}
