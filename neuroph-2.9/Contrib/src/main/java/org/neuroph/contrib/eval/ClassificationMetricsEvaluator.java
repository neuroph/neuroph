package org.neuroph.contrib.eval;

import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.Utils;
import org.neuroph.core.data.DataSet;

public abstract class ClassificationMetricsEvaluator implements Evaluator<ClassificationMetrics[]> {

    ConfusionMatrix confusionMatrix;

    private ClassificationMetricsEvaluator(String[] labels, int classNumber) {
        confusionMatrix = new ConfusionMatrix(labels, classNumber);
    }

    @Override
    public ClassificationMetrics[] getResult() {
        return  ClassificationMetrics.createFromMatrix(confusionMatrix);
    }

    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }
    
    

    public static ClassificationMetricsEvaluator createForDataSet(final DataSet dataSet) {
        if (dataSet.getOutputSize() == 1) {
            //TODO how can we handle different thresholds??? - use thresholds for both binary and multiclass
            return new BinaryClassEvaluator(0.5);
        } else {
            return new MultiClassEvaluator(dataSet);
        }
    }


    /**
     * Binary evaluator used for computation of metrics in case when data has only one output result (one output neuron)
     */
    public static class BinaryClassEvaluator extends ClassificationMetricsEvaluator {

        public static final String[] BINARY_CLASS_LABELS = new String[]{"False", "True"};
        public static final int BINARY_CLASSIFICATION = 2;
        public static final int TRUE = 1;
        public static final int FALSE = 0;

        private double threshold;

        private BinaryClassEvaluator(double threshold) {
            super(BINARY_CLASS_LABELS, BINARY_CLASSIFICATION);
            this.threshold = threshold;
        }

        @Override
        public void processNetworkResult(double[] networkOutput, double[] desiredOutput) {
            int actualClass = classForValueOf(desiredOutput[0]);
            int predictedClass = classForValueOf(networkOutput[0]);

            confusionMatrix.incrementElement(actualClass, predictedClass);
        }

        private int classForValueOf(double classResult) {
            int classValue = FALSE;
            if (classResult >= threshold) {
                classValue = TRUE;
            }
            return classValue;
        }

    }

    /**
     * Evaluator used for computation of metrics in case when data has
     * multiple classes - one vs many classification
     */
    public static class MultiClassEvaluator extends ClassificationMetricsEvaluator {

        private MultiClassEvaluator(DataSet dataSet) {            
            super(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"} , dataSet.getOutputSize());
            // dataSet.getColumnNames()
        }

        @Override
        public void processNetworkResult(double[] predictedOutput, double[] actualOutput) {
            // just get max index
            int actualClassIdx = Utils.maxIdx(actualOutput);
            int predictedClassIdx = Utils.maxIdx(predictedOutput);

            confusionMatrix.incrementElement(actualClassIdx, predictedClassIdx);
        }
    }

}
