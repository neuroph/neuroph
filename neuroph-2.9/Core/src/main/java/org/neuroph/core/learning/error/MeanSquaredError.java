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

package org.neuroph.core.learning.error;

import java.io.Serializable;

/**
 * Commonly used mean squared error
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MeanSquaredError implements ErrorFunction, Serializable {

    private static final long serialVersionUID = 1L;
    
    private transient double totalError;
    /**
     * Number of patterns - n 
     */
    private transient double patternCount;

    public MeanSquaredError() {
        reset();
    }


    @Override
    public void reset() {
        totalError = 0d;
        patternCount = 0;
    }


    @Override
    public double getTotalError() {
        return totalError / ( 2 * patternCount );
    }

    @Override
    public double[]calculatePatternError(double[] predictedOutput, double[] targetOutput) {
        double[] patternError = new double[targetOutput.length];

        for (int i = 0; i < predictedOutput.length; i++) {
            patternError[i] =  targetOutput[i] - predictedOutput[i];
            totalError += patternError[i] * patternError[i];
        }
        patternCount++;
        return patternError;
    }

}
