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
package org.neuroph.core.input;

import org.neuroph.core.Connection;

/**
 * Optimized version of weighted input function
 *
 * @author Zoran Sevarac
 */
public class WeightedSum extends InputFunction {

    private static final long serialVersionUID = 1L;

    @Override
    public double getOutput(Connection[] inputConnections) {
        double output = 0d;

        for (Connection connection : inputConnections) {
            output += connection.getWeightedInput();
        }

        return output;
    }

    public static double[] getOutput(double[] inputs, double[] weights) {
        double[] output = new double[inputs.length];

        for (int i = 0; i < inputs.length; i++) {
            output[i] += inputs[i] * weights[i];
        }

        return output;
    }
}
