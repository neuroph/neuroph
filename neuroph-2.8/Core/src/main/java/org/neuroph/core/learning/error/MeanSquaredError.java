package org.neuroph.core.learning.error;

import java.io.Serializable;

/**
 *
 * @author zoran
 */
public class MeanSquaredError implements ErrorFunction, Serializable {
    private transient double totalSquaredErrorSum;
    private transient double n;

    public MeanSquaredError(double n) {
        this.n = n;
    }
    
    public void reset() {
        totalSquaredErrorSum = 0;
    }
    
    
    @Override
    public double getTotalError() {
        return totalSquaredErrorSum/n;
    }

    @Override
    public void addOutputError(double[] outputError) {
        double outputErrorSqrSum = 0;
        for (double error : outputError) {
            outputErrorSqrSum += (error * error) * 0.5; // a;so multiply with 1/trainingSetSize  1/2n * (...)
        }

        this.totalSquaredErrorSum += outputErrorSqrSum;
    }
    
}
