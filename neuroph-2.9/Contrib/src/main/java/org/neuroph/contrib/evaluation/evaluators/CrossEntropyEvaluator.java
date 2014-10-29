package org.neuroph.contrib.evaluation.evaluators;

import org.neuroph.contrib.evaluation.domain.ErrorResult;


public class CrossEntropyEvaluator implements NeurophEvaluator<ErrorResult> {

    private double cumulativeError;
    private double n;


    @Override
    public void processResult(double[] predictedOutput, double[] actualOutput) {

        for (int i = 0; i < actualOutput.length; i++) {
            cumulativeError += actualOutput[i] * Math.log(predictedOutput[i]);
        }
        n++;
    }

    @Override
    public ErrorResult getEvaluationResult() {
        double averageError = -cumulativeError / n;
        return new ErrorResult.ErrorMeasureResultBuilder()
                .withError(averageError)
                .createErrorMeasureResult();
    }
}

