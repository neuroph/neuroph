/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
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

package org.neuroph.nnet;

import org.neuroph.nnet.learning.ConvolutionalBackpropagation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;
import org.neuroph.nnet.comp.layer.Layer2D;

/**
 * Convolutional neural network with backpropagation algorithm modified for 
 * convolutional networks. 
 * 
 * TODO: provide Hiton, LeCun, AndrewNg implementation specific features
 * 
 * @author Boris Fulurija
 * @author Zoran Sevarac
 * 
 * @see ConvolutionalBackpropagation
 */
public class ConvolutionalNetwork extends NeuralNetwork<ConvolutionalBackpropagation> {

	private static final long serialVersionUID = -1393907449047650509L;

        /**
         * Creates empty convolutional network with ConvolutionalBackpropagation learning rule
         */
	public ConvolutionalNetwork() {
		this.setLearningRule(new ConvolutionalBackpropagation());
	}
	
//      This method is not used anywhere...
//	public void connectLayers(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer, int fromFeatureMapIndex, int toFeatureMapIndex) {
//		ConvolutionalUtils.connectFeatureMaps(fromLayer, toLayer, fromFeatureMapIndex, toFeatureMapIndex);
//	}

        /**
         * Sets network input, to all feature maps in input layer
         * @param inputVector
         * @throws VectorSizeMismatchException 
         */
	@Override
	public void setInput(double... inputVector) throws VectorSizeMismatchException {
		// if (inputVector.length != getInputNeurons().length) {
		// throw new
		// VectorSizeMismatchException("Input vector size does not match network input dimension!");
		// }
		// It would be good if i could do the following:
		// int i = 0;
		// Layer inputLayer =getLayerAt(0);
		// for (Neuron neuron : inputLayer.getNeurons()){
		// neuron.setInput(inputVector[i++]);
		// }
		// But for that getNeuron must be non final method

		FeatureMapsLayer inputLayer = (FeatureMapsLayer) getLayerAt(0);
		int currentNeuron = 0;
		for (int i = 0; i < inputLayer.getNumberOfMaps(); i++) {
			Layer2D map = inputLayer.getFeatureMap(i);
			for (Neuron neuron : map.getNeurons()) {
				if (!(neuron instanceof BiasNeuron))
					neuron.setInput(inputVector[currentNeuron++]);
			}
		}
	}

}