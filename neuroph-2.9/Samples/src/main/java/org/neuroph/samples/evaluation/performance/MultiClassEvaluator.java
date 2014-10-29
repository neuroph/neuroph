package org.neuroph.samples.evaluation.performance;

import org.neuroph.samples.evaluation.BaseEvaluator;
import org.neuroph.samples.evaluation.domain.ClassificationOutput;
import org.neuroph.samples.evaluation.domain.ConfusionMatrix;
import org.neuroph.samples.evaluation.domain.MetricResult;

/**
 * @author Boris Fulurija
 */
public class MultiClassEvaluator implements BaseEvaluator<MetricResult> {

    private ConfusionMatrix confusionMatrix;
    private MetricResult cachedResults;

    //TODO DataSet.getLabels() should be implemented!!!!!
    public MultiClassEvaluator(String[] labels, int numberOfClasses) {
        confusionMatrix = new ConfusionMatrix.ConfusionMatrixBuilder()
                .withLabels(labels)
                .withClassNumber(numberOfClasses)
                .createConfusionMatrix();
    }

    @Override
    public void processResult(double[] predictedOutputs, double[] actualOutputs) {
        int actualClass = ClassificationOutput.getMaxOutput(actualOutputs).getClassId();
        int predictedClass = ClassificationOutput.getMaxOutput(predictedOutputs).getClassId();

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
