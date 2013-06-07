package org.neuroph.data.norm;

import org.neuroph.data.DataSet;
import org.neuroph.data.DataSetRow;

/**
 * This one does normalization for outputs too
 * @author zoran
 */
public class RangeNormalizer implements Normalizer {
    private double low, high;
    double[] max; // contains max values for all columns
    double[] min; // contains min values for all columns    

    public RangeNormalizer(double low, double high) {
        this.low= low;
        this.high = high;        
    }  
    
    @Override
    public void normalize(DataSet dataSet) {
        findMaxinAndMinVectors(dataSet);
       
        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = normalizeToRange(row.getInput());
            double[] normalizedOutput = normalizeToRange(row.getDesiredOutput());
                        
            row.setInput(normalizedInput);
            row.setDesiredOutput(normalizedOutput);
            
        }
        
    }

    
    private double[] normalizeToRange(double[] vector) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = ((vector[i] - min[i]) / (max[i] - min[i])) * (high - low) + low ;
        }

        return normalizedVector;             
    }
    
    private void findMaxinAndMinVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        max = new double[inputSize];
        min = new double[inputSize];

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (input[i] > max[i]) {
                    max[i] = input[i];
                }
                if (input[i] < min[i]) {
                    min[i] = input[i];
                }
            }
        }        
    }    
    
}
