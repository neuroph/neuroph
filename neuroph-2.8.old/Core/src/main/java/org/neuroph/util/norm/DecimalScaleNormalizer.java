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

package org.neuroph.util.norm;

import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;

/**
 * Decimal scaling normalization method, which normalize data by moving decimal point
 * in regard to max element in training set (by columns)
 * Normalization is done according to formula:
 * normalizedVector[i] = vector[i] / scaleFactor[i]
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DecimalScaleNormalizer implements Normalizer {
    double[] max; // contains max values for all columns
    double[] scaleFactor;
        
 @Override
 public void normalize(DataSet dataSet) {
        findMaxVector(dataSet);        
        findScaleVector();

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            double[] normalizedInput = normalizeScale(input);
            dataSetRow.setInput(normalizedInput);                        
        }
    }    
 
    private void findMaxVector(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        max = new double[inputSize];

        for (DataSetRow row : dataSet.getRows()) {
            double[] input = row.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (Math.abs(input[i]) > max[i]) {
                    max[i] = Math.abs(input[i]);
                }
            }
        }        
    }         
    
    public void findScaleVector() {
        scaleFactor = new double[max.length];
        for(int i = 0; i < scaleFactor.length; i++)
            scaleFactor[i] = 1; 
        
        for(int i = 0; i < max.length; i++) {            
            while(max[i]>1) {
                max[i] = max[i] / 10.0;
                scaleFactor[i] = scaleFactor[i] * 10;
            }            
        }            
    }
    
    public double[] normalizeScale(double[] vector) {
        double[] normalizedVector = new double[vector.length];        
        for(int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / scaleFactor[i];
        }        
        return normalizedVector;
    }     
    
}
