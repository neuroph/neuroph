package org.neuroph.contrib.model.selection.sampling;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.sample.Sampling;

import java.util.*;

public class RandomSamplingWithoutRepetition implements Sampling {

    private int numberOfFolds;

    public RandomSamplingWithoutRepetition(final int numberOfFolds) {
        this.numberOfFolds = numberOfFolds;
    }

    @Override
    public List<DataSet> sample(DataSet dataSet) {

        int foldSize = dataSet.getRows().size() / numberOfFolds;
        List<DataSet> dataSets = new ArrayList<>(numberOfFolds);
        Deque<DataSetRow> dataDeque = new ArrayDeque<>();

        dataDeque.addAll(dataSet.getRows());

        for (int i = 0; i < numberOfFolds; i++) {
            DataSet foldSet = new DataSet(dataSet.getInputSize(), dataSet.getOutputSize());

            for (int j = 0; j < foldSize; j++) {
                foldSet.addRow(dataDeque.pop());
            }
            dataSets.add(foldSet);
        }
        return dataSets;
    }
}
