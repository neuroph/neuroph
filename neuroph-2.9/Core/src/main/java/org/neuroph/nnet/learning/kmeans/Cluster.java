package org.neuroph.nnet.learning.kmeans;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single cluster, with corresponding centroid and assigned vectors
 * @author Zoran Sevarac
 */
public class Cluster {    
    
    /**
     * Centroid for this cluster
     */
    KVector centroid;
    
    /**
     * Vectors assigned to this cluster during clustering
     */
    List<KVector> vectors;    

    
    public Cluster() {
        this.vectors = new ArrayList<>();
    }
   
    public KVector getCentroid() {
        return centroid;
    }

    public void setCentroid(KVector centroid) {
        this.centroid = centroid;
    }
    
    
    public void removePoint(KVector point) {
        vectors.remove(point);
    }

    public List<KVector> getPoints() {
        return vectors;
    }
    
    /**
     * Calculate and return avg sum vector for all vectors
     * @return avg sum vector 
     */
    public double[] getAvgSum() {
        double size = vectors.size();
        double[] avg = new double[vectors.get(0).size()];
        
        for(KVector item : vectors) {
            double[] values = item.getValues();
            
            for(int i=0; i<values.length; i++ ) {
                avg[i] += values[i]/size;
            }
            
        }
        
        return avg;
    }
    

    /**
     * Returns true if two clusters have same centroid
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Cluster other = (Cluster) obj;
        double[] otherValues = other.getCentroid().getValues();
        double[] values = other.getCentroid().getValues();
        // do this ina for loop here
        for(int i=0; i<centroid.size(); i++) {
            if (otherValues[i] != values[i])
                return false;
        }
                       
        return true;
    }

    /**
     * Assignes vector to this cluster.
     * @param vector vector to assign
     */
    public void assignVector(KVector vector) {
        // if vector's cluster is allready set to this, save some cpu cycles
        if (vector.getCluster() != this) { 
            vector.setCluster(this);
            vectors.add(vector);
        }
    }
    
    /**
     * Returns number of vectors assigned to this cluster.
     * @return number of vectors assigned to this cluster
     */
    public int size() {
        return vectors.size();
    }
    
    
    
}
