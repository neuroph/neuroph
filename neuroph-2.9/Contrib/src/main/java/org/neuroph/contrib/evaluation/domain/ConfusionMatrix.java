package org.neuroph.contrib.evaluation.domain;


public class ConfusionMatrix {


    private String[] labels;
    private double[][] confusionMatrix;


    ConfusionMatrix(String[] labels, double[][] confusionMatrix) {
        this.labels = labels;
        this.confusionMatrix = confusionMatrix;
    }


    public double[][] getConfusionMatrix() {
        return confusionMatrix;
    }



    public void incrementElement(int actual, int predicted) {
        confusionMatrix[actual][predicted]++;
    }

    @Override
    public String toString() {


        StringBuilder builder = new StringBuilder();

        int max = 5;
        for (String label : labels)
            max = Math.max(max, label.length());

        builder.append(String.format("%1$" + max + "s", ""));
        for (String label : labels)
            builder.append(String.format("%1$" + max + "s", label));
        builder.append("\n");

        for (int i = 0; i < confusionMatrix.length; i++) {
            builder.append(String.format("%1$" + max + "s", labels[i]));
            for (int j = 0; j < confusionMatrix[0].length; j++) {
                builder.append(String.format("%1$" + max + "s", confusionMatrix[i][j]));
            }
            builder.append("\n");

        }


        return builder.toString();
    }


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
