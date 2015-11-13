package org.neuroph.contrib.model.errorestimation;

import org.neuroph.contrib.eval.ClassifierEvaluator;
import org.neuroph.contrib.eval.ErrorEvaluator;
import org.neuroph.contrib.eval.Evaluator;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author zoran
 */
public class RunCrossValidation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // test subsampling here too with some small dataset
        
        NeuralNetwork nnet = NeuralNetwork.createFromFile("MIcrNet1.nnet");
        DataSet dataSet =  DataSet.load("MICRData.tset");

        // get class labels from output neurons
        String[] classNames = new String[nnet.getOutputsCount()];// = {"LeftHand", "RightHand", "Foot", "Rest"};        
        int i = 0;
        for (Neuron n : nnet.getOutputNeurons()) {
            classNames[i] = n.getLabel();
            i++;
        }
        
        
        CrossValidation crossval = new CrossValidation(nnet, dataSet, 5);
        crossval.addEvaluator(new ClassifierEvaluator.MultiClass(classNames)); // add multi class here manualy to make it independent from data set
                                                                                   // data set should hav ecolumn names when loading/creating , not hardcocd
        //   crossval.setSampling(null);
        
        crossval.run();
        CrossValidationResult results = crossval.getResult();

 
        System.out.println(results);
        
       // razmisli kako da uzmes rezultate i kako da ih prikazes u Neuroph studio - vuci ih direktno iz evaluatora
       // i kako da integrises ovo kroz training dialog -  samo dodati opciju KFold u trening dijalogu
        // tokom kfoldinga treba prikazivati gresku i desavanja tokom treninga - izlozi learning rule; napravi neki event listening za crossvalidaciju!!!
        // svaku istreniranu mrezu sacuvati, amozda negde i rezultate testiranja
        
        // potrebno je na kraju izracunati i srednju vrednost/statistikuu svih mera klasifikacije
        // takodje napravi boostraping - da radi ovo isto samo sa drugim sampling algoritmom
        
        
//        System.out.println("MeanSquare Error: " + crossval.getEvaluator(ErrorEvaluator.class).getResult());
//      
//        ClassificationEvaluator evaluator = crossval.getEvaluator(ClassificationEvaluator.MultiClass.class);              
//        ConfusionMatrix confusionMatrix = evaluator.getResult();        
//        
//        System.out.println("Confusion Matrix: \r\n"+confusionMatrix.toString());
//                      
//        System.out.println("Classification metrics: ");   
//        
//        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);     // add all of these to result 
//        // createaverage statisticss from ClassificationMetrics
//        
//        for(ClassificationMetrics cm : metrics)
//            System.out.println(cm.toString());
//          
//        
    }
    
}
