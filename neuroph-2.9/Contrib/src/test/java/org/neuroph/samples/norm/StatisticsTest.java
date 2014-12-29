package org.neuroph.samples.norm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class StatisticsTest {

	private static final double LARGEST_MEAN_ERROR = 0.00001;
	private static final double LARGEST_MIN_MAX_ERROR = 0;

	@Test
	public void testMeanNoInput() {
		DataSet dataSet = createDataSetWithOneEmtyRow();
		double[] meanByColumns = DataSetStatistics.calculateMean(dataSet);
		assertEquals(0, meanByColumns.length);
	}

	@Test
	public void testMeanOneRowOneColumn() {
		double value = 3;
		DataSet dataSet = createDataSetWithOneRowAndOneColumn(value);

		double[] meanByColumns = DataSetStatistics.calculateMean(dataSet);
		assertEquals(value, meanByColumns[0], LARGEST_MEAN_ERROR);
	}

	@Test
	public void testMeanOneRowManySameValueColumns() {
		double value = 3;
		DataSet dataSet = createDataSetWithOneRowAndManySameValueColumns(value);

		double[] meanByColumns = DataSetStatistics.calculateMean(dataSet);
		for (double columnMean : meanByColumns)
			assertEquals(value, columnMean, LARGEST_MEAN_ERROR);
	}

	@Test
	public void testMeanOneRowManyDifferentValueColumns() {
		double value1 = 2;
		double value2 = 2;
		double value3 = 4;
		double value4 = 4;
		double[] firstRowData = new double[] { value1, value2, value3, value4 };
		DataSetRow firstRow = createDataRow(firstRowData);
		DataSet dataSet = createDataSetFromRows(firstRow);

		double[] meanByColumns = DataSetStatistics.calculateMean(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(firstRowData[i], meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}

	@Test
	public void testMeanManyRowManySameValueColumns() {
		double value11 = 1;
		double value12 = 2;
		double value13 = 3;

		double value21 = 1;
		double value22 = 2;
		double value23 = 3;

		double value31 = 1;
		double value32 = 2;
		double value33 = 3;

		double[] firstRowData = new double[] { value11, value12, value13 };
		double[] secondRowData = new double[] { value21, value22, value23 };
		double[] thirdRowData = new double[] { value31, value32, value33 };

		DataSetRow firstRow = createDataRow(firstRowData);
		DataSetRow secondRow = createDataRow(secondRowData);
		DataSetRow thirdRow = createDataRow(thirdRowData);
		DataSet dataSet = createDataSetFromRows(firstRow, secondRow, thirdRow);

		double[] meanByColumns = DataSetStatistics.calculateMean(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(firstRowData[i], meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}

	@Test
	public void testMeanManyRowManyDifferentValueColumns() {
		double value11 = 2;
		double value12 = 2;
		double value13 = 2;

		double value21 = 4;
		double value22 = 4;
		double value23 = 4;

		double value31 = 6;
		double value32 = 6;
		double value33 = 6;

		double[] firstRowData = new double[] { value11, value12, value13 };
		double[] secondRowData = new double[] { value21, value22, value23 };
		double[] thirdRowData = new double[] { value31, value32, value33 };

		DataSetRow firstRow = createDataRow(firstRowData);
		DataSetRow secondRow = createDataRow(secondRowData);
		DataSetRow thirdRow = createDataRow(thirdRowData);
		DataSet dataSet = createDataSetFromRows(firstRow, secondRow, thirdRow);

		double[] meanByColumns = DataSetStatistics.calculateMean(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(secondRowData[i], meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}

	@Test
	public void testMaxNoInput() {
		DataSet dataSet = createDataSetWithOneEmtyRow();
		double[] maxByColumns = DataSetStatistics.calculateMaxByColumns(dataSet);
		assertEquals(0, maxByColumns.length);
	}

	@Test
	public void testMaxOneRowOneColumn() {
		double value = 3;
		DataSet dataSet = createDataSetWithOneRowAndOneColumn(value);

		double[] maxByColumns = DataSetStatistics.calculateMaxByColumns(dataSet);
		assertEquals(value, maxByColumns[0], LARGEST_MIN_MAX_ERROR);
	}

	@Test
	public void testMaxOneRowManySameValueColumns() {
		double value = 3;
		DataSet dataSet = createDataSetWithOneRowAndManySameValueColumns(value);

		double[] maxByColumns = DataSetStatistics.calculateMaxByColumns(dataSet);
		for (double columnMaxValue : maxByColumns)
			assertEquals(value, columnMaxValue, LARGEST_MIN_MAX_ERROR);
	}

	@Test
	public void testMaxOneRowManyDifferentValueColumns() {
		double value1 = 1;
		double value2 = 2;
		double value3 = 3;
		double[] firstRowData = new double[] { value1, value2, value3 };
		DataSetRow firstRow = createDataRow(firstRowData);
		DataSet dataSet = createDataSetFromRows(firstRow);

		double[] maxByColumns = DataSetStatistics.calculateMaxByColumns(dataSet);
		for (int i = 0; i < maxByColumns.length; i++)
			assertEquals(firstRowData[i], maxByColumns[i], LARGEST_MIN_MAX_ERROR);
	}

	@Test
	public void testMaxManyRowManySameValueColumns() {
		double value11 = 1;
		double value12 = 2;
		double value13 = 3;

		double value21 = 1;
		double value22 = 2;
		double value23 = 3;

		double value31 = 1;
		double value32 = 2;
		double value33 = 3;

		double[] firstRowData = new double[] { value11, value12, value13 };
		double[] secondRowData = new double[] { value21, value22, value23 };
		double[] thirdRowData = new double[] { value31, value32, value33 };

		DataSetRow firstRow = createDataRow(firstRowData);
		DataSetRow secondRow = createDataRow(secondRowData);
		DataSetRow thirdRow = createDataRow(thirdRowData);
		DataSet dataSet = createDataSetFromRows(firstRow, secondRow, thirdRow);

		double[] meanByColumns = DataSetStatistics.calculateMaxByColumns(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(firstRowData[i], meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}

	@Test
	public void testMaxManyRowManyDifferentValueColumns() {
		double value11 = 2;
		double value12 = 2;
		double value13 = 2;

		double value21 = 4;
		double value22 = 4;
		double value23 = 4;

		double value31 = 6;
		double value32 = 6;
		double value33 = 6;

		double[] firstRowData = new double[] { value11, value12, value13 };
		double[] secondRowData = new double[] { value21, value22, value23 };
		double[] thirdRowData = new double[] { value31, value32, value33 };

		DataSetRow firstRow = createDataRow(firstRowData);
		DataSetRow secondRow = createDataRow(secondRowData);
		DataSetRow thirdRow = createDataRow(thirdRowData);
		DataSet dataSet = createDataSetFromRows(firstRow, secondRow, thirdRow);

		double[] meanByColumns = DataSetStatistics.calculateMaxByColumns(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(6, meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}
	
	@Test
	public void testMinNoInput() {
		DataSet dataSet = createDataSetWithOneEmtyRow();
		double[] maxByColumns = DataSetStatistics.calculateMinByColumns(dataSet);
		assertEquals(0, maxByColumns.length);
	}

	@Test
	public void testMinOneRowOneColumn() {
		double value = 3;
		DataSet dataSet = createDataSetWithOneRowAndOneColumn(value);

		double[] maxByColumns = DataSetStatistics.calculateMinByColumns(dataSet);
		assertEquals(value, maxByColumns[0], LARGEST_MIN_MAX_ERROR);
	}

	@Test
	public void testMinOneRowManySameValueColumns() {
		double value = 3;
		DataSet dataSet = createDataSetWithOneRowAndManySameValueColumns(value);

		double[] maxByColumns = DataSetStatistics.calculateMinByColumns(dataSet);
		for (double columnMaxValue : maxByColumns)
			assertEquals(value, columnMaxValue, LARGEST_MIN_MAX_ERROR);
	}

	@Test
	public void testMinOneRowManyDifferentValueColumns() {
		double value1 = 1;
		double value2 = 2;
		double value3 = 3;
		double[] firstRowData = new double[] { value1, value2, value3 };
		DataSetRow firstRow = createDataRow(firstRowData);
		DataSet dataSet = createDataSetFromRows(firstRow);

		double[] maxByColumns = DataSetStatistics.calculateMinByColumns(dataSet);
		for (int i = 0; i < maxByColumns.length; i++)
			assertEquals(firstRowData[i], maxByColumns[i], LARGEST_MIN_MAX_ERROR);
	}

	@Test
	public void testMinManyRowManySameValueColumns() {
		double value11 = 1;
		double value12 = 2;
		double value13 = 3;

		double value21 = 1;
		double value22 = 2;
		double value23 = 3;

		double value31 = 1;
		double value32 = 2;
		double value33 = 3;

		double[] firstRowData = new double[] { value11, value12, value13 };
		double[] secondRowData = new double[] { value21, value22, value23 };
		double[] thirdRowData = new double[] { value31, value32, value33 };

		DataSetRow firstRow = createDataRow(firstRowData);
		DataSetRow secondRow = createDataRow(secondRowData);
		DataSetRow thirdRow = createDataRow(thirdRowData);
		DataSet dataSet = createDataSetFromRows(firstRow, secondRow, thirdRow);

		double[] meanByColumns = DataSetStatistics.calculateMinByColumns(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(firstRowData[i], meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}

	@Test
	public void testMinManyRowManyDifferentValueColumns() {
		double value11 = 2;
		double value12 = 2;
		double value13 = 2;

		double value21 = 4;
		double value22 = 4;
		double value23 = 4;

		double value31 = 6;
		double value32 = 6;
		double value33 = 6;

		double[] firstRowData = new double[] { value11, value12, value13 };
		double[] secondRowData = new double[] { value21, value22, value23 };
		double[] thirdRowData = new double[] { value31, value32, value33 };

		DataSetRow firstRow = createDataRow(firstRowData);
		DataSetRow secondRow = createDataRow(secondRowData);
		DataSetRow thirdRow = createDataRow(thirdRowData);
		DataSet dataSet = createDataSetFromRows(firstRow, secondRow, thirdRow);

		double[] meanByColumns = DataSetStatistics.calculateMinByColumns(dataSet);
		for (int i = 0; i < meanByColumns.length; i++) {
			assertEquals(2, meanByColumns[i], LARGEST_MEAN_ERROR);
		}
	}


	private DataSet createDataSetWithOneEmtyRow() {
		double[] firstRowData = new double[] {};
		DataSetRow firstRow = createDataRow(firstRowData);
		return createDataSetFromRows(firstRow);
	}

	private DataSet createDataSetWithOneRowAndOneColumn(double value) {
		double[] firstRowData = new double[] { value };
		DataSetRow firstRow = createDataRow(firstRowData);
		return createDataSetFromRows(firstRow);
	}

	private DataSet createDataSetWithOneRowAndManySameValueColumns(double value) {
		double[] firstRowData = new double[] { value, value, value, value, value };
		DataSetRow firstRow = createDataRow(firstRowData);
		return createDataSetFromRows(firstRow);
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
