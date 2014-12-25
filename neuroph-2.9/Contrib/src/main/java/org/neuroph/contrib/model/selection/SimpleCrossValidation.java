package org.neuroph.contrib.model.selection;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.contrib.evaluation.evaluators.MetricsEvaluator;
import org.neuroph.contrib.model.selection.sampling.RandomSamplingWithoutRepetition;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

import java.util.List;

// 2 fold validation, first version of k fold
public class SimpleCrossValidation implements ErrorEstimationMethod { 

    //goood place for autowiring!!!
    NeuralNetworkEvaluationService evaluationService = new NeuralNetworkEvaluationService();

    public MetricResult computeErrorEstimate(NeuralNetwork neuralNetwork, DataSet dataSet) {
        evaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));
        dataSet.shuffle();

        List<DataSet> folds = new RandomSamplingWithoutRepetition(2).sample(dataSet);

        DataSet trainSet = folds.get(0);
        DataSet validationSet = folds.get(1);

        neuralNetwork.learn(trainSet);
        evaluationService.evaluate(neuralNetwork, validationSet);

        return evaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult();
    }

}
