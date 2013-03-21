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

package org.neuroph.nnet.comp.neuron;

import org.neuroph.core.Neuron;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Linear;

/**
 * Provides input neuron behaviour - neuron with input extranaly set, which just
 * transfer that input to output without change. Its purporse is to distribute its input
 * to all neurons it is connected to. It has no input connections
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class InputNeuron extends Neuron {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of InputNeuron with linear transfer function
     */
    public InputNeuron() {
        super(new WeightedSum(), new Linear());
    }

    /**
     * Calculate method of this type of neuron just transfers its externaly set 
     * input (with setNetInput) to its output
     */
    @Override
    public void calculate() {
        this.output = this.netInput;
    }
}
