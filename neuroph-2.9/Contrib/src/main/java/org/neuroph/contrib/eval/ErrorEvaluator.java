package org.neuroph.contrib.eval;

import org.neuroph.core.learning.error.ErrorFunction;

/**
 * Calculates scalar evaluation result using ErrorFunction
 */
public class ErrorEvaluator implements Evaluator<Double> {

    private final ErrorFunction errorFunction;

    public ErrorEvaluator(final ErrorFunction errorFunction) {
        this.errorFunction = errorFunction;
    }

    @Override
    public void processNetworkResult(final double[] networkOutput, final double[] desiredOutput) {
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

