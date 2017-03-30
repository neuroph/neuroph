/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.util;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * This class represents statistics for data set.
 *
 * @author Arsenovic Aleksandar <salle18@gmail.com>
 */
public class DataSetStatistics {

    private final int inputSize;

    private final int outputSize;

    private final int rowCount;

    private final DataSetColumnType[] inputColumnType;

    private final DataSetColumnType[] outputColumnType;

    private final double[] mean;

    private final double[] max;

    private final double[] maxOut;

    private final double[] min;

    private final double[] minOut;

    private final double[] sum;

    private final double[] var;

    private final double[] stdDev;

    private final double[] frequencyIn;

    private final double[] frequencyOut;

    private final DataSet dataSet;

    public DataSetStatistics(DataSet dataSet) {
        this.dataSet = dataSet;
        this.inputSize = dataSet.getInputSize();
        this.outputSize = dataSet.getOutputSize();
        this.rowCount = dataSet.getRows().size();
        this.inputColumnType = new DataSetColumnType[this.inputSize];
        this.outputColumnType = new DataSetColumnType[this.outputSize];
        this.mean = new double[this.inputSize];
        this.max = new double[this.inputSize];
        this.maxOut = new double[this.outputSize];
        this.min = new double[this.inputSize];
        this.minOut = new double[this.outputSize];
        this.sum = new double[this.inputSize];
        this.var = new double[this.inputSize];
        this.stdDev = new double[this.inputSize];
        this.frequencyIn = new double[this.inputSize];
        this.frequencyOut = new double[this.outputSize];
        this.setDefaultValues();
    }

    private void setDefaultValues() {
        for (int i = 0; i < this.inputSize; i++) {
            this.max[i] = -Double.MAX_VALUE;
            this.min[i] = Double.MAX_VALUE;
            this.inputColumnType[i] = DataSetColumnType.NUMERIC;
        }
        for (int i = 0; i < this.outputSize; i++) {
            this.maxOut[i] = -Double.MAX_VALUE;
            this.minOut[i] = Double.MAX_VALUE;
            this.outputColumnType[i] = DataSetColumnType.NUMERIC;
        }
    }

    private void resetValues() {
        for (int i = 0; i < this.inputSize; i++) {
            this.sum[i] = 0;
            this.var[i] = 0;
            this.frequencyIn[i] = -1;
        }
        for (int i = 0; i < this.outputSize; i++) {
            this.frequencyOut[i] = -1;
        }
    }

    public void setInputColumnType(int index, DataSetColumnType columnType) {
        this.inputColumnType[index] = columnType;
    }

    public void setOutputColumnType(int index, DataSetColumnType columnType) {
        this.outputColumnType[index] = columnType;
    }

    public void calculateStatistics() {
        this.resetValues();
        for (DataSetRow dataSetRow : this.dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            double[] output = dataSetRow.getDesiredOutput();

            for (int i = 0; i < this.inputSize; i++) {
                this.max[i] = Math.max(this.max[i], input[i]);
                this.min[i] = Math.min(this.min[i], input[i]);
                this.sum[i] += input[i];
                if (this.inputColumnType[i] == DataSetColumnType.NOMINAL) {
                    this.frequencyIn[i] += input[i];
                }
            }

            for (int i = 0; i < this.outputSize; i++) {
                this.maxOut[i] = Math.max(this.maxOut[i], output[i]);
                this.minOut[i] = Math.min(this.minOut[i], output[i]);
                if (this.outputColumnType[i] == DataSetColumnType.NOMINAL) {
                    this.frequencyOut[i] += output[i];
                }
            }
        }

        for (int i = 0; i < this.inputSize; i++) {
            this.mean[i] = this.sum[i] / (double) this.rowCount;
            if (this.inputColumnType[i] == DataSetColumnType.NOMINAL) {
                this.frequencyIn[i] /= (double) this.rowCount;
            }
        }

        for (int i = 0; i < this.outputSize; i++) {
            if (this.outputColumnType[i] == DataSetColumnType.NOMINAL) {
                this.frequencyOut[i] /= (double) this.rowCount;
            }

        }

        for (DataSetRow dataSetRow : this.dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < this.inputSize; i++) {
                double delta = input[i] - this.mean[i];
                this.var[i] += delta * delta;
            }
        }

        for (int i = 0; i < this.inputSize; i++) {
            this.var[i] /= (double) this.rowCount;
            this.stdDev[i] = Math.sqrt(this.var[i]);
        }
    }

    public double[] getMean() {
        return this.mean;
    }

    public double[] getMax() {
        return this.max;
    }

    public double[] getMaxOut() {
        return this.maxOut;
    }

    public double[] getMin() {
        return this.min;
    }

    public double[] getMinOut() {
        return this.minOut;
    }

    public double[] getSum() {
        return this.sum;
    }

    public double[] getVar() {
        return this.var;
    }

    public double[] getStdDev() {
        return this.stdDev;
    }

    public double[] getFrequencyIn() {
        return this.frequencyIn;
    }

    public double[] getFrequencyOut() {
        return this.frequencyOut;
    }
}
