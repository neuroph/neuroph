package org.neuroph.contrib.model.sampling;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Sampling algorithm where each element can be placed in multiple samples
 */
public class RandomSamplingWithRepetition extends AbstractSampling {

    Deque<DataSetRow> dataDeque;


    public RandomSamplingWithRepetition(final int numberOfFolds) {
        super(numberOfFolds);
        dataDeque = new ArrayDeque<>();
    }

    @Override
    protected int getSampleSize() {
        return dataDeque.size();
    }

    @Override
    protected DataSetRow getNextDataSetRow() {
        return dataDeque.pop();
    }

    @Override
    protected void populateInternalDataStructure(DataSet dataSet) {
        dataDeque.addAll(dataSet.getRows());

    }
}
