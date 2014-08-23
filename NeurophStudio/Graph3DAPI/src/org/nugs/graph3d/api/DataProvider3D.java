package org.nugs.graph3d.api;

import org.nugs.graph2d.api.Attribute;

/**
 * Provides method to get 3D data from some model that will be visualized in 3D
 *  
 * @author Vedrana Gajic
 * @param <P> Class that represents 3D point
 */
public interface DataProvider3D<P> {
    
    /**
     * This should return an array of 3D points, as an array of <P> objects
     * P - Class that is used to represent 3D point (usually Point3D but custom class can be used if needed)
     * 
     * @param a
     * 
     * @return 
     */
    public P[] getData(Attribute... a);

    
}
