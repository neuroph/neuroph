package org.neuroph.contrib.eval.classification;

/**
 * Confusion matrix container, holds class labels and matrix values .
 */
public class ConfusionMatrix {

    /**
     * Class labels
     */
    private String[] classLabels;
    
    /**
     * Values of confusion matrix
     */    
    private double[][] values;
    
    /**
     * Number of classes
     */
    private int classCount;
    
    private int total = 0;
    
    
    /**
     * Default setting for formating toString
     */
    private  static final int STRING_DEFAULT_WIDTH = 7;    
    
    /**
     * Creates new confusion matrix with specified class labels and number of classes
     * @param labels
     * @param classCount 
     */
    public ConfusionMatrix(String[] classLabels) {
        this.classLabels = classLabels;     
        this.classCount = classLabels.length;
        this.values = new double[classCount][classCount];
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
        total++;
    }
    
    int getClassCount() {
        return classCount;
    }    

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        int maxColumnLenght = STRING_DEFAULT_WIDTH;
        for (String label : classLabels)
            maxColumnLenght = Math.max(maxColumnLenght, label.length());

        builder.append(String.format("%1$" + maxColumnLenght + "s", ""));
        for (String label : classLabels)
            builder.append(String.format("%1$" + maxColumnLenght + "s", label));
        builder.append("\n");

        for (int i = 0; i < values.length; i++) {
            builder.append(String.format("%1$" + maxColumnLenght + "s", classLabels[i]));
            for (int j = 0; j < values[0].length; j++) {
                builder.append(String.format("%1$" + maxColumnLenght + "s", values[i][j]));
            }
            builder.append("\n");

        }
        return builder.toString();
    }

    public int getTruePositive(int clsIdx) {
        return (int)values[clsIdx][clsIdx];
    }
    
    public int getTrueNegative(int clsIdx) {
        int trueNegative = 0;
        
        for(int i = 0; i < classCount; i++) {
            if (i == clsIdx) continue; 
            for(int j = 0; j < classCount; j++) {
                if (j == clsIdx) continue; 
                trueNegative += values[i][j];
            }
        }
        
        return trueNegative;
    }    

    public int getFalsePositive(int clsIdx) {
        int falsePositive = 0;
        
        for(int i=0; i<classCount; i++) {
            if (i == clsIdx) continue; 
            falsePositive += values[i][clsIdx];
        }
        
        return falsePositive;
    }

    public int getFalseNegative(int clsIdx) {
        int falseNegative = 0;
        
        for(int i=0; i<classCount; i++) {
            if (i == clsIdx) continue; 
            falseNegative += values[clsIdx][i];
        }
        
        return falseNegative;
    }

    public String[] getClassLabels() {
        return classLabels;
    }

    public int getTotal() {
        return total;
    }
    
    
    

    
}
