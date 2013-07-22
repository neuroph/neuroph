package org.nugs.graph3d.api;

import org.nugs.graph2d.api.Attribute;

/**
 *
 * @author Vedrana Gajic
 */
public interface DataProvider3D {
    public Point3D[] getData(Attribute... a);
}
