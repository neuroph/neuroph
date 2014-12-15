package org.neuroph.contrib.model.selection;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.contrib.evaluation.evaluators.MetricsEvaluator;
import org.neuroph.contrib.model.selection.sampling.RandomSamplingWithoutRepetition;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Error estimation method which uses sampling without repetition
 */
public class KFoldCrossValidation implements ErrorEstimationMethod {

    private static Logger LOG = LoggerFactory.getLogger(KFoldCrossValidation.class);

    //TODO this is ideal for dependency injection
    private NeuralNetworkEvaluationService evaluationService = new NeuralNetworkEvaluationService();
    private Sampling sampling;
    private int numberOfFolds;

    /**
     * Default constructor for creating KFold error estimation
     * @param numberOfFolds defines number of folds where
     */
    public KFoldCrossValidation(int numberOfFolds) {
        this.numberOfFolds = numberOfFolds;
        this.sampling = new RandomSamplingWithoutRepetition(numberOfFolds);
    }

    /**
     * @param neuralNetwork supervised neural network
     * @param dataSet       test data used to create other sub-sets
     * @return average metrics for all folds
     */
    @Override
    public MetricResult computeErrorEstimate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet dataSet) {
        evaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));
        dataSet.shuffle();

        List<DataSet> folds = sampling.sample(dataSet);
        List<MetricResult> results = new ArrayList<>();

        for (int i = 0; i < folds.size(); i++) {
            DataSet trainSet = new DataSet(dataSet.getInputSize(), dataSet.getOutputSize());
            DataSet validationSet = new DataSet(dataSet.getInputSize(), dataSet.getOutputSize());
            //TODO DataSets should be immutable
            //TODO there should be an easy way to merge 2 datasets
            for (int j = 0; j < folds.size(); j++) {
                if (i != j) {
                    for (DataSetRow dataSetRow : folds.get(j).getRows()) {
                        trainSet.addRow(dataSetRow);
                    }
                } else {
                    validationSet = folds.get(i);
                }
            }
            neuralNetwork.learn(trainSet);
            evaluationService.evaluate(neuralNetwork, validationSet);
            results.add(evaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult());

            restartNeuralNetwork(neuralNetwork);

            LOG.info(evaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult().toString());

        }

        return MetricResult.averageFromMultipleRuns(results);
    }

    private void restartNeuralNetwork(NeuralNetwork<BackPropagation> neuralNetwork) {
        neuralNetwork.randomizeWeights(new NguyenWidrowRandomizer(-0.5, 0.5));
    }


}
