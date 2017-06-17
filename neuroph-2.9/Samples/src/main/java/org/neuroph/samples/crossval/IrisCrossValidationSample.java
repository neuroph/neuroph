package org.neuroph.samples.crossval;

import java.util.concurrent.ExecutionException;
import org.neuroph.eval.ClassifierEvaluator;
import org.neuroph.eval.CrossValidation;
import org.neuroph.eval.CrossValidationResult;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 *
 * @author zoran
 */
public class IrisCrossValidationSample {
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MultiLayerPerceptron neuralNet = (MultiLayerPerceptron) NeuralNetwork.createFromFile("irisNet.nnet");
        DataSet dataSet = DataSet.createFromFile("data_sets/iris_data_normalised.txt", 4, 3, ",");       
        String[] classNames = {"Virginica", "Setosa", "Versicolor"};
        
        CrossValidation crossval = new CrossValidation(neuralNet, dataSet, 10);
        crossval.addEvaluator(new ClassifierEvaluator.MultiClass(classNames));
                                                                                         
        crossval.run();
        CrossValidationResult results = crossval.getResult();
        results.printResult();        
    }
    

    
    
}
