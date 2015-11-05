package org.neuroph.nnet.learning;

import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.nnet.learning.kmeans.Cluster;
import org.neuroph.nnet.learning.kmeans.KMeansClustering;
import org.neuroph.nnet.learning.kmeans.KVector;
import org.neuroph.nnet.learning.knn.KNearestNeighbour;

/**
 * Learning rule for Radial Basis Function networks.
 * Use K-Means to determine centroids for hidden units, K-NearestNeighbour to set widths,
 * and LMS to tweak output neurons.
 * 
 * @author Zoran Sevarac sevarac@gmail.com
 */
public class RBFLearning extends LMS {

    /**
     * how many nearest neighbours to use when determining width (sigma) for
     * gaussian functions
     */
    int k=2;
    
    @Override
    protected void onStart() {
        super.onStart();         
        
        // set weights between input and rbf layer using kmeans
        KMeansClustering kmeans = new KMeansClustering(getTrainingSet());
        kmeans.setNumberOfClusters(neuralNetwork.getLayerAt(1).getNeuronsCount()); // set number of clusters as number of rbf neurons
        kmeans.doClustering();
        
        // get clusters (centroids)
        Cluster[] clusters = kmeans.getClusters();
        
        // assign each rbf neuron to one cluster
        // and use centroid vectors to initialize neuron's input weights
        Layer rbfLayer = neuralNetwork.getLayerAt(1);
        int i=0;
        for(Neuron neuron : rbfLayer.getNeurons()) {
            KVector centroid = clusters[i].getCentroid();
            double[] weightValues = centroid.getValues();
            int c=0;
            for(Connection conn : neuron.getInputConnections()) {
                conn.getWeight().setValue(weightValues[c]);
                c++;
            }
            i++;
        }
        
        // get cluster centroids as list
        List<KVector> centroids = new ArrayList<>();                
        for(Cluster cluster : clusters) {
            centroids.add(cluster.getCentroid());
        }        

        // use KNN to calculate sigma param - gausssian function width for each neuron
        KNearestNeighbour knn = new KNearestNeighbour();
        knn.setDataSet(centroids);
        
        int n = 0;
        for(KVector centroid : centroids) {
        // calculate and set sigma for each neuron in rbf layer
            KVector[] nearestNeighbours = knn.getKNearestNeighbours(centroid, k);
            double sigma = calculateSigma(centroid, nearestNeighbours); // calculate in method 
            Neuron neuron = rbfLayer.getNeuronAt(n);
            ((Gaussian)neuron.getTransferFunction()).setSigma(sigma);
            i++;
            
        }
        
                
    }
    
    /**
     * Calculates and returns  width of a gaussian function
     * @param centroid
     * @param nearestNeighbours
     * @return 
     */
    private double calculateSigma(KVector centroid,  KVector[] nearestNeighbours) {
       double sigma = 0;
              
       for(KVector nn : nearestNeighbours){
           sigma += Math.pow( centroid.distanceFrom(nn), 2 ) ;
       }
       
       sigma = Math.sqrt(1/((double)nearestNeighbours.length)  * sigma);
       
       return sigma;
    }


    
   
    
}
