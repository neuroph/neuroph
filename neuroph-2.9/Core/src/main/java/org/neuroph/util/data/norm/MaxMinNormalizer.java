/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.util.data.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * MaxMin normalization method, which normalize data in regard to min and max elements in training set (by columns)
 * Normalization is done according to formula:
 * normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i])
 * 
 * This class works fine if  max and min are both positive and we want to normalize to  [0,1]
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MaxMinNormalizer implements Normalizer {
    double[] maxIn, maxOut; // contains max values for in and out columns
    double[] minIn, minOut; // contains min values for in and out columns     


    @Override
    public void normalize(DataSet dataSet) {
        // find min i max vectors
        findMaxAndMinVectors(dataSet);
       
        for (DataSetRow row : dataSet.getRows()) {
           double[] normalizedInput = normalizeMaxMin(row.getInput(), minIn, maxIn);
           row.setInput(normalizedInput);
            
           if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeMaxMin(row.getDesiredOutput(), minOut, maxOut);
                row.setDesiredOutput(normalizedOutput);
           }
        }

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
  
    
    private double[] normalizeMaxMin(double[] vector, double[] min, double[] max) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i]);
        }

        return normalizedVector;             
    }    
    
}
