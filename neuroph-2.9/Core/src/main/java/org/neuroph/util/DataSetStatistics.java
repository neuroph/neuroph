/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.util;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * This class calculates statistics for data set.
 *
 * @author Arsenovic Aleksandar <salle18@gmail.com>
 */
public class DataSetStatistics {

    private final int rowSize;

    private final int rowCount;

    private final double[] mean;

    private final double[] max;

    private final double[] min;

    private final double[] sum;

    private final double[] var;

    private final double[] stdDev;

    private final double[] frequency;

    private final DataSet dataSet;
    
    public static final String MIN = "min";
    
    public static final String MAX = "max";
    
    public static final String MEAN = "mean";
    
    public static final String SUM = "sum";
    
    public static final String STD_DEV = "std dev";
    
    public static final String VAR = "var";
    
    public static final String FREQ = "freq";

    public DataSetStatistics(DataSet dataSet) {
        this.dataSet = dataSet;
        this.rowSize = dataSet.getInputSize() + dataSet.getOutputSize();
        this.rowCount = dataSet.getRows().size();
        this.mean = new double[this.rowSize];
        this.max = new double[this.rowSize];
        this.min = new double[this.rowSize];
        this.sum = new double[this.rowSize];
        this.var = new double[this.rowSize];
        this.stdDev = new double[this.rowSize];
        this.frequency = new double[this.rowSize];
        this.setDefaultValues();
    }

    /**
     * Sets default values for statistics.
     */
    private void setDefaultValues() {
        for (int i = 0; i < this.rowSize; i++) {
            this.max[i] = -Double.MAX_VALUE;
            this.min[i] = Double.MAX_VALUE;
        }
    }

    /**
     * Resets statistics values to default.
     */
    private void resetValues() {
        for (int i = 0; i < this.rowSize; i++) {
            this.sum[i] = 0;
            this.var[i] = 0;
            this.frequency[i] = -0.0;
        }
    }

    /**
     * Calculates basic statistics by columns of the dataset.
     */
    public void calculateStatistics() {
        this.resetValues();
        DataSetColumnType[] columnTypes = this.dataSet.getColumnTypes();
        for (DataSetRow dataSetRow : this.dataSet.getRows()) {
            double[] row = dataSetRow.toArray();
            for (int i = 0; i < this.rowSize; i++) {
                this.max[i] = Math.max(this.max[i], row[i]);
                this.min[i] = Math.min(this.min[i], row[i]);
                this.sum[i] += row[i];
                if (columnTypes[i] == DataSetColumnType.NOMINAL) {
                    this.frequency[i] += row[i];
                }
            }
        }

        for (int i = 0; i < this.rowSize; i++) {
            this.mean[i] = this.sum[i] / (double) this.rowCount;
            if (columnTypes[i] == DataSetColumnType.NOMINAL) {
                this.frequency[i] /= (double) this.rowCount;
            }
        }

        for (DataSetRow dataSetRow : this.dataSet.getRows()) {
            double[] row = dataSetRow.toArray();
            for (int i = 0; i < this.rowSize; i++) {
                double delta = row[i] - this.mean[i];
                this.var[i] += delta * delta;
            }
        }

        for (int i = 0; i < this.rowSize; i++) {
            this.var[i] /= (double) this.rowCount;
            this.stdDev[i] = Math.sqrt(this.var[i]);
        }
    }
    
    /**
     * Get original data set.
     * 
     * @return Original dataset.
     */
    public DataSet getDataSet() {
        return this.dataSet;
    }

    /**
     * Get mean for each data set column.
     * 
     * @return Array of means by columns.
     */
    public double[] getMean() {
        return this.mean;
    }

    /**
     * Get maximum for each data set column.
     * 
     * @return Array of maximums by columns.
     */
    public double[] getMax() {
        return this.max;
    }

    /**
     * Get minimum for each data set column.
     * 
     * @return Array of minimums by columns. 
     */
    public double[] getMin() {
        return this.min;
    }

    /**
     * Get variant for each data set column.
     * 
     * @return Array of variants by columns. 
     */
    public double[] getVar() {
        return this.var;
    }

    /**
     * Get standard deviation for each data set column.
     * 
     * @return Array of standard deviations by columns.
     */
    public double[] getStdDev() {
        return this.stdDev;
    }

    /**
     * Get data set frequency for nominal columns.
     * Returns -0.0 for numeric columns.
     * 
     * @return Array of frequencies by columns.
     */
    public double[] getFrequency() {
        return this.frequency;
    }
}
