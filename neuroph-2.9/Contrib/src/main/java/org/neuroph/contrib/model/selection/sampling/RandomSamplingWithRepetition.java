package org.neuroph.contrib.model.selection.sampling;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.sample.Sampling;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
