package org.neuroph.core.data.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * This class does normalization of a data set to specified range
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RangeNormalizer implements Normalizer {
    private double lowLimit=0, highLimit=1;
    double[] maxIn, maxOut; // contains max values for in and out columns
    double[] minIn, minOut; // contains min values for in and out columns     

    public RangeNormalizer(double lowLimit, double highLimit) {
        this.lowLimit= lowLimit;
        this.highLimit = highLimit;        
    }  
    
    @Override
    public void normalize(DataSet dataSet) {
        findMaxAndMinVectors(dataSet);
       
        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = normalizeToRange(row.getInput(), minIn, maxIn);
            row.setInput(normalizedInput);
            
            if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeToRange(row.getDesiredOutput(), minOut, maxOut);
                row.setDesiredOutput(normalizedOutput);
            }
            
        }
        
    }
    
    private double[] normalizeToRange(double[] vector, double[] min, double[] max) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = ((vector[i] - min[i]) / (max[i] - min[i])) * (highLimit - lowLimit) + lowLimit ;
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
        
        for(int i=0; i<outputSize; i++) {
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
