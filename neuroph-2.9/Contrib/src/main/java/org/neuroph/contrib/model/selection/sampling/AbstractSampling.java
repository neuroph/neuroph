package org.neuroph.contrib.model.selection.sampling;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.sample.Sampling;

import java.util.ArrayList;
import java.util.List;

/**
 * Skeleton class which makes easy to implement concrete Sampling algorithms
 */
public abstract class AbstractSampling implements Sampling {

    private final int numberOfFolds;

    public AbstractSampling(int numberOfFolds) {
        this.numberOfFolds = numberOfFolds;
    }


    /**
     * Skeleton implementation for sample method
     *
     * @param dataSet initial data set
     * @return DataSets created using sampling algorithm
     */
    @Override
    public List<DataSet> sample(DataSet dataSet) {
        populateInternalDataStructure(dataSet);

        List<DataSet> dataSets = new ArrayList<>(numberOfFolds);
        for (int i = 0; i < numberOfFolds; i++) {
            dataSets.add(createDataSetFold(dataSet));
        }
        return dataSets;
    }


    private DataSet createDataSetFold(DataSet dataSet) {
        int numberOfElements = dataSet.getRows().size();
        int foldSize = numberOfElements / numberOfFolds;

        DataSet foldSet = new DataSet(dataSet.getInputSize(), dataSet.getOutputSize());
        for (int j = 0; j < foldSize; j++) {
            foldSet.addRow(getNextDataSetRow());
        }

        return foldSet;
    }


    /**
     * SPI method which has to  be implemented in concrete Sampling algorithms
     *
     * @return DataSetRow using concrete sampling algorithm
     */
    protected abstract DataSetRow getNextDataSetRow();

    /**
     * SPI method which has to  be implemented in concrete Sampling algorithms
     *
     * @param dataSet creates and populates data structure required by sampling algorithm
     */
    protected abstract void populateInternalDataStructure(DataSet dataSet);


}
