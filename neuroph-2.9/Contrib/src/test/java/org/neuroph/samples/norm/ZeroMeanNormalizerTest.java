package org.neuroph.samples.norm;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class ZeroMeanNormalizerTest {

	ZeroMeanNormalizer normalizer;

	@Before
	public void setUp() {
		normalizer = new ZeroMeanNormalizer();
	}

	@Test
	public void testOneRowWithManyElementsNormalization() {
		double[] inputRow1 = new double[] { 0, 2, 3, 4 };
		DataSetRow row1 = createDataRow(inputRow1);
		DataSet dataSet = createDataSetFromRows(row1);
		normalizer.normalize(dataSet);

		double[] columnMeans = DataSetStatistics.calculateMean(dataSet);
		for (int i = 0; i < dataSet.getInputSize(); i++) {
			double currentColumnMean = columnMeans[i];
			assertEquals(0.0, currentColumnMean, 0.01);
		}
	}

	@Test
	public void testTwoRowsWithOneElementNormalization() {
		double[] inputRow1 = new double[] { 2 };
		double[] inputRow2 = new double[] { 4 };
		DataSetRow row1 = createDataRow(inputRow1);
		DataSetRow row2 = createDataRow(inputRow2);
		DataSet dataSet = createDataSetFromRows(row1, row2);
		normalizer.normalize(dataSet);

		double[] columnMeans = DataSetStatistics.calculateMean(dataSet);
		for (int i = 0; i < dataSet.getInputSize(); i++) {
			double currentColumnMean = columnMeans[i];
			assertEquals(0.0, currentColumnMean, 0.01);
		}
	}

	private DataSet createDataSetFromRows(DataSetRow... rows) {
		DataSet dataSet = new DataSet(rows[0].getInput().length);
		for (DataSetRow row : rows) {
			dataSet.addRow(row);
		}
		return dataSet;
	}

	private DataSetRow createDataRow(double[] input) {
		DataSetRow row = new DataSetRow(input);
		return row;
	}

}
