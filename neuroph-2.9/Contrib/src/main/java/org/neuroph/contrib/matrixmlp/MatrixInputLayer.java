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

package org.neuroph.contrib.matrixmlp;

/**
 * Input matrix layer
 * @author Zoran Sevarac
 */
public class MatrixInputLayer implements MatrixLayer {
    double[] inputs;

    public MatrixInputLayer(int neuronsCount) {
        this.inputs = new double[neuronsCount];
    }
    
    public void setInputs(double[] inputs) {
        System.arraycopy(inputs, 0, this.inputs, 0, inputs.length);
        this.inputs[this.inputs.length - 1] = 1;
        //this.inputs = inputs;
        // dodaj i bias output
    }

    public double[] getInputs() {
        return inputs;
    }

    public void setOutputs(double[] outputs) {
        this.inputs = outputs;
    }

    public double[] getOutputs() {
        return inputs;
    }

    public void calculate() { }


}
