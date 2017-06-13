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
        NeuralNetwork nnet = NeuralNetwork.createFromFile("MicrNetwork.nnet");
        DataSet dataSet =  DataSet.load("MicrDataColor.tset");
        
        Evaluation.runFullEvaluation(nnet, dataSet);
         
    }
    
}
