package org.neuroph.util.data.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * This one does normalization for outputs too
 * @author zoran
 */
public class RangeNormalizer implements Normalizer {
    private double low, high;
    double[] maxIn, maxOut; // contains max values for in and out columns
    double[] minIn, minOut; // contains min values for in and out columns     

    public RangeNormalizer(double low, double high) {
        this.low= low;
        this.high = high;        
    }  
    
    @Override
    public void normalize(DataSet dataSet) {
        findMaxAndMinVectors(dataSet);
       
        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = normalizeToRange(row.getInput(), minIn, maxIn);
            double[] normalizedOutput = normalizeToRange(row.getDesiredOutput(), minOut, maxOut);
                        
            row.setInput(normalizedInput);
            row.setDesiredOutput(normalizedOutput);
            
        }
        
    }

    
    private double[] normalizeToRange(double[] vector, double[] min, double[] max) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = ((vector[i] - min[i]) / (max[i] - min[i])) * (high - low) + low ;
        }

        return normalizedVector;             
    }
    
    
    
    private void findMaxAndMinVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();
        
        maxIn = new double[inputSize];
        minIn = new double[inputSize];
        
        for(int i=0; i<inputSize; i++) {
            maxIn[i] = Double.MIN_VALUE;
            minIn[i] = Double.MAX_VALUE;
        }
        
        maxOut = new double[outputSize];
        minOut = new double[outputSize];   
        
        for(int i=0; i<inputSize; i++) {
            maxOut[i] = Double.MIN_VALUE;
            minOut[i] = Double.MAX_VALUE;
        }        

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (input[i] > maxIn[i]) {
                    maxIn[i] = input[i];
                }
                if (input[i] < minIn[i]) {
                    minIn[i] = input[i];
                }
            }
            
            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < outputSize; i++) {
                if (output[i] > maxOut[i]) {
                    maxOut[i] = output[i];
                }
                if (output[i] < minOut[i]) {
                    minOut[i] = output[i];
                }
            }            
                                    
        }        
    }     
    
}
