package org.neuroph.contrib.eval;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 *
 * @author zoran
 */
public class RunEvaluation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NeuralNetwork nnet = NeuralNetwork.createFromFile("MicrNetwork.nnet");
        DataSet dataSet =  DataSet.load("MicrDataColor.tset");
        
        Evaluation.runFullEvaluation(nnet, dataSet);
         
    }
    
}
