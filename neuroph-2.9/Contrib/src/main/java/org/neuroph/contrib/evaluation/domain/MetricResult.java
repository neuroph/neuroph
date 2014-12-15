package org.neuroph.contrib.evaluation.domain;

import org.neuroph.contrib.utils.ArrayUtils;

import java.util.List;

/**
 * Container class for all metrics which use confusion matrix for their computation
 */
public class MetricResult {

    private double accuracy;

    private double error;

    private double precision;

    private double recall;

    private double fScore;

    public double getAccuracy() {
        return accuracy;
    }

    public double getError() {
        return error;
    }

    public double getPrecision() {
        return precision;
    }

    public double getRecall() {
        return recall;
    }

    public double getFScore() {
        return fScore;
    }

    @Override
    public String toString() {
        return "MetricResult{" +
                "accuracy=" + accuracy +
                ", error=" + error +
                ", precision=" + precision +
                ", recall=" + recall +
                ", fScore=" + fScore +
                '}';
    }

    /**
     * @param confusionMatrix confusion matrix computed on test set
     * @return result numeric scores calculated  using confusion matrix
     */
    public static MetricResult fromConfusionMatrix(ConfusionMatrix confusionMatrix) {
        MetricResult metricsEvaluationResult = new MetricResult();

        double[][] matrix = confusionMatrix.getConfusionMatrix();

        int totalElements = getDataSetSize(matrix);
        int totalCorrect = getCorrect(matrix);
        double[] precisions = createPrecisionForEachClass(confusionMatrix);
        double[] recalls = createRecallForEachClass(confusionMatrix);
        double[] fScores = createFScoresForEachClass(precisions, recalls);

        metricsEvaluationResult.accuracy = (double) totalCorrect / totalElements;
        metricsEvaluationResult.error = 1.0 - metricsEvaluationResult.accuracy;
        metricsEvaluationResult.precision = ArrayUtils.average(precisions);
        metricsEvaluationResult.recall = ArrayUtils.average(recalls);
        metricsEvaluationResult.fScore = ArrayUtils.average(fScores);

        return metricsEvaluationResult;
    }

    /**
     *
     * @param results list of different metric results computed on different sets of data
     * @return average metrics computed different MetricResults
     */
    public static MetricResult averageFromMultipleRuns(List<MetricResult> results) {
        double averageAccuracy = 0;
        double averageError = 0;
        double averagePrecision = 0;
        double averageRecall = 0;
        double averageFScore = 0;

        for (MetricResult metricResult : results) {
            averageAccuracy += metricResult.getAccuracy();
            averageError += metricResult.getError();
            averagePrecision += metricResult.getPrecision();
            averageRecall += metricResult.getRecall();
            averageFScore += metricResult.getFScore();
        }

        MetricResult averageMetricsResult = new MetricResult();

        averageMetricsResult.accuracy = averageAccuracy / results.size();
        averageMetricsResult.error = averageError / results.size();
        averageMetricsResult.precision = averagePrecision / results.size();
        averageMetricsResult.recall = averageRecall / results.size();
        averageMetricsResult.fScore = averageFScore / results.size();

        return averageMetricsResult;
    }

    /**
     *
     * @param results list of different metric results computed on different sets of data
     * @return maximum metrics computed different MetricResults
     */
    public static MetricResult maxFromMultipleRuns(List<MetricResult> results) {
        double maxAccuracy = 0;
        double maxError = 0;
        double maxPrecision = 0;
        double maxRecall = 0;
        double maxFScore = 0;

        for (MetricResult metricResult : results) {
            maxAccuracy = Math.max(maxAccuracy, metricResult.getAccuracy());
            maxError = Math.max(maxError, metricResult.getError());
            maxPrecision = Math.max(maxPrecision, metricResult.getPrecision());
            maxRecall = Math.max(maxRecall, metricResult.getRecall());
            maxFScore = Math.max(maxFScore, metricResult.getFScore());
        }

        MetricResult averageMetricsResult = new MetricResult();

        averageMetricsResult.accuracy = maxAccuracy;
        averageMetricsResult.error = maxError;
        averageMetricsResult.precision = maxPrecision;
        averageMetricsResult.recall = maxRecall;
        averageMetricsResult.fScore = maxFScore;

        return averageMetricsResult;
    }

    private static int getCorrect(double[][] matrix) {
        int totalCorrect = 0;
        for (int i = 0; i < matrix.length; i++) {
            totalCorrect += matrix[i][i];
        }
        return totalCorrect;
    }

    private static int getDataSetSize(double[][] matrix) {
        int totalElements = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                totalElements += matrix[i][j];
            }
        }
        return totalElements;
    }


    private static double[] createRecallForEachClass(ConfusionMatrix confusionMatrix) {
        double[][] matrix = confusionMatrix.getConfusionMatrix();
        double[] recall = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                recall[i] += matrix[i][j];
            }
            recall[i] = safelyDivide(matrix[i][i], recall[i]);
        }
        return recall;
    }

    private static double[] createPrecisionForEachClass(ConfusionMatrix confusionMatrix) {
        double[][] matrix = confusionMatrix.getConfusionMatrix();
        double[] precisions = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                precisions[i] += matrix[j][i];
            }
            precisions[i] = safelyDivide(matrix[i][i], precisions[i]);
        }
        return precisions;
    }

    private static double[] createFScoresForEachClass(double[] precisions, double[] recalls) {
        double[] fScores = new double[precisions.length];

        for (int i = 0; i < precisions.length; i++) {
            fScores[i] = 2 * (precisions[i] * recalls[i]) / (precisions[i] + recalls[i]);
        }

        return fScores;
    }


    private static double safelyDivide(double x, double y) {
        double divisor = x == 0.0 ? 1 : x;
        double divider = y == 0.0 ? 1.0 : y;
        return divisor / divider;
    }


}
