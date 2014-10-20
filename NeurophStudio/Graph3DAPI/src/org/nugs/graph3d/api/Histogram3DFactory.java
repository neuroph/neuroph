package org.nugs.graph3d.api;

/**
 * Provides methods to create 3D histogram graph
 * C - graph panel class that will be created and returned by factory methods 
 * P - class that is used to represent 3D point 
 * 
 * @author Zoran Sevarac
 * @author Milos Randjic
 * @param <C>
 * @param <P>
 */
public interface Histogram3DFactory<C, P> {
    
    /**
     * Creates and returns 3D histogram for given list of 3D points
     * @param points an array of 3d points
     * @return 3D histogram graph
     */
    public C createHistogram3D(P[] points);
    
    /**
     * Creates and returns 3D histogram for given list of 3D points and histogram graph properties
     * @param points an array of 3d points
     * @param prop
     * @return 3D histogram graph
     */
    public C createHistogram3D(P[] points, Histogram3DProperties prop);
    
}
