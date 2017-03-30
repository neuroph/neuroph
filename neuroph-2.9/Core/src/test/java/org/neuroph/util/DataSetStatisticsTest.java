/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.util;

import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author salle18
 */
public class DataSetStatisticsTest extends TestCase {

    DataSetStatistics statistics;

    private double[] roundVector(double[] input) {
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.round(input[i] * 100.0) / 100.0;
        }
        return output;
    }

    @Before
    public void setUp() {
        DataSet dataSet = DataSet.createFromFile("src/test/resources/iris.txt", 4, 3, ",");
        this.statistics = new DataSetStatistics(dataSet);
    }

    @Test
    public void testStatistics() {
        this.statistics.setOutputColumnType(0, DataSetColumnType.NOMINAL);
        this.statistics.setOutputColumnType(1, DataSetColumnType.NOMINAL);
        this.statistics.setOutputColumnType(2, DataSetColumnType.NOMINAL);
        this.statistics.calculateStatistics();
        double[] min = this.roundVector(statistics.getMin());
        double[] max = this.roundVector(statistics.getMax());
        double[] mean = this.roundVector(statistics.getMean());
        double[] sum = this.roundVector(statistics.getSum());
        double[] var = this.roundVector(statistics.getVar());
        double[] stdDev = this.roundVector(statistics.getStdDev());
        double[] minOut = this.roundVector(statistics.getMinOut());
        double[] maxOut = this.roundVector(statistics.getMaxOut());
        double[] frequencyIn = this.roundVector(statistics.getFrequencyIn());
        double[] frequencyOut = this.roundVector(statistics.getFrequencyOut());
        Assert.assertTrue(Arrays.equals(min, new double[]{4.3, 2.0, 1.0, 0.1}));
        Assert.assertTrue(Arrays.equals(max, new double[]{7.9, 4.4, 6.9, 2.5}));
        Assert.assertTrue(Arrays.equals(mean, new double[]{5.84, 3.05, 3.76, 1.20}));
        Assert.assertTrue(Arrays.equals(mean, new double[]{5.84, 3.05, 3.76, 1.20}));
        Assert.assertTrue(Arrays.equals(sum, new double[]{876.5, 458.1, 563.8, 179.8}));
        Assert.assertTrue(Arrays.equals(var, new double[]{0.68, 0.19, 3.09, 0.58}));
        Assert.assertTrue(Arrays.equals(stdDev, new double[]{0.83, 0.43, 1.76, 0.76}));
        Assert.assertTrue(Arrays.equals(minOut, new double[]{0, 0, 0}));
        Assert.assertTrue(Arrays.equals(maxOut, new double[]{1, 1, 1}));
        Assert.assertTrue(Arrays.equals(frequencyIn, new double[]{-1, -1, -1, -1}));
        Assert.assertTrue(Arrays.equals(frequencyOut, new double[]{0.33, 0.33, 0.33}));
    }

}
