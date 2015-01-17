package org.neuroph.contrib.eval.classification;

/**
 * Confusion matrix container, holds class labels and matrix values .
 */
public class ConfusionMatrix {

    /**
     * Class labels
     */
    private String[] labels;
    
    /**
     * Values of confusion matrix
     */    
    private double[][] values;
    
    /**
     * Default setting for formating toString
     */
    private  static final int STRING_DEFAULT_WIDTH = 5;    
    
    /**
     * Creates new confusion matrix with specified labels and number of classes
     * @param labels
     * @param classNumber 
     */
    public ConfusionMatrix(String[] labels, int classNumber) {
        this.labels = labels;        
        this.values = new double[classNumber][classNumber];
    }

    /**
     * Returns confusion matrix values as double array
     * @return confusion matrix values as double array
     */
    public double[][] getValues() {
        return values;
    }
    
    /**
     * Returns value of confusion matrix at specified position 
     * @param actual actual idx position
     * @param predicted predicted idx position
     * @return value of confusion matrix at specified position 
     */
    public double getValueAt(int actual, int predicted) {
       return values[actual][predicted]; 
    }

    /**
     * Increments matrix value at specified position
     * @param actual class id of correct classification
     * @param predicted class id of predicted classification
     */
    public void incrementElement(int actual, int predicted) {
        values[actual][predicted]++;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        int maxColumnLenght = STRING_DEFAULT_WIDTH;
        for (String label : labels)
            maxColumnLenght = Math.max(maxColumnLenght, label.length());

        builder.append(String.format("%1$" + maxColumnLenght + "s", ""));
        for (String label : labels)
            builder.append(String.format("%1$" + maxColumnLenght + "s", label));
        builder.append("\n");

        for (int i = 0; i < values.length; i++) {
            builder.append(String.format("%1$" + maxColumnLenght + "s", labels[i]));
            for (int j = 0; j < values[0].length; j++) {
                builder.append(String.format("%1$" + maxColumnLenght + "s", values[i][j]));
            }
            builder.append("\n");

        }
        return builder.toString();
    }
    
}
