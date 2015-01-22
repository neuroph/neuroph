/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
            
        // spoj classificationMeasure and ClassificationMetric
        
        Evaluation.runFullEvaluation(nnet, dataSet);
         
    }
    
}
