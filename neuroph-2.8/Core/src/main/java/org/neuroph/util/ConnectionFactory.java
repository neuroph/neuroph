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

package org.neuroph.util;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.nnet.comp.DelayedConnection;
import org.neuroph.nnet.comp.neuron.BiasNeuron;

/**
 * Provides methods to connect neurons by creating Connection objects.
 */
 public class ConnectionFactory {

	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param fromNeuron
	 *            output neuron
	 * @param toNeuron
	 *            input neuron
	 */
	public static void createConnection(Neuron fromNeuron, Neuron toNeuron) {
		Connection connection = new Connection(fromNeuron, toNeuron);
		toNeuron.addInputConnection(connection);
	}

	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param fromNeuron
	 *            neuron to connect (connection source)
	 * @param toNeuron
	 *            neuron to connect to (connection target)
	 * @param weightVal
	 *            connection weight value
	 */
	public static void createConnection(Neuron fromNeuron, Neuron toNeuron, double weightVal) {
		Connection connection = new Connection(fromNeuron, toNeuron, weightVal);
		toNeuron.addInputConnection(connection);
	}

	public static void createConnection(Neuron fromNeuron, Neuron toNeuron, double weightVal, int delay) {
		DelayedConnection connection = new DelayedConnection(fromNeuron, toNeuron, weightVal, delay);
		toNeuron.addInputConnection(connection);
	}

	/**
	 * Creates connection between two specified neurons
	 * 
	 * @param fromNeuron
	 *            neuron to connect (connection source)
	 * @param toNeuron
	 *            neuron to connect to (connection target)
	 * @param weight
	 *            connection weight
	 */
	public static void createConnection(Neuron fromNeuron, Neuron toNeuron, Weight weight) {
		Connection connection = new Connection(fromNeuron, toNeuron, weight);
		toNeuron.addInputConnection(connection);
	}
        
        
        /**
	 * Creates  connectivity between specified neuron and all neurons in specified layer
	 * 
	 * @param fromNeuron
	 *            neuron to connect
	 * @param toLayer
	 *            layer to connect to
	 */        
	public static void createConnection(Neuron fromNeuron, Layer toLayer) {
                for (Neuron toNeuron : toLayer.getNeurons()) {
                    ConnectionFactory.createConnection(fromNeuron, toNeuron);
                }
	}
        

	/**
	 * Creates full connectivity between the two specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void fullConnect(Layer fromLayer, Layer toLayer) {
		for(Neuron fromNeuron : fromLayer.getNeurons()) {
			for (Neuron toNeuron : toLayer.getNeurons()) {
				createConnection(fromNeuron, toNeuron);
			} 
		} 
	}
        
	/**
	 * Creates full connectivity between the two specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void fullConnect(Layer fromLayer, Layer toLayer, boolean connectBiasNeuron) {
		for(Neuron fromNeuron : fromLayer.getNeurons()) {
                    if (fromNeuron instanceof BiasNeuron) {
                        continue;
                    }
                    for (Neuron toNeuron : toLayer.getNeurons()) {
			createConnection(fromNeuron, toNeuron);
                    } 
		} 
	}



	/**
	 * Creates full connectivity between two specified layers with specified
	 * weight for all connections
	 * 
	 * @param fromLayer
	 *            output layer
	 * @param toLayer
	 *            input layer
         * @param weightVal
         *             connection weight value
	 */
	public static void fullConnect(Layer fromLayer, Layer toLayer, double weightVal) {
		for(Neuron fromNeuron : fromLayer.getNeurons()) {
			for (Neuron toNeuron : toLayer.getNeurons()) {
				createConnection(fromNeuron, toNeuron, weightVal);
			} 
		} 		
	}

	/**
	 * Creates full connectivity within layer - each neuron with all other
	 * within the same layer
	 */
	public static void fullConnect(Layer layer) {
		int neuronNum = layer.getNeuronsCount();
		for (int i = 0; i < neuronNum; i++) {
			for (int j = 0; j < neuronNum; j++) {
				if (j == i)
					continue;
				Neuron from = layer.getNeuronAt(i);
				Neuron to = layer.getNeuronAt(j);
				createConnection(from, to);
			} // j
		} // i
	}

	/**
	 * Creates full connectivity within layer - each neuron with all other
	 * within the same layer with the specified weight values for all
	 * conections.
	 */
	public static void fullConnect(Layer layer, double weightVal) {
		int neuronNum = layer.getNeuronsCount();
		for (int i = 0; i < neuronNum; i++) {
			for (int j = 0; j < neuronNum; j++) {
				if (j == i)
					continue;
				Neuron from = layer.getNeuronAt(i);
				Neuron to = layer.getNeuronAt(j);
				createConnection(from, to, weightVal);
			} // j
		} // i
	}

	/**
	 * Creates full connectivity within layer - each neuron with all other
	 * within the same layer with the specified weight and delay values for all
	 * conections.
	 */
	public static void fullConnect(Layer layer, double weightVal, int delay) {
		int neuronNum = layer.getNeuronsCount();
		for (int i = 0; i < neuronNum; i++) {
			for (int j = 0; j < neuronNum; j++) {
				if (j == i)
					continue;
				Neuron from = layer.getNeuronAt(i);
				Neuron to = layer.getNeuronAt(j);
				createConnection(from, to, weightVal, delay);
			} // j
		} // i
	}

	/**
	 * Creates forward connectivity pattern between the specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void forwardConnect(Layer fromLayer, Layer toLayer, double weightVal) {
		for(int i=0; i<fromLayer.getNeuronsCount(); i++) {
			Neuron fromNeuron = fromLayer.getNeuronAt(i);
			Neuron toNeuron = toLayer.getNeuronAt(i);
			createConnection(fromNeuron, toNeuron, weightVal);
		}
	}

	/**
	 * Creates forward connection pattern between specified layers
	 * 
	 * @param fromLayer
	 *            layer to connect
	 * @param toLayer
	 *            layer to connect to
	 */
	public static void forwardConnect(Layer fromLayer, Layer toLayer) {
		for(int i=0; i<fromLayer.getNeuronsCount(); i++) {
			Neuron fromNeuron = fromLayer.getNeuronAt(i);
			Neuron toNeuron = toLayer.getNeuronAt(i);
			createConnection(fromNeuron, toNeuron, 1);
		}		
	}

}