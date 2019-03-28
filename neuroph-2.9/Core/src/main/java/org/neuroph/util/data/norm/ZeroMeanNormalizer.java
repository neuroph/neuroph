package org.neuroph.util.data.norm;

import org.neuroph.util.DataSetStats;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Normalizes data sets by shifting all values in such way that data set has mean of 0 and std deviation of 1
 */
public class ZeroMeanNormalizer implements Normalizer {


    public void normalize(DataSet dataSet) {

        double[] maxInput = DataSetStats.calculateMaxByColumns(dataSet);
        double[] minInput = DataSetStats.calculateMinByColumns(dataSet);
        double[] meanInput = DataSetStats.calculateMeanByColumns(dataSet);

        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = row.getInput();

            for (int i = 0; i < dataSet.getInputSize(); i++) {
                double divider = maxInput[i] - minInput[i] == 0 ? 1 : maxInput[i] - minInput[i];
                normalizedInput[i] = (normalizedInput[i] - meanInput[i]) / divider; // should we delete with std ?
            }
            row.setInput(normalizedInput);
        }

    }

}
