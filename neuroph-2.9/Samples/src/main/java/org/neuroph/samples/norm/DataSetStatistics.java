package org.neuroph.samples.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Utility class with methods for calculating dataset statistics
 */
public class DataSetStatistics {

    /**
     *
     * @param dataSet Neuroph dataset
     * @return arithmetic mean for each variable in data set
     */
	public static double[] calculateMean(DataSet dataSet) {
		double[] mean = new double[dataSet.getInputSize()];

		for (DataSetRow row : dataSet.getRows()) {
			double[] currentInput = row.getInput();
			for (int i = 0; i < dataSet.getInputSize(); i++) {
				mean[i] += currentInput[i];
			}
		}
		for (int i = 0; i < dataSet.getInputSize(); i++) {
			mean[i] /= dataSet.getRows().size();
		}
		return mean;
	}

    /**
     *
     * @param dataSet Neuroph dataset
     * @return maximum value for each variable in data set
     */
	public static double[] calculateMaxByColumns(DataSet dataSet) {

		int inputSize = dataSet.getInputSize();
		double[] maxColumnElements = new double[inputSize];

		for (int i = 0; i < inputSize; i++) {
			maxColumnElements[i] = Double.MIN_VALUE;
		}

		for (DataSetRow dataSetRow : dataSet.getRows()) {
			double[] input = dataSetRow.getInput();
			for (int i = 0; i < inputSize; i++) {
				maxColumnElements[i] = Math.max(maxColumnElements[i], input[i]);
			}
		}

		return maxColumnElements;
	}

    /**
     *
     * @param dataSet Neuroph dataset
     * @return minimum value for each variable in data set
     */
	public static double[] calculateMinByColumns(DataSet dataSet) {

		int inputSize = dataSet.getInputSize();
		double[] minColumnElements = new double[inputSize];

		for (int i = 0; i < inputSize; i++) {
			minColumnElements[i] = Double.MAX_VALUE;
		}

		for (DataSetRow dataSetRow : dataSet.getRows()) {
			double[] input = dataSetRow.getInput();
			for (int i = 0; i < inputSize; i++) {
				minColumnElements[i] = Math.min(minColumnElements[i], input[i]);
			}
		}
		return minColumnElements;
	}

}
