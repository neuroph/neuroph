package org.neuroph.nnet.learning.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * 
 *   1. Pick an initial set of K centroids (this can be random or any other means)
 *   2. For each data point, assign it to the member of the closest centroid according to the given distance function
 *   3. Adjust the centroid position as the mean of all its assigned member data points. Go back to (2) until the membership isn't change and centroid position is stable.
 *
 * @author Zoran Sevarac
 * @author Uros Stojkic
 */
public class KMeansClustering {

    /**
     * Data/points to cluster
     */
    private DataSet dataSet;
    
    private KVector[] dataVectors;
    
    /**
     * Total number of clusters
     */
    private int numberOfClusters;
    
    /**
     * Clusters
     */
    private Cluster[] clusters;
        
    StringBuilder log=new StringBuilder();

    public KMeansClustering(DataSet dataSet) {
        this.dataSet = dataSet;
        this.dataVectors = new KVector[dataSet.size()];
        // iterate dataset and create dataVectors field
        this.dataVectors = new KVector[dataSet.size()];
         // iterate dataset and create dataVectors field
        int i=0;
        for(DataSetRow row : dataSet.getRows()) {
            KVector vector = new KVector(row.getInput());
            this.dataVectors[i]=vector;
            i++;
            
        }        
    }

    public KMeansClustering(DataSet dataSet, int numberOfClusters) {
        this.dataSet = dataSet;
        this.numberOfClusters = numberOfClusters;
        this.dataVectors = new KVector[dataSet.size()];
         // iterate dataset and create dataVectors field
        int i=0;
        for(DataSetRow row : dataSet.getRows()) {
            KVector vector = new KVector(row.getInput());
            this.dataVectors[i]=vector;
            i++;
            
        }
    }

    // find initial values for centroids/clusters
    // dont need to return string this is just for debuging and output in dialog
    // forgy and random partitions
    // http://en.wikipedia.org/wiki/K-means_clustering
    public void initClusters() {               
        
        ArrayList<Integer> idxList = new ArrayList<>();
        
        for(int i=0; i<dataSet.size(); i++) {
            idxList.add(i);
        }
        Collections.shuffle(idxList);
        
     //   log.append("Clusters initialized at:\n\n");
        
        clusters = new Cluster[numberOfClusters];
        for (int i = 0; i < numberOfClusters; i++) {
            clusters[i] = new Cluster();
            int randomIdx = idxList.get(i);
            KVector randomVector = dataVectors[randomIdx];
            clusters[i].setCentroid(randomVector);
            //log.append(randomVector.toString()+System.lineSeparator() );
        }
        //log.append(System.lineSeparator());

    }
    
    /**
     * Find and return the nearest cluster for the specified vector
     * @param vector
     * @return 
     */
    private Cluster getNearestCluster(KVector vector) {
        Cluster nearestCluster = null;
        double minimumDistanceFromCluster = Double.MAX_VALUE;
        double distanceFromCluster = 0;

        for (Cluster cluster : clusters) {
            distanceFromCluster = vector.distanceFrom(cluster.getCentroid());
            if (distanceFromCluster < minimumDistanceFromCluster) {
                minimumDistanceFromCluster = distanceFromCluster;
                nearestCluster = cluster;
            }
        }

        return nearestCluster;
    }

    // runs cluctering
    public void doClustering() {
        // throw exception if number of clusters is 0 
        if (numberOfClusters <= 0) {
            throw new RuntimeException("Error: Number of clusters must be greater then zero!");
        }
        
        // initialize clusters
        initClusters();
        
        // initial nearest cluster assignement
        for (KVector vector : dataVectors) { // Iterate all dataSet    
            Cluster nearestCluster = getNearestCluster(vector);
            nearestCluster.assignVector(vector);
        }

        // this is the loop doing the main thing
        //  keep re-calculating centroids and assigning points until there is no change
        boolean clustersChanged; // flag to indicate cluster change
        do {
            clustersChanged = false;            
            recalculateCentroids();                      

            for (KVector vector : dataVectors) {
                Cluster nearestCluster = getNearestCluster(vector);
                if (!vector.getCluster().equals(nearestCluster)) {
                    nearestCluster.assignVector(vector);
                    clustersChanged = true;
                }
            }
        } while(clustersChanged);
    }

    /**
     * Calculate new centroids as an average of all dataSet in cluster
     */
    private void recalculateCentroids() {
        // for each cluster do the following
        for (Cluster cluster : clusters) { // for each cluster
            if (cluster.size()>0) {         // that cointains data
                double[] avgSum = cluster.getAvgSum();  // calculate avg sum
                cluster.getCentroid().setValues(avgSum);  // and set new centroid to avg sum
            }                                  
        }
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet vectors) {
        this.dataSet = vectors;
    }

    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    public Cluster[] getClusters() {
        return clusters;
    }
    
    public String getLog() {
        return log.toString();
    }
}