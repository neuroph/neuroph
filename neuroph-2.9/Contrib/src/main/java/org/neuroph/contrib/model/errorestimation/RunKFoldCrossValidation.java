/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.model.errorestimation;

import org.neuroph.contrib.eval.ClassificationEvaluator;
import org.neuroph.contrib.eval.ErrorEvaluator;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author zoran
 */
public class RunKFoldCrossValidation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // test subsampling here too with some small dataset
        
        NeuralNetwork nnet = NeuralNetwork.createFromFile("MIcrNet1.nnet");
        DataSet dataSet =  DataSet.load("MICRData.tset");

        KFoldCrossValidation crossval = new KFoldCrossValidation(nnet, dataSet, 5);
        crossval.addEvaluator(new ClassificationEvaluator.MultiClass(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"})); // add multi class here manualy to make it independent from data set
                                                                                   // data set should hav ecolumn names when loading/creating , not hardcocd
        //   crossval.setSampling(null);
        
        crossval.run();
        
       // razmisli kako da uzmes rezultate i kako da ih prikazes u Neuroph studio - vuci ih direktno iz evaluatora
       // i kako da integrises ovo kroz training dialog -  samo dodati opciju KFold u trening dijalogu
        // tokom kfoldinga treba prikazivati gresku i desavanja tokom treninga 
        // svaku istreniranu mrezu sacuvati, amozda negde i rezultate testiranja
        
        // potrebno je na kraju izracunati i srednju vrednost/statistikuu svih mera klasifikacije
        // takodje napravi boostraping - da radi ovo isto samo sa drugim sampling algoritmom
        
        
        System.out.println("##############################################################################");
        System.out.println("MeanSquare Error: " + crossval.getEvaluator(ErrorEvaluator.class).getResult());
        System.out.println("##############################################################################");
      
        ClassificationEvaluator evaluator = crossval.getEvaluator(ClassificationEvaluator.MultiClass.class);
                
        ConfusionMatrix confusionMatrix = evaluator.getConfusionMatrix();        
        
        System.out.println("Confusion Matrix: \r\n"+confusionMatrix.toString());
                      
        System.out.println("##############################################################################");
        System.out.println("Classification metrics: ");        
        ClassificationMetrics[] metrics = evaluator.getResult();     // add all of these to result 
     
        for(ClassificationMetrics cm : metrics)
            System.out.println(cm.toString());

        System.out.println("##############################################################################");            
        
    }
    
}
