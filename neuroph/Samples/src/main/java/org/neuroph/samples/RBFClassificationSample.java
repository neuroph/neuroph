package org.neuroph.samples;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.RBFLearning;

/**
 * TODO: use k-nearest neighbour to tweak sigma
 * @author zoran
 */
public class RBFClassificationSample implements LearningEventListener {
    
    /**
     *  Runs this sample
     */
    public static void main(String[] args) {    
          (new RBFClassificationSample()).run();
    }
    
    
    public void run() {
        // get the path to file with data
        String inputFileName = "data_sets/sine.csv";
        
        // create MultiLayerPerceptron neural network
        RBFNetwork neuralNet = new RBFNetwork(1, 15, 1);
        
        // create training set from file
        DataSet dataSet = DataSet.createFromFile(inputFileName, 1, 1, ",", false);

        RBFLearning learningRule = ((RBFLearning)neuralNet.getLearningRule());
        learningRule.setLearningRate(0.02);
        learningRule.setMaxError(0.01);
        learningRule.addListener(this);
                
        // train the network with training set
        neuralNet.learn(dataSet);      
                        
        System.out.println("Done training.");
        System.out.println("Testing network...");
        
        testNeuralNetwork(neuralNet, dataSet);                
    }
    
    /**
     * Prints network output for the each element from the specified training set.
     * @param neuralNet neural network
     * @param testSet test data set
     */
    public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {
        for(DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString( testSetRow.getInput() ) );
            System.out.println(" Output: " + Arrays.toString( networkOutput) );
        }
    }  
    
    @Override
    public void handleLearningEvent(LearningEvent event) {
        LMS lr = (LMS) event.getSource();
        System.out.println(lr.getCurrentIteration() + ". iteration | Total network error: " + lr.getTotalNetworkError());
    }    
    
}
