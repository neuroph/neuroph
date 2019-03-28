package org.neuroph.samples.eval;

import org.neuroph.eval.Evaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author zoran
 */
public class RunExampleEvaluation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NeuralNetwork nnet = NeuralNetwork.createFromFile("irisNet.nnet");
        DataSet dataSet =  DataSet.createFromFile("data_sets/iris_data_normalised.txt", 4, 3, ",");
        
        Evaluation.runFullEvaluation(nnet, dataSet);
         
    }
    
}
