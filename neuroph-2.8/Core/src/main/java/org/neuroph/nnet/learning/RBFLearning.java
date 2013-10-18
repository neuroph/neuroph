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
 * inicijalizuj centroide klastera na osnovu datas set-a
 * tako se inicijalizuju weights izmedju input i rbf layera
 * 
 * potrebno je jos dodati podesavanje sirine gausovih funkcija sigma pomocu k-nearest neighbour 
 * 
 * @author zoran
 */
public class RBFLearning extends LMS {

    @Override
    protected void onStart() {
        super.onStart();         
        
        // set weights between input and rbf layer using kmeans
        //- create KMeansVector for every row in dataset
        KMeansClustering kmeans = new KMeansClustering(getTrainingSet()); // this one should be able to use DataSet
        kmeans.setNumberOfClusters(neuralNetwork.getLayerAt(1).getNeuronsCount()); // set number of clusters as number of rbf neurons
        kmeans.doClustering();
        
        // get clusters (centroids)
        Cluster[] clusters = kmeans.getClusters();
        
        // assign each neuron to one cluster
        // use centroid vectors to initialize weights
        Layer rbfLayer = neuralNetwork.getLayerAt(1);
        int i=0;
        for(Neuron neuron : rbfLayer.getNeurons()) {
            KVector centroid = clusters[i].getCentroid();
            double weightValues[] = centroid.getValues();
            int c=0;
            for(Connection conn : neuron.getInputConnections()) {
                conn.getWeight().setValue(weightValues[c]);
                c++;
            }
            i++;
        }
        
        List<KVector> centroids = new ArrayList<>();        
        // get centroids from clusters
        for(Cluster cluster : clusters) {
            centroids.add(cluster.getCentroid());
        }
        
        int k=2;
        // use KNN to calculate sigma param - gausssian width
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
    
    private double calculateSigma(KVector centroid,  KVector[] nearestNeighbours) {
       double sigma = 0;
              
       for(KVector nn : nearestNeighbours){
           sigma += Math.pow( centroid.distanceFrom(nn), 2 ) ;
       }
       
       sigma = Math.sqrt(1/((double)nearestNeighbours.length)  * sigma);
       
       return sigma;
    }


    
   
    
}
