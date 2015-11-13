package org.neuroph.contrib.eval.classification;


import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.contrib.eval.EvaluationResult;

/**
 * Container class for all metrics which use confusion matrix for their computation 
 *
 * Based on:
 * http://java-ml.sourceforge.net/api/0.1.7/net/sf/javaml/classification/evaluation/PerformanceMeasure.html
 * http://sourceforge.net/p/java-ml/java-ml-code/ci/a25ddde7c3677da44e47a643f88e32e2c8bbc32f/tree/net/sf/javaml/classification/evaluation/PerformanceMeasure.java
 * 
 * http://en.wikipedia.org/wiki/Matthews_correlation_coefficient
 * 
 */
public class ClassificationMetrics {

    double falseNegative;
    double falsePositive;
    double trueNegative;
    double truePositive;
    double total;
    
    String classLabel;
    
    
   /**
    * Constructs a new measure using arguments
    * TODO: add class to which measure corresponds?
    * 
    * @param truePositive
    * @param trueNegative
    * @param falsePositive
    * @param falseNegative
    */
    public ClassificationMetrics(int truePositive, int trueNegative, int falsePositive, int falseNegative) {
        this.truePositive = truePositive;
        this.trueNegative = trueNegative;
        this.falsePositive = falsePositive;
        this.falseNegative = falseNegative;                        
        this.total = falseNegative + falsePositive + trueNegative + truePositive;
    }

    public String getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
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
        
        sb.append("Class: "+classLabel).append("\n");
        sb.append("Total items: ").append(getTotal()).append("\n");        
        sb.append("True positive:").append(truePositive).append("\n");
        sb.append("True negative:").append(trueNegative).append("\n");
        sb.append("False positive:").append(falsePositive).append("\n");   
        sb.append("False negative:").append(falseNegative).append("\n");        
        sb.append("Sensitivity or true positive rate (TPR): ").append(getSensitivity()).append("\n");
        sb.append("Specificity (SPC) or true negative rate (TNR): ").append(getSpecificity()).append("\n");
        sb.append("Fall-out or false positive rate (FPR): ").append(getFalsePositiveRate()).append("\n");
        sb.append("False negative rate (FNR): ").append(getFalseNegativeRate()).append("\n");        
        sb.append("Accuracy (ACC): ").append(getAccuracy()).append("\n");
        sb.append("Precision or positive predictive value (PPV): ").append(getPrecision()).append("\n");
        sb.append("Recall: ").append(getRecall()).append("\n");        
        sb.append("F-measure: ").append(getFMeasure()).append("\n");        
        sb.append("False discovery rate (FDR): ").append(getFalseDiscoveryRate()).append("\n");
        sb.append("Matthews correlation Coefficient (MCC): ").append(getMatthewsCorrelationCoefficient()).append("\n");
        return sb.toString();
    }    
    

    
    
    public static class Stats {
        public double accuracy=0;
        public double precision=0;
        public double recall=0;
        public double fScore=0;        
        public double mserror=0;    

        @Override
        public String toString() {
            return "Stats{" + "accuracy=" + accuracy + ", precision=" + precision + ", recall=" + recall + ", fScore=" + fScore + ", mserror=" + mserror + '}';
        }
        
        
    }
    

    public static ClassificationMetrics[] createFromMatrix(ConfusionMatrix confusionMatrix) {
        // Create Classification measure for each class 
        ClassificationMetrics[] measures = new ClassificationMetrics[confusionMatrix.getClassCount()];
        String[] classLabels = confusionMatrix.getClassLabels();
                
        for(int clsIdx=0; clsIdx<confusionMatrix.getClassCount(); clsIdx++) { // for each class
            // ove metode mozda ubaciti u matricu Confusion matrix - najbolje tako
            int tp = confusionMatrix.getTruePositive(clsIdx);
            int tn = confusionMatrix.getTrueNegative(clsIdx);
            int fp = confusionMatrix.getFalsePositive(clsIdx);
            int fn = confusionMatrix.getFalseNegative(clsIdx);                                   
            
            measures[clsIdx] = new ClassificationMetrics(tp, tn, fp, fn);         
            measures[clsIdx].setClassLabel(classLabels[clsIdx]);           
        }        
        
        return measures;
    }
    
    
    
    /**
     *
     * @param results list of different metric results computed on different sets of data
     * @return average metrics computed different MetricResults
     */
    public static ClassificationMetrics.Stats average(ClassificationMetrics[] results) {
        List<String> classLabels = new ArrayList<>();
         ClassificationMetrics.Stats average = new ClassificationMetrics.Stats();
          double count = 0;
            for (ClassificationMetrics cm : results) {
                average.accuracy += cm.getAccuracy();
                average.precision += cm.getPrecision();
                average.recall += cm.getRecall();
                average.fScore += cm.getFMeasure();
//                average.mserror += er.getMeanSquareError();
                
                if(!classLabels.contains(cm.getClassLabel()))
                    classLabels.add(cm.getClassLabel());
            }
            count++;
        
        count = count * classLabels.size(); // * classes count
        average.accuracy = average.accuracy / count;
        average.precision = average.precision / count;
        average.recall = average.recall / count;
        average.fScore = average.fScore / count;
        average.mserror = average.mserror / count;
        
        return average;
    }

    /**
     *
     * @param results list of different metric results computed on different sets of data
     * @return maximum metrics computed different MetricResults
     */
//    public static ClassificationMetrics maxFromMultipleRuns(List<ClassificationMetrics> results) {
//        double maxAccuracy = 0;
//        double maxError = 0;
//        double maxPrecision = 0;
//        double maxRecall = 0;
//        double maxFScore = 0;
//
//        for (ClassificationMetrics metricResult : results) {
//            maxAccuracy = Math.max(maxAccuracy, metricResult.getAccuracy());
//            maxError = Math.max(maxError, metricResult.getError());
//            maxPrecision = Math.max(maxPrecision, metricResult.getPrecision());
//            maxRecall = Math.max(maxRecall, metricResult.getRecall());
//            maxFScore = Math.max(maxFScore, metricResult.getFScore());
//        }
//
//        ClassificationMetrics averageMetricsResult = new ClassificationMetrics();
//
//        averageMetricsResult.accuracy = maxAccuracy;
//        averageMetricsResult.error = maxError;
//        averageMetricsResult.precision = maxPrecision;
//        averageMetricsResult.recall = maxRecall;
//        averageMetricsResult.fScore = maxFScore;
//
//        return averageMetricsResult;
//    }



//    private static double[] createFScoresForEachClass(double[] precisions, double[] recalls) {
//        double[] fScores = new double[precisions.length];
//
//        for (int i = 0; i < precisions.length; i++) {
//            fScores[i] = 2 * (precisions[i] * recalls[i]) / (precisions[i] + recalls[i]);
//        }
//
//        return fScores;
//    }


//    private static double safelyDivide(double x, double y) {
//        double divisor = x == 0.0 ? 1 : x;
//        double divider = y == 0.0 ? 1.0 : y;
//        return divisor / divider;
//    }


}