/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.util;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author salle18
 */
public class DataSetStatisticsTest extends TestCase {

    private double[] roundVector(double[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.round(input[i] * 100.0) / 100.0;
        }
        return output;
    }

    @Test
    public void testStatistics() {
        DataSet dataSet = DataSet.createFromFile("src/test/resources/iris.txt", 4, 3, ",");
        DataSetStatistics statistics = new DataSetStatistics(dataSet);

        statistics.setColumnType(4, DataSetColumnType.NOMINAL);
        statistics.setColumnType(5, DataSetColumnType.NOMINAL);
        statistics.setColumnType(6, DataSetColumnType.NOMINAL);
        statistics.calculateStatistics();
        double[] min = this.roundVector(statistics.getMin());
        double[] max = this.roundVector(statistics.getMax());
        double[] mean = this.roundVector(statistics.getMean());
        double[] sum = this.roundVector(statistics.getSum());
        double[] var = this.roundVector(statistics.getVar());
        double[] stdDev = this.roundVector(statistics.getStdDev());
        double[] frequency = this.roundVector(statistics.getFrequency());
        Assert.assertTrue(Arrays.equals(min, new double[]{4.3, 2.0, 1.0, 0.1, 0, 0, 0}));
        Assert.assertTrue(Arrays.equals(max, new double[]{7.9, 4.4, 6.9, 2.5, 1, 1, 1}));
        Assert.assertTrue(Arrays.equals(mean, new double[]{5.84, 3.05, 3.76, 1.20, 0.33, 0.33, 0.33}));
        Assert.assertTrue(Arrays.equals(sum, new double[]{876.5, 458.1, 563.8, 179.8, 50, 50, 50}));
        Assert.assertTrue(Arrays.equals(var, new double[]{0.68, 0.19, 3.09, 0.58, 0.22, 0.22, 0.22}));
        Assert.assertTrue(Arrays.equals(stdDev, new double[]{0.83, 0.43, 1.76, 0.76, 0.47, 0.47, 0.47}));
        Assert.assertTrue(Arrays.equals(frequency, new double[]{-1, -1, -1, -1, 0.33, 0.33, 0.33}));
    }

    @Test
    public void testNormalizedStatistics() {
        DataSet dataSet = DataSet.createFromFile("src/test/resources/iris_normalized.txt", 4, 3, ",");
        DataSetStatistics statistics = new DataSetStatistics(dataSet);

        statistics.setColumnType(4, DataSetColumnType.NOMINAL);
        statistics.setColumnType(5, DataSetColumnType.NOMINAL);
        statistics.setColumnType(6, DataSetColumnType.NOMINAL);
        statistics.calculateStatistics();
        double[] min = this.roundVector(statistics.getMin());
        double[] max = this.roundVector(statistics.getMax());
        double[] mean = this.roundVector(statistics.getMean());
        double[] sum = this.roundVector(statistics.getSum());
        double[] var = this.roundVector(statistics.getVar());
        double[] stdDev = this.roundVector(statistics.getStdDev());
        double[] frequency = this.roundVector(statistics.getFrequency());
        Assert.assertTrue(Arrays.equals(min, new double[]{0.54, 0.45, 0.14, 0.04, 0, 0, 0}));
        Assert.assertTrue(Arrays.equals(max, new double[]{1, 1, 1, 1, 1, 1, 1}));
        Assert.assertTrue(Arrays.equals(mean, new double[]{0.74, 0.69, 0.54, 0.48, 0.33, 0.33, 0.33}));
        Assert.assertTrue(Arrays.equals(sum, new double[]{110.95, 104.11, 81.71, 71.92, 50, 50, 50}));
        Assert.assertTrue(Arrays.equals(var, new double[]{0.01, 0.01, 0.06, 0.09, 0.22, 0.22, 0.22}));
        Assert.assertTrue(Arrays.equals(stdDev, new double[]{0.10, 0.10, 0.25, 0.30, 0.47, 0.47, 0.47}));
        Assert.assertTrue(Arrays.equals(frequency, new double[]{-1, -1, -1, -1, 0.33, 0.33, 0.33}));
    }

}
