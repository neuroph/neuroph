package org.neuroph.nnet.learning.knn;

import java.util.List;
import org.neuroph.nnet.learning.kmeans.KVector;

/**
 * for given vector
 * calculate distances to all vectors from list 
 * and find minimum vector
 * or sort vectors by distance and seelct
 * 
 * @author zoran
 */
public class KNearestNeighbour {
    
        private List<KVector> dataSet;
                      
        /**
         * http://en.wikipedia.org/wiki/Selection_algorithm
         * @param vector
         * @param k
         * @return 
         */
        public KVector[] getKNearestNeighbours( KVector vector, int k ) {                                       
            KVector[] nearestNeighbours = new KVector[k];                     
            
            // calculate distances for entire dataset
            for(KVector otherVector : dataSet) {
                double distance = vector.distanceFrom(otherVector);
                otherVector.setDistance(distance);                                
            }
                        
            for(int i=0; i<k; i++) {
                int minIndex=i;
                KVector minVector = dataSet.get(i);
                double minDistance = minVector.getDistance();
                
                for(int j=i+1; j<dataSet.size(); j++) {                  
                    if (dataSet.get(j).getDistance() <= minDistance) {
                        minVector=dataSet.get(j);
                        minDistance = minVector.getDistance();
                        minIndex=j;                        
                    }
                }
                                              
                // swap list[i] and list[minIndex]
                KVector temp = dataSet.get(i);
                dataSet.set(i, dataSet.get(minIndex));
                dataSet.set(minIndex, temp);                                
                
                nearestNeighbours[i] = dataSet.get(i);
            }
            

//            function select(list[1..n], k)
//                for i from 1 to k
//                    minIndex = i
//                    minValue = list[i]
//                    for j from i+1 to n
//                        if list[j] < minValue
//                            minIndex = j
//                            minValue = list[j]
//                    swap list[i] and list[minIndex]
//                return list[k]
            
            return nearestNeighbours;                                                                                        
        }       

    public List<KVector> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<KVector> dataSet) {
        this.dataSet = dataSet;
    }
        
        
    
}
