package org.neuroph.samples.evaluation.domain;

import org.neuroph.samples.evaluation.ArrayUtils;

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

    public double getfScore() {
        return fScore;
    }


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
            double divisor = matrix[i][i] == 0.0 ? 1.0 : matrix[i][i];
            double divider =  recall[i] == 0.0 ? 1.0 : recall[i];

            recall[i] = divisor / divider;
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
            double divisor = matrix[i][i] == 0.0 ? 1 : matrix[i][i];
            double divider =  precisions[i] == 0.0 ? 1.0 : precisions[i];
            precisions[i] =  divisor / divider;
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


}
