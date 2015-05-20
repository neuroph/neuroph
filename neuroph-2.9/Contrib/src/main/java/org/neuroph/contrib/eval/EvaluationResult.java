package org.neuroph.contrib.eval;

import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.data.DataSet;

/**
 * Create class that will hold statistics for all evaluated datasets - avgs, mx, min, std, variation
 * @author zoran
 */
public class EvaluationResult {
    // for now this aggregates hardcoded results from all evaluators
    ConfusionMatrix confusionMatrix;
    double meanSquareError ;
    DataSet dataSet;
    // include neural net and data set?

    public ConfusionMatrix getConfusionMatrix() {
        return confusionMatrix;
    }

    public void setConfusionMatrix(ConfusionMatrix confusionMatrix) {
        this.confusionMatrix = confusionMatrix;
    }
    
    public ClassificationMetrics[] getClassificationMetricses() {
        return ClassificationMetrics.createFromMatrix(confusionMatrix);
    }    

    public double getMeanSquareError() {
        return meanSquareError;
    }

    public void setMeanSquareError(double meanSquareError) {
        this.meanSquareError = meanSquareError;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public String toString() {
        //-- also display getClassificationMetricses here
          ClassificationMetrics[] cms = getClassificationMetricses();
          StringBuilder sb = new StringBuilder();
          for(ClassificationMetrics c : cms ) {
              sb.append(c).append("\r\n");
          }
          
        return "EvaluationResult{" + "dataSet=" + dataSet.getLabel() + ", meanSquareError=" + meanSquareError + ", \r\n confusionMatrix=\r\n" + confusionMatrix +"\r\n" +sb.toString() +"}\r\n";
    }
                                    
}
