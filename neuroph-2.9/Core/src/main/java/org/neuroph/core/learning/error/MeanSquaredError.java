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
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MeanSquaredError implements ErrorFunction, Serializable {
    private transient double totalSquaredErrorSum;
    private transient double n;

    public MeanSquaredError(double n) {
        this.n = n;
    }
    
    @Override
    public void reset() {
        totalSquaredErrorSum = 0;
    }
        
    @Override
    public double getTotalError() {
        return totalSquaredErrorSum/n;
    }

    @Override
    public void addOutputError(double[] outputError) {
        double outputErrorSqrSum = 0;
        for (double error : outputError) {
            outputErrorSqrSum += (error * error) * 0.5;
        }

        this.totalSquaredErrorSum += outputErrorSqrSum;
    }
    
}
