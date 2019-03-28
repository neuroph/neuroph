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

package org.neuroph.nnet.comp.layer;

import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.Linear;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 * Represents a layer of input neurons - a typical neural network input layer 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see InputNeuron
 */
public class InputLayer extends Layer {

    /**
     * Creates a new instance of InputLayer with specified number of input neurons
     * @param neuronsCount input neurons count for this layer
     */
    public InputLayer(int neuronsCount) {
        NeuronProperties inputNeuronProperties = new NeuronProperties(InputNeuron.class, Linear.class);

        for (int i = 0; i < neuronsCount; i++) {
            Neuron neuron = NeuronFactory.createNeuron(inputNeuronProperties);
            this.addNeuron(neuron);
        }
    }
}
