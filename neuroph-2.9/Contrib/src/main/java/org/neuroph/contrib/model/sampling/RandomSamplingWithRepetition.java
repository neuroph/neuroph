package org.neuroph.contrib.model.sampling;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.*;

/**
 * Sampling algorithm where each element can be placed only in one sample
 */
public class RandomSamplingWithRepetition extends AbstractSampling {

    private DataSet dataSet;

    public RandomSamplingWithRepetition(final int numberOfSamples) {
        super(numberOfSamples);
    }

    @Override
    protected int getSampleSize() {
        return dataSet.size();
    }

    @Override
    protected DataSetRow getNextDataSetRow() {
        return dataSet.getRowAt(new Random().nextInt(dataSet.size()));
    }

    @Override
    protected void populateInternalDataStructure(DataSet dataSet) {
        this.dataSet = dataSet;
    }

}
