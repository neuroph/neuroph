package org.neuroph.contrib.eval.classification;

import static java.lang.Math.sqrt;

/**
 * 
 * 
 * Based on:
 * http://java-ml.sourceforge.net/api/0.1.7/net/sf/javaml/classification/evaluation/PerformanceMeasure.html
 * http://sourceforge.net/p/java-ml/java-ml-code/ci/a25ddde7c3677da44e47a643f88e32e2c8bbc32f/tree/net/sf/javaml/classification/evaluation/PerformanceMeasure.java
 * 
 * 
 * http://en.wikipedia.org/wiki/Matthews_correlation_coefficient
 * Terminology and derivations from a confusion matrix
 * 
 * @author Zoran Sevarac
 * @author Nemanja Drobnjak
 * @author Nina Desnica
 * 
 * 
 */
public class ClassificationMeasure {

    double falseNegative;
    double falsePositive;
    double trueNegative;
    double truePositive;
    double total;


   /**
    * Constructs a new measure using arguments
    * TODO: add class to which measure corresponds?
    * 
    * @param truePositive
    * @param trueNegative
    * @param falsePositive
    * @param falseNegative
    */
    public ClassificationMeasure(int truePositive, int trueNegative, int falsePositive, int falseNegative) {
        this.truePositive = truePositive;
        this.trueNegative = trueNegative;
        this.falsePositive = falsePositive;
        this.falseNegative = falseNegative;                        
        this.total = falseNegative + falsePositive + trueNegative + truePositive;
    }

    /**
     * Calculate and return classification accuracy measure
     * @return 
     */
    public double getAccuracy() {
        return (truePositive + trueNegative) / total;
    }
    
    public double getPrecision() {
        return truePositive / (double) (truePositive + falsePositive);
    }    

    public double getRecall() {
        return this.truePositive / (double)(this.truePositive + this.falseNegative);
    }

    /**
     * Calculate and return classification true positive rate, recall, sensitivity
     * @return 
     */
    public double getSensitivity() {
        return truePositive / (truePositive + falseNegative);
    }

    //Specifity

    public double getSpecificity() {
        return trueNegative / (trueNegative + falsePositive);
    }

    public double getFalsePositiveRate() {
        return falsePositive / (falsePositive + trueNegative);
    }

    //False negative rate,

    public double getFalseNegativeRate() {
        return falseNegative / (falseNegative + truePositive);
    }

    public double getErrorRate() {
        return (this.falsePositive + this.falseNegative) / total;
    }   
    
    //Total
    public double getTotal() {
        return total;
    }


    public double getFalseDiscoveryRate() {
        return falsePositive / (truePositive + falsePositive);
    }

    // http://en.wikipedia.org/wiki/Matthews_correlation_coefficient
    //  measure of the quality of binary (two-class) classifications. It takes into account true and false positives and negatives and is generally regarded as a balanced measure which can be used even if the classes are of very different sizes.     
    public double getMatthewsCorrelationCoefficient() {
        return (truePositive * trueNegative - falsePositive * falseNegative) /
                (sqrt((truePositive + falsePositive) * (truePositive + falseNegative) * (trueNegative + falsePositive) * (trueNegative + falseNegative)));
    }
    
   
    
   /**
     * Calculates F-score for beta equal to 1.
     * 
     * @return f-score
     */
    public double getFMeasure() {
        return getFMeasure(1);
    }

    /**
     * Returns the F-score. When recall and precision are zero, this method will
     * return 0.
     * 
     * @param beta
     * @return f-score
     */
    public double getFMeasure(int beta) {
        double f = ((beta * beta + 1) * getPrecision() * getRecall())
                / (double)(beta * beta * getPrecision() + getRecall());
        if (Double.isNaN(f))
            return 0;
        else
            return f;
    }

    public double getQ9() {
        if (truePositive + falseNegative == 0) {
            return (trueNegative - falsePositive) / (trueNegative + falsePositive);
        } else if (trueNegative + falsePositive == 0) {
            return (truePositive - falseNegative) / (truePositive + falseNegative);
        } else
            return 1 - Math.sqrt(2)
                    * Math.sqrt(Math.pow(falseNegative / (truePositive + falseNegative), 2)
                    + Math.pow(falsePositive / (trueNegative + falsePositive), 2));
    }
    

    public double getBalancedClassificationRate() {
        if (trueNegative == 0 && falsePositive == 0)
            return truePositive / (truePositive + falseNegative);
        if (truePositive == 0 && falseNegative == 0)
            return trueNegative / (trueNegative + falsePositive);

        return 0.5 * (truePositive / (truePositive + falseNegative) + trueNegative / (trueNegative + falsePositive));
    }    
    

    

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("True positive:").append(truePositive).append("\n");
        sb.append("True negative:").append(trueNegative).append("\n");
        sb.append("False positive:").append(falsePositive).append("\n");   
        sb.append("False negative:").append(falseNegative).append("\n");        
        sb.append("Sensitivity or true positive rate (TPR): ").append(getSensitivity()).append("\n");
        sb.append("Specificity (SPC) or true negative rate (TNR): ").append(getSpecificity()).append("\n");
        sb.append("Fall-out or false positive rate (FPR): ").append(getFalsePositiveRate()).append("\n");
        sb.append("False negative rate (FNR): ").append(getFalseNegativeRate()).append("\n");
        sb.append("Total items: ").append(getTotal()).append("\n");
        sb.append("Accuracy (ACC): ").append(getAccuracy()).append("\n");
        sb.append("Precision or positive predictive value (PPV): ").append(getPrecision()).append("\n");
        sb.append("Recall: ").append(getRecall()).append("\n");        
        sb.append("F-measure: ").append(getFMeasure()).append("\n");        
        sb.append("False discovery rate (FDR): ").append(getFalseDiscoveryRate()).append("\n");
        sb.append("Matthews correlation Coefficient (MCC): ").append(getMatthewsCorrelationCoefficient()).append("\n");
        return sb.toString();

    }

}
