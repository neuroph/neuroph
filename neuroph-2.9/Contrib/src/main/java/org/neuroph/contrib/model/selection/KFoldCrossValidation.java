package org.neuroph.contrib.model.selection;

import org.neuroph.contrib.evaluation.NeuralNetworkEvaluationService;
import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.contrib.evaluation.evaluators.MetricsEvaluator;
import org.neuroph.contrib.model.selection.sampling.RandomSamplingWithoutRepetition;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.random.NguyenWidrowRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class KFoldCrossValidation implements ErrorEstimationMethod {

    private static Logger LOG = LoggerFactory.getLogger(KFoldCrossValidation.class);

    //TODO this is ideal for dependency injection
    private NeuralNetworkEvaluationService evaluationService = new NeuralNetworkEvaluationService();

    private int numberOfFolds;

    public KFoldCrossValidation(int numberOfFolds) {
        this.numberOfFolds = numberOfFolds;
    }

    @Override
    public MetricResult computeErrorEstimate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet dataSet) {
        evaluationService.add(MetricsEvaluator.class, MetricsEvaluator.createEvaluator(dataSet));
        dataSet.shuffle();

        List<DataSet> folds = new RandomSamplingWithoutRepetition(numberOfFolds).sample(dataSet);

        List<MetricResult> results = new ArrayList<>();

        for (int i = 0; i < folds.size(); i++) {
//            LOG.info("Fold number: [{}]", i);
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
            LOG.info(evaluationService.resultFor(MetricsEvaluator.class).getEvaluationResult().toString());

//            neuralNetwork.reset();
//            neuralNetwork.getLearningRule().getErrorFunction().reset();
            neuralNetwork.randomizeWeights(new NguyenWidrowRandomizer(-0.5, 0.5));
        }


        return MetricResult.averageFromMultipleRuns(results);
    }


}
