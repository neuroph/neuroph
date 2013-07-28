package org.neuroph.samples;

import java.io.File;
import java.io.FileNotFoundException;
import org.neuroph.core.data.BufferedDataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author zoran
 */
public class BufferedDataSetSample implements LearningEventListener {
    
    
    public static void main(String[] args) throws FileNotFoundException  {
        (new BufferedDataSetSample()).run();
        
    }
    
    public  void run() throws FileNotFoundException {
        String inputFileName = BufferedDataSetSample.class.getResource("data/iris_data_normalised.txt").getFile();
        
        // create MultiLayerPerceptron neural network
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 16, 3);
        
        BufferedDataSet irisDataSet = new BufferedDataSet(new File(inputFileName), 4, 3,  ",");
        
        neuralNet.getLearningRule().addListener(this);
        neuralNet.learn(irisDataSet);      
        
       // neuralNet.getLearningRule().setMaxError(0.001);
    }

    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation)event.getSource();
        System.out.println(bp.getCurrentIteration() + ". iteration : "+ bp.getTotalNetworkError());
    }
    
    
}
