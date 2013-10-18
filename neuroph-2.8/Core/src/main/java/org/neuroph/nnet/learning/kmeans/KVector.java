package org.neuroph.nnet.learning.kmeans;

/**
 * Represents feature vector used in k-means clustering algorithm
 * @author Zoran Sevarac
 * @author Uros Stojkic
  */
public class KVector {
    
    /**
     * Values of feature vector
     */
    private double[] values;  
    
    /**
     * Cluster to which this vector is assigned during clustering
     */
    private Cluster cluster;
    
    
    /**
     * Distance fro other vector (used by K nearest neighbour)
     */
    private double distance;

     
    public KVector(int size) {
        this.values = new double[size];
    }
    
    public KVector(double[] values) {
        this.values = values;
    }
        
    
    public void setValueAt(int idx, double value) {
        this.values[idx] = value;
    }
    
    public double getValueAt(int idx) {
        return values[idx];
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
         
    
   public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        // remove this from old cluster and assign it to new
        if (this.cluster != null) {
            this.cluster.removePoint(this);
        }
        this.cluster = cluster;
    }
    
    /**
     * Calculates and returns intensity of this vector
     * @return intensity of this vector
     */
    public double getIntensity() {
        double intensity = 0;
        
        for(double value : values) {
            intensity += value*value;
        }
        
        intensity = Math.sqrt(intensity);
        
        return intensity;
    }
    
    /**
     * Calculates and returns euclidean distance of this vector from the given cluster
     * @param cluster
     * @return euclidean distance of this vector from given cluster
     */
    public double distanceFrom(KVector otherVector) {
        // get values and do this in loop
        double[] otherValues = otherVector.getValues();
        
        double distance = 0;
        
        for(int i=0; i<values.length; i++) {
            distance += Math.pow(otherValues[i]-values[i], 2);
        }
        
        distance = Math.sqrt(distance);
        
        return distance;        
    }      
    
    public int size() {
        return values.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KMeansVector{");
        for(int i=0; i<values.length; i++)
            sb.append("["+i+"]="+values[i]+",");
        
        sb.append('}');
        
        return  sb.toString();
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    
    
}