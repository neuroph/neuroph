package org.neuroph.contrib.model.errorestimation;


import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.ClassificationMetricsEvaluator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.data.sample.Sampling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import org.neuroph.contrib.model.sampling.RandomSamplingWithoutRepetition;
import org.neuroph.core.data.DataSetRow;

/**
 * Skeleton class which makes easy to implement concrete ErrorEstimationMethod algorithms
 */
public class KFoldCrossValidation  {

    private static Logger LOG = LoggerFactory.getLogger(KFoldCrossValidation.class.getName());


    private Evaluation evaluation = new Evaluation();
    private Sampling sampling;
    private List<ClassificationMetrics> results;

    
    
      /**
     * Default constructor for creating KFold error estimation
     *
     * @param numberOfSamples defines number of folds used in sampling algorithm
     */
    public KFoldCrossValidation(int numberOfSamples) {
        this(new RandomSamplingWithoutRepetition(numberOfSamples));
    }  
    
    public KFoldCrossValidation(final Sampling sampling) {
        this.sampling = sampling;
    }

    /**
     * @param neuralNetwork supervised neural network
     * @param dataSet       test data used to create data samples
     * @return average metrics computed over all samples
     */
    public ClassificationMetrics computeErrorEstimate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet dataSet) {

        initializeData(dataSet);
        calculateResults(neuralNetwork, dataSet);

       // return ClassificationMetrics.averageFromMultipleRuns(results);
        return null;
    }

    private void initializeData(DataSet dataSet) {
        evaluation.addEvaluator(ClassificationMetricsEvaluator.createForDataSet(dataSet));
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

    
    //mrezu ne trenirati ovde nego dati na testiranje ! znaci istreniranu mrezu dati na krosvalidaciju
    private void trainNetwork(NeuralNetwork<BackPropagation> neuralNetwork, List<DataSet> samples, int i) {
        DataSet trainSet = createTrainingSet(samples, i);
        neuralNetwork.learn(trainSet);
    }

    private void evaluateResults(NeuralNetwork<BackPropagation> neuralNetwork, List<DataSet> samples, int i) {
        DataSet testSet = createValidationSet(samples, i);
        evaluation.evaluateDataSet(neuralNetwork, testSet);
        // works for binary what if we have multiple classes
        results.add(evaluation.getEvaluator(ClassificationMetricsEvaluator.class).getResult()[0]);
    }


    private void restartNeuralNetwork(NeuralNetwork<BackPropagation> neuralNetwork) {
        neuralNetwork.reset();
    }


//    protected abstract DataSet createTrainingSet(List<DataSet> samples, int validationSampleIndex);
//
//    protected abstract DataSet createValidationSet(List<DataSet> folds, int validationSampleIndex);
    

    protected DataSet createTrainingSet(List<DataSet> samples, int validationSampleIndex) {
        DataSet trainSet = new DataSet(samples.get(0).getInputSize(), samples.get(0).getOutputSize());
        for (int j = 0; j < samples.size(); j++) {
            if (validationSampleIndex != j) {
                for (DataSetRow dataSetRow : samples.get(j).getRows()) {
                    trainSet.addRow(dataSetRow);
                }
            }
        }

        return trainSet;
    }


    protected DataSet createValidationSet(List<DataSet> folds, int validationSampleIndex) {
        return folds.get(validationSampleIndex);
    }


}
