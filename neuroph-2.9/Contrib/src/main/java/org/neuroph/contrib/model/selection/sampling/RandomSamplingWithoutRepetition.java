package org.neuroph.contrib.model.selection.sampling;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.*;

/**
 * Sampling algorithm where each element can be placed only in one sample
 */
public class RandomSamplingWithoutRepetition extends AbstractSampling {

    private DataSet dataSet;

    public RandomSamplingWithoutRepetition(final int numberOfFolds) {
        super(numberOfFolds);
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
