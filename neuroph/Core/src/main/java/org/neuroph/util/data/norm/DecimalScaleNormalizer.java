/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.util.data.norm;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Decimal scaling normalization method, which normalize data by moving decimal
 * point in regard to max element in training set (by columns) Normalization is
 * done according to formula: normalizedVector[i] = vector[i] / scaleFactor[i]
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DecimalScaleNormalizer implements Normalizer {

    private double[] maxIn, maxOut; // contains max values for all columns
    private  double[] scaleFactorIn, scaleFactorOut; // holds scaling values for all columns

    @Override
    public void normalize(DataSet dataSet) {
        findMaxVectors(dataSet);
        findScaleVectors();

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] normalizedInput = normalizeScale(dataSetRow.getInput(), scaleFactorIn);
            dataSetRow.setInput(normalizedInput);

            if (dataSet.isSupervised()) {
                double[] normalizedOutput = normalizeScale(dataSetRow.getDesiredOutput(), scaleFactorOut);
                dataSetRow.setDesiredOutput(normalizedOutput);
            }
        }
    }

    /**
     * Finds max values for all columns in dataset (inputs and outputs)
     * Sets max column values to maxIn and maxOut fields
     * @param dataSet 
     */
    private void findMaxVectors(DataSet dataSet) {
        int inputSize = dataSet.getInputSize();
        int outputSize = dataSet.getOutputSize();

        maxIn = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            maxIn[i] = Double.MIN_VALUE;
        }

        maxOut = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            maxOut[i] = Double.MIN_VALUE;
        }

        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double[] input = dataSetRow.getInput();
            for (int i = 0; i < inputSize; i++) {
                if (input[i] > maxIn[i]) {
                    maxIn[i] = input[i];
                }
            }

            double[] output = dataSetRow.getDesiredOutput();
            for (int i = 0; i < outputSize; i++) {
                if (output[i] > maxOut[i]) {
                    maxOut[i] = output[i];
                }
            }

        }
    }

    public void findScaleVectors() {
        scaleFactorIn = new double[maxIn.length];
        for (int i = 0; i < scaleFactorIn.length; i++) {
            scaleFactorIn[i] = 1;
        }

        for (int i = 0; i < maxIn.length; i++) {
            while (maxIn[i] > 1) {
                maxIn[i] = maxIn[i] / 10.0;
                scaleFactorIn[i] = scaleFactorIn[i] * 10;
            }
        }

        scaleFactorOut = new double[maxOut.length];
        for (int i = 0; i < scaleFactorOut.length; i++) {
            scaleFactorOut[i] = 1;
        }

        for (int i = 0; i < maxOut.length; i++) {
            while (maxOut[i] > 1) {
                maxOut[i] = maxOut[i] / 10.0;
                scaleFactorOut[i] = scaleFactorOut[i] * 10;
            }
        }


    }

    private double[] normalizeScale(double[] vector, double[] scaleFactor) {
        double[] normalizedVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = vector[i] / scaleFactor[i];
        }
        return normalizedVector;
    }
}
