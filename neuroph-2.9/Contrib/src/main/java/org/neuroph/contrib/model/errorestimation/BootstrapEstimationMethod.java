package org.neuroph.contrib.model.errorestimation;

import org.neuroph.contrib.model.sampling.RandomSamplingWithRepetition;
import org.neuroph.core.data.DataSet;

import java.util.List;

/**
 * Error estimation method which uses sampling with repetition. Each sample contains N elements which forms
 * so called E632 Bootstrap
 */
public class BootstrapEstimationMethod extends AbstractErrorEstimationMethod {

    /**
     * Default constructor for creating BootstrapEstimationMethod error estimation
     *
     * @param numberOfSamples defines number of bootstrap samples
     */
    public BootstrapEstimationMethod(int numberOfSamples) {
        super(new RandomSamplingWithRepetition(numberOfSamples));
    }

    @Override
    protected DataSet createTrainSet(List<DataSet> samples, int selectedSampleIndex) {
        return samples.get(selectedSampleIndex);
    }

    @Override
    protected DataSet createValidationSet(List<DataSet> samples, int selectedSampleIndex) {
        return samples.get((selectedSampleIndex + 1) % samples.size());
    }


}
