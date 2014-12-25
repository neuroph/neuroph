package org.neuroph.contrib.evaluation.evaluators;

import org.neuroph.core.learning.error.ErrorFunction;


public class ErrorEvaluator implements Evaluator<Double> {

    private ErrorFunction errorFunction; // this shoul dbe changed to real MSE not Neuroph


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

