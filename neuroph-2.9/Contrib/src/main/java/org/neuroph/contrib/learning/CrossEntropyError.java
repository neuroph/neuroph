package org.neuroph.contrib.learning;


import org.neuroph.core.learning.error.ErrorFunction;

import java.io.Serializable;

/**
 * Special error function which is recommended to be used in classification models
 */
public class CrossEntropyError implements ErrorFunction, Serializable {

    private double[] errorDerivative;
    private transient double totalError;
    private transient double n;

    @Override
    public double getTotalError() {
        return -totalError / n ;
    }

    @Override
    public void reset() {
        totalError = 0;
        n = 0;
    }

    @Override
    public double[] calculatePatternError(double[] predictedOutput, double[] targetOutput) {
        double[] error = new double[targetOutput.length];

        if (predictedOutput.length != targetOutput.length)
            throw new IllegalArgumentException("Output array length and desired output array length must be the same size!");

        for (int i = 0; i < predictedOutput.length; i++) {
            errorDerivative[i] =  targetOutput[i] - predictedOutput[i];
            totalError += targetOutput[i] * Math.log(predictedOutput[i]) ;

        }
        n++;
        
        return error;
    }

}
