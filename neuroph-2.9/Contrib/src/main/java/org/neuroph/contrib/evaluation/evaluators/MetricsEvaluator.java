package org.neuroph.contrib.evaluation.evaluators;

import org.neuroph.core.data.DataSet;
import org.neuroph.contrib.evaluation.domain.ClassificationOutput;
import org.neuroph.contrib.evaluation.domain.ConfusionMatrix;
import org.neuroph.contrib.evaluation.domain.MetricResult;

public class MetricsEvaluator implements NeurophEvaluator<MetricResult> {

    ConfusionMatrix confusionMatrix;
    MetricResult cachedResults;

    private MetricsEvaluator(String[] labels, int classNumber) {
        confusionMatrix = new ConfusionMatrix.ConfusionMatrixBuilder()
                .withLabels(labels)
                .withClassNumber(classNumber)
                .createConfusionMatrix();
    }


    @Override
    public void processResult(double[] predictedOutput, double[] actualOutput) {

    }

    @Override
    public MetricResult getEvaluationResult() {
        if (cachedResults == null) {
            cachedResults = MetricResult.fromConfusionMatrix(confusionMatrix);
        }
        return cachedResults;
    }


    public static MetricsEvaluator createEvaluator(final DataSet dataSet) {
        if (dataSet.getOutputSize() == 1) {
            //TODO how can we handle different thresholds???
            return new BinaryClassEvaluator(0.5);
        } else {
            return new MultiClassEvaluator(dataSet);
        }
    }


    private static class BinaryClassEvaluator extends MetricsEvaluator {

        public static final String[] BINARY_CLASS_LABELS = new String[]{"Yes", "No"};
        public static final int BINARY_CLASSIFICATION = 2;
        public static final int TRUE = 0;
        public static final int FALSE = 1;

        private double threshold;

        private BinaryClassEvaluator(double threshold) {
            super(BINARY_CLASS_LABELS, BINARY_CLASSIFICATION);
            this.threshold = threshold;
        }

        @Override
        public void processResult(double[] predictedOutput, double[] actualOutput) {
            double actualOutputValue = actualOutput[0];
            double predictedOutputValue = predictedOutput[0];

            int actualClass = FALSE;
            int predictedClass = FALSE;

            if (actualOutputValue >= threshold) {
                actualClass = TRUE;
            }
            if (predictedOutputValue >= threshold) {
                predictedClass = TRUE;
            }


            confusionMatrix.incrementElement(actualClass, predictedClass);
        }

    }


    private static class MultiClassEvaluator extends MetricsEvaluator {

        private MultiClassEvaluator(DataSet dataSet) {
            super(dataSet.getColumnNames(), dataSet.getOutputSize());
        }

        @Override
        public void processResult(double[] predictedOutput, double[] actualOutput) {
            int actualClass = ClassificationOutput.getMaxOutput(actualOutput).getClassId();
            int predictedClass = ClassificationOutput.getMaxOutput(predictedOutput).getClassId();

            confusionMatrix.incrementElement(actualClass, predictedClass);
        }

    }

}
