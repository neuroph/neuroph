/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.samples.eval;

import org.neuroph.contrib.eval.ClassifierEvaluator;
import org.neuroph.contrib.eval.ErrorEvaluator;
import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 *
 * @author zoran
 */
public class ClassifierEvaluationSample {

    public static void main(String[] args) {
        Evaluation evaluation = new Evaluation();
        evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
    
        String[] classNames = {"Virginica", "Setosa", "Versicolor"};
        
        
        MultiLayerPerceptron neuralNet = (MultiLayerPerceptron) NeuralNetwork.createFromFile("irisNet.nnet");
        DataSet dataSet = DataSet.createFromFile("data_sets/iris_data_normalised.txt", 4, 3, ",");
        
        evaluation.addEvaluator(new ClassifierEvaluator.MultiClass(classNames));
        evaluation.evaluateDataSet(neuralNet, dataSet);

        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        System.out.println("Confusion matrrix:\r\n");
        System.out.println(confusionMatrix.toString() + "\r\n\r\n");
        System.out.println("Classification metrics\r\n");
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        for (ClassificationMetrics cm : metrics) {
            System.out.println(cm.toString() + "\r\n");
        }
        System.out.println(average.toString());

    }

}
