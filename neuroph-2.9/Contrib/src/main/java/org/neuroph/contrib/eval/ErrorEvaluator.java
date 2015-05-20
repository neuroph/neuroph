package org.neuroph.contrib.eval;

import org.neuroph.core.learning.error.ErrorFunction;

/**
 * Calculates scalar result using ErrorFunction
 */
public class ErrorEvaluator implements Evaluator<Double> {

    private ErrorFunction errorFunction;

    public ErrorEvaluator(final ErrorFunction errorFunction) {
        this.errorFunction = errorFunction;
    }

    @Override
    public void processNetworkResult(double[] networkOutput, double[] desiredOutput) {
        errorFunction.calculatePatternError(networkOutput, desiredOutput);
    }

    @Override
    public Double getResult() {
        return errorFunction.getTotalError();
    }

    @Override
    public void reset() {
        errorFunction.reset();
    }
}

