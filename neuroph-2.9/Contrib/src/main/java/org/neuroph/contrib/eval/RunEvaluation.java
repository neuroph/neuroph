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
        NeuralNetwork nnet = new MultiLayerPerceptron(4, 3, 6);
        DataSet dataSet = new DataSet(4, 6);
        
        
        // pripremi mrezu istreniranu sa MICR karakterima
        // pripremi dataset za trening i testiranje
        // omoguciti da svaki DataSetRow ima label/klasu - da li je to neophodno?
        
        // napravi neki test za evaluaciju MICR: isterniranu MICR mrezu i dataset
        
        Evaluation.runFullEvaluation(nnet, dataSet);
        
        // ispisi rezultate evaluacije
        // srednju kvadratnu gresku
        // matricu konfuzije
        // mere klasifikacije
        
        
        
    }
    
}
