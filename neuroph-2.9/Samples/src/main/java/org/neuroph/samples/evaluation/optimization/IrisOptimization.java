package org.neuroph.samples.evaluation.optimization;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.model.selection.optimizer.MultilayerPerceptronOptimazer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;

public class IrisOptimization {


    public static void main(String[] args) {
        String inputFileName = "/iris_data.txt";

        DataSet irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);

        BackPropagation bp = new BackPropagation();
        bp.setMaxIterations(100);
        bp.setMaxError(0.0001);

        NeuralNetwork neuralNet = new MultilayerPerceptronOptimazer<>()
                .withLearningRule(bp)
                .createOptimalModel(irisDataSet);

        NeuralNetworkEvaluationService.completeEvaluation(neuralNet, irisDataSet);

    }

}
