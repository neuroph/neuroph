package org.neuroph.contrib.model.evaluation.domain;

/**
 * Confusion matrix container
 */
public class ConfusionMatrix {

    public static final int STRING_DEFAULT_WIDTH = 5;
    private String[] labels;
    private double[][] confusionMatrix;

    ConfusionMatrix(String[] labels, double[][] confusionMatrix) {
        this.labels = labels;
        this.confusionMatrix = confusionMatrix;
    }

    /**
     *
     * @return confusion matrix as double array
     */
    public double[][] getConfusionMatrix() {
        return confusionMatrix;
    }

    /**
     *
     * @param actual class id of correct classification
     * @param predicted class id of predicted classification
     */
    public void incrementElement(int actual, int predicted) {
        confusionMatrix[actual][predicted]++;
    }

    @Override
    public String toString() {
        return prettyPrintConfusionMatrix();
    }

    private String prettyPrintConfusionMatrix() {
        StringBuilder builder = new StringBuilder();

        int maxColumnLenght = STRING_DEFAULT_WIDTH;
        for (String label : labels)
            maxColumnLenght = Math.max(maxColumnLenght, label.length());

        builder.append(String.format("%1$" + maxColumnLenght + "s", ""));
        for (String label : labels)
            builder.append(String.format("%1$" + maxColumnLenght + "s", label));
        builder.append("\n");

        for (int i = 0; i < confusionMatrix.length; i++) {
            builder.append(String.format("%1$" + maxColumnLenght + "s", labels[i]));
            for (int j = 0; j < confusionMatrix[0].length; j++) {
                builder.append(String.format("%1$" + maxColumnLenght + "s", confusionMatrix[i][j]));
            }
            builder.append("\n");

        }
        return builder.toString();
    }

    /**
     * Builder class used to create ConfusionMatrix objects
     */
    public static class ConfusionMatrixBuilder {
        private String[] labels;
        private double[][] confusionMatrix;

        public ConfusionMatrixBuilder withLabels(String[] labels) {
            this.labels = labels;
            return this;
        }

        public ConfusionMatrixBuilder withClassNumber(int classNumber) {
            this.confusionMatrix = new double[classNumber][classNumber];
            return this;
        }

        public ConfusionMatrix createConfusionMatrix() {
            if (confusionMatrix == null)
                confusionMatrix = new double[2][2];

            return new ConfusionMatrix(labels, confusionMatrix);
        }
    }


}
