package org.neuroph.contrib.evaluation.evaluators;

import org.neuroph.contrib.evaluation.domain.ErrorResult;


public class MeanSquareErrorEvaluator implements NeurophEvaluator<ErrorResult> {

    private double totalError;
    private double n;

    @Override
    public void processResult(double[] predictedOutput, double[] actualOutput) {

        for (int i = 0; i < actualOutput.length; i++) {
            totalError += (actualOutput[i] - predictedOutput[i]) * (actualOutput[i] - predictedOutput[i]);
        }
        n++;
    }

    @Override
    public ErrorResult getEvaluationResult() {
        double averageError = totalError / n;
        return new ErrorResult.ErrorMeasureResultBuilder()
                .withError(averageError)
                .createErrorMeasureResult();
    }
}
