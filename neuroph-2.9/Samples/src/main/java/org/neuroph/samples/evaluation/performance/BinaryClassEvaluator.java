package org.neuroph.samples.evaluation.performance;

import org.neuroph.samples.evaluation.BaseEvaluator;
import org.neuroph.samples.evaluation.domain.ConfusionMatrix;
import org.neuroph.samples.evaluation.domain.MetricResult;


public class BinaryClassEvaluator implements BaseEvaluator<MetricResult> {

    public static final String[] BINARY_CLASS_LABELS = new String[]{"Yes", "No"};
    public static final int BINARY_CLASSIFICATION = 2;
    public static final int TRUE = 0;
    public static final int FALSE = 1;


    private ConfusionMatrix confusionMatrix;
    private MetricResult cachedResults;
    private double threshold;

    public BinaryClassEvaluator(double threshold) {
        this.threshold = threshold;
        confusionMatrix = new ConfusionMatrix.ConfusionMatrixBuilder()
                .withLabels(BINARY_CLASS_LABELS)
                .withClassNumber(BINARY_CLASSIFICATION)
                .createConfusionMatrix();
    }

    @Override
    public void processResult(double[] predictedOutputs, double[] actualOutputs) {
        double actualOutput = actualOutputs[0];
        double predictedOutput = predictedOutputs[0];

        int actualClass = FALSE;
        int predictedClass = FALSE;

        if (actualOutput >= threshold) {
            actualClass = TRUE;
        }
        if (predictedOutput >= threshold) {
            predictedClass = TRUE;
        }


        confusionMatrix.incrementElement(actualClass, predictedClass);
    }

    @Override
    public MetricResult getEvaluationResult() {
        if (cachedResults == null) {
            cachedResults = MetricResult.fromConfusionMatrix(confusionMatrix);
        }
        return cachedResults;
    }

}
