package org.neuroph.contrib.model.errorestimation;


import org.neuroph.contrib.model.metricevaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.model.metricevaluation.domain.MetricResult;
import org.neuroph.contrib.model.metricevaluation.evaluators.MetricsEvaluator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Skeleton class which makes easy to implement concrete ErrorEstimationMethod algorithms
 */
public abstract class AbstractErrorEstimationMethod implements ErrorEstimationMethod {

    private static Logger LOG = LoggerFactory.getLogger(KFoldCrossValidation.class.getName());


    private NeuralNetworkEvaluationService evaluationService = new NeuralNetworkEvaluationService();
    private Sampling sampling;
    private List<MetricResult> results;

    public AbstractErrorEstimationMethod(final Sampling sampling) {
        this.sampling = sampling;
    }

    /**
     * @param neuralNetwork supervised neural network
     * @param dataSet       test data used to create data samples
     * @return average metrics computed over all samples
     */
    @Override
    public MetricResult computeErrorEstimate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet dataSet) {

        initializeData(dataSet);
        calculateResults(neuralNetwork, dataSet);

        return MetricResult.averageFromMultipleRuns(results);
    }

    private void initializeData(DataSet dataSet) {
        evaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));
        results = new ArrayList<>();
        dataSet.shuffle();
    }

    private void calculateResults(NeuralNetwork<BackPropagation> neuralNetwork, DataSet dataSet) {
        List<DataSet> samples = sampling.sample(dataSet);

        //TODO Good place for parallelization.
        // But in order to make this possible NeuralNetwork must be cloneable or immutable
        for (int i = 0; i < samples.size(); i++) {
            trainNetwork(neuralNetwork, samples, i);
            evaluateResults(neuralNetwork, samples, i);
            restartNeuralNetwork(neuralNetwork);
        }
    }

    private void trainNetwork(NeuralNetwork<BackPropagation> neuralNetwork, List<DataSet> samples, int i) {
        DataSet trainSet = createTrainSet(samples, i);
        neuralNetwork.learn(trainSet);
    }

    private void evaluateResults(NeuralNetwork<BackPropagation> neuralNetwork, List<DataSet> samples, int i) {
        DataSet validationSet = createValidationSet(samples, i);
        evaluationService.evaluate(neuralNetwork, validationSet);
        results.add(evaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult());
    }


    private void restartNeuralNetwork(NeuralNetwork<BackPropagation> neuralNetwork) {
        neuralNetwork.reset();
    }


    protected abstract DataSet createTrainSet(List<DataSet> samples, int validationSampleIndex);

    protected abstract DataSet createValidationSet(List<DataSet> folds, int validationSampleIndex);

}
