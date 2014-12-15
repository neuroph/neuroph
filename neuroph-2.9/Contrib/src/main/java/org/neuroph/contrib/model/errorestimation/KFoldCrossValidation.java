package org.neuroph.contrib.model.errorestimation;

import org.neuroph.contrib.model.sampling.RandomSamplingWithoutRepetition;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.List;

/**
 * Error estimation method which uses sampling without repetition. DataSet is divided into m folds,
 * where each fold contains N / m elements (N is number of rows in data set)
 */
public class KFoldCrossValidation extends AbstractErrorEstimationMethod {

    /**
     * Default constructor for creating KFold error estimation
     *
     * @param numberOfSamples defines number of folds used in sampling algorithm
     */
    public KFoldCrossValidation(int numberOfSamples) {
        super(new RandomSamplingWithoutRepetition(numberOfSamples));
    }

    @Override
    protected DataSet createTrainSet(List<DataSet> samples, int validationSampleIndex) {
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

    @Override
    protected DataSet createValidationSet(List<DataSet> folds, int validationSampleIndex) {
        return folds.get(validationSampleIndex);
    }

}
