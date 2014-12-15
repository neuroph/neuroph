package org.neuroph.contrib.evaluation.evaluators;

import org.neuroph.core.learning.error.ErrorFunction;

/**
 * Calculates scalar result using ErrorFunction
 */
public class ErrorEvaluator implements NeurophEvaluator<Double> {

    private ErrorFunction errorFunction;


    public ErrorEvaluator(final ErrorFunction errorFunction) {
        this.errorFunction = errorFunction;
    }

    @Override
    public void processResult(double[] predictedOutput, double[] actualOutput) {
        errorFunction.calculatePatternError(predictedOutput, actualOutput);
    }

    @Override
    public Double getEvaluationResult() {
        return errorFunction.getTotalError();
    }
}

