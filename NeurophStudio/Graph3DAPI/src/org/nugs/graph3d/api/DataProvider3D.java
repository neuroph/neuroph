package org.nugs.graph3d.api;

import org.nugs.graph2d.api.Attribute;

/**
 * Provides methods to get data from some model that will be visualized
 * P - Class that represents 3D point
 * 
 * @author Vedrana Gajic
 * @param <P>
 */
public interface DataProvider3D<P> {
    /**
     * Returns and array of 3D points
     * P - Class that is used to represent 3D point
     * @param a
     * @return 
     */
    public P[] getData(Attribute... a);

    
}
