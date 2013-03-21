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

package org.neuroph.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.util.NeuronFactory;
import org.neuroph.util.NeuronProperties;

/**
 *<pre>
 * Layer of neurons in a neural network. The Layer is basic neuron container (a collection of neurons),
 * and it provides methods for manipulating neurons (add, remove, get, set, calculate, randomize).
 * </pre>
 * 
 * @see Neuron
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Layer implements Serializable {
	
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */
	private static final long serialVersionUID = 3L;
	
	/**
	 * Reference to parent neural network
	 */
	private NeuralNetwork parentNetwork;

	/**
	 * Array of neurons (Neuron instances)
	 */
	protected Neuron[] neurons;
        
        
        /**
         * Label for this layer
         */
        private String label;

	/**
	 *  Creates an instance of empty Layer
	 */
	public Layer() {
		this.neurons = new Neuron[0];
	}

	/**
	 * Creates an instance of Layer with the specified number of neurons with
	 * specified neuron properties
         *
         * @param neuronsCount
         *          number of neurons in layer
         * @param neuronProperties
         *          properties of neurons in layer
	 */
	public Layer(int neuronsCount, NeuronProperties neuronProperties) {
		this();

		for (int i = 0; i < neuronsCount; i++) {
			Neuron neuron = NeuronFactory.createNeuron(neuronProperties);
			this.addNeuron(neuron);
		}
	}

	/**
	 * Sets reference on parent network
	 * 
	 * @param parent
	 *            parent network
	 */
	public void setParentNetwork(NeuralNetwork parent) {
		this.parentNetwork = parent;
	}

	/**
	 * Returns reference to parent network
	 * 
	 * @return reference on parent neural network
	 */
	public NeuralNetwork getParentNetwork() {
		return this.parentNetwork;
	}

	/**
	 * Returns array of neurons in this layer
	 * 
	 * @return array of neurons in this layer
	 */
	public final Neuron[] getNeurons() {
		return this.neurons;
	}

	/**
	 * Adds specified neuron to this layer
	 * 
	 * @param neuron
	 *            neuron to add
	 */
	public final void addNeuron(Neuron neuron) {
            // prevent adding null neurons
            if (neuron == null) { 
                throw new NeurophException("Neuron cant be null!");
            }
                       
            // grow existing neurons array to make space for new neuron
            this.neurons =  Arrays.copyOf(neurons, neurons.length+1);     
            
            // set neuron's parent layer to this layer
            neuron.setParentLayer(this); 
            
            // add new neuron at the end of the array
            this.neurons[neurons.length - 1] = neuron;                    
	}

	/**
	 * Adds specified neuron to this layer,at specified index position
	 * 
	 * @param neuron
	 *            neuron to add
	 * @param index
	 *            index position at which neuron should be added
	 */
	public final void addNeuron(int index, Neuron neuron) {
                // make sure that index position is within allowed range
                if ((index >= neurons.length) || (index < 0)) {
                    throw new NeurophException("Specified neuron index position is out of range: "+index);
                } 
            
                // and the neuron is not null
                if (neuron == null) { 
                    throw new NeurophException("Neuron cant be null!");
                }     
                           		                              
                // grow existing neurons array to make space for new neuron
                this.neurons =  Arrays.copyOf(neurons, neurons.length+1); 
                
                // shift all neurons after index position to the right
                for (int i = neurons.length-1; i > index; i-- ) {
                       this.neurons[i] = this.neurons[i-1];
                }
	
                // set neuron's parent layer to this layer
                neuron.setParentLayer(this);                
                
                // add new neuron to specified index position
                this.neurons[index] = neuron;
	}

	/**
	 * Sets (replace) the neuron at specified position in layer
	 * 
	 * @param index
	 *            index position to set/replace
	 * @param neuron
	 *            new Neuron object to set
	 */
        public void setNeuron(int index, Neuron neuron) {
            // make sure that index position is within allowed range...
            if ((index >= neurons.length) || (index < 0)) {
                throw new NeurophException("Specified neuron index position is out of range: " + index);
            }

            // and the neuron is not null
            if (neuron == null) {
                throw new NeurophException("Neuron cant be null!");
            }

            // set neuron's parent layer to this layer                        
            neuron.setParentLayer(this);

            // now safely set new neuron at specified index position
            this.neurons[index] = neuron;
        }

	/**
	 * Removes neuron from layer
	 * 
	 * @param neuron
	 *            neuron to remove
	 */
	public void removeNeuron(Neuron neuron) {
            int index = indexOf(neuron);
            removeNeuronAt(index);
	}

	/**
	 * Removes neuron at specified index position in this layer
	 * 
	 * @param index
	 *            index position of neuron to remove
	 */
	public void removeNeuronAt(int index) {
              neurons[index].removeAllConnections();
            
             for (int i = index; i < neurons.length-1; i++ ) {
                 neurons[i] = neurons[i+1];
             }

              neurons[neurons.length-1] = null;    
              if (neurons.length > 0) {
                neurons = Arrays.copyOf(neurons, neurons.length-1);
              }
	}
        
        public void removeAllNeurons() {
            for(int i = 0; i <neurons.length; i++) {
                removeNeuronAt(i);
            }
        }

	/**
	 * Returns neuron at specified index position in this layer
	 * 
	 * @param index
	 *            neuron index position
	 * @return neuron at specified index position
	 */
	public Neuron getNeuronAt(int index) {
		return this.neurons[index];
	}

	/**
	 * Returns the index position in layer for the specified neuron
	 * 
	 * @param neuron
	 *            neuron object
	 * @return index position of specified neuron
	 */
	public int indexOf(Neuron neuron) {
            for(int i = 0; i < this.neurons.length; i++) { 
                if (neurons[i] == neuron) {
                   return i;
                }
            }

            return -1;
	}

	/**
	 * Returns number of neurons in this layer
	 * 
	 * @return number of neurons in this layer
	 */
	public int getNeuronsCount() {
		return neurons.length;
	}

	/**
	 * Performs calculaton for all neurons in this layer
	 */
	public void calculate() {
            
                //neuronCalculators = parentNetwork.getNeuronCalculators();
         //   neuronCalculators = new CalculatorThread[4];
            
//                if (neuronCalculators == null) {
                    for(Neuron neuron : this.neurons) {
                            neuron.calculate();
                    }
//                } else {
//                    for(int i = 0; i < LayerCalculatorThread.threadCount; i++) {
//                            neuronCalculators[i] = new LayerCalculatorThread(i);
//                            neuronCalculators[i].calculate(this);
//                    }
//                    
//                    for(int i = 0; i < neuronCalculators.length; i++) {
//                        try {
//                            neuronCalculators[i].join();
//                        } catch (InterruptedException ignore) {  }
//                    }                    
                    
             //   }
	}

	/**
	 * Resets the activation and input levels for all neurons in this layer
	 */
	public void reset() {
		for(Neuron neuron : this.neurons) {
			neuron.reset();
		}		
	}

	/**
	 * Randomize input connection weights for all neurons in this layer
	 */
	public void randomizeWeights() {
		for(Neuron neuron : this.neurons) {
			neuron.randomizeWeights();
		}
	}


	/**
	 * Randomize input connection weights for all neurons in this layer
         * within specified value range
	 */
	public void randomizeWeights(double minWeight, double maxWeight) {
		for(Neuron neuron : this.neurons) {
			neuron.randomizeWeights(minWeight, maxWeight);
		}
	}
        
        /**
         * Initialize connection weights for all neurons in this layer using a
         * random number generator
         *
         * @param generator the random number generator
         */
        public void randomizeWeights(Random generator) {
              for(Neuron neuron : this.neurons) {
                   neuron.randomizeWeights(generator);
              }
        }        

        /**
         * Initialize connection weights for the whole layer to to specified value
         * 
         * @param value the weight value
         */
        public void initializeWeights(double value) {
              for(Neuron neuron : this.neurons) {
                  neuron.initializeWeights(value);
              }
        }

        /**
         * Get layer label
         * @return layer label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Set layer label
         * @param label layer label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

 
}
