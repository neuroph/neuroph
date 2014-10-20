package org.nugs.graph3d.api;

/**
 * Provides methods to create 3D scatter graph
 * C - graph panel class that will be created and returned by factory methods 
 * P - class that is used to represent 3D point 
 * 
 * @author zoran
 * @param <C>
 * @param <P>
 */
public interface Scatter3DFactory<C, P> {
    
    /**
     * Creates and returns 3D scatter for given list of 3D points
     * @param points an array of 3d points
     * @return 3D scatter graph
     */
    public C createScatter3D(P[] points);    
    
    /**
     * Creates and returns 3D scatter for given list of 3D points and scatter graph properties
     * @param points an array of 3d points
     * @param prop
     * @return 
     */
    public C createScatter3D(P[] points, Scatter3DProperties prop);  
    
}
