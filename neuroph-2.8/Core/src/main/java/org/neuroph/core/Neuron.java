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
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Step;
import org.neuroph.core.transfer.TransferFunction;

/**
 *<pre>
 * Basic general neuron model according to McCulloch-Pitts neuron model.
 * Different neuron models can be created by using different input and transfer functions for instances of this class,
 * or by deriving from this class. The neuron is basic processing element of neural network.
 * This class implements the following behaviour:
 *  
 * output = transferFunction( inputFunction(inputConnections) )
 *</pre>
 * 
 * @see InputFunction
 * @see TransferFunction
 * @author Zoran Sevarac <sevarac@gmail.com>
 */

public class Neuron implements Serializable {
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 3L;

	/**
	 * Parent layer for this neuron
	 */
	protected Layer parentLayer;

	/**
	 * Array of neuron's input connections (connections to this neuron)
	 */
	protected Connection[] inputConnections;

	/**
	 * Array of neuron's output connections (connections from this to other
	 * neurons)
	 */
	protected Connection[] outConnections;

        /**
	 * Total net input for this neuron. Represents total input for this neuron
	 * received from input function.
	 */
	protected transient double netInput = 0;

	/**
	 * Neuron output
	 */
	protected transient double output = 0;

	/**
	 * Local error for this neuron
	 */
	protected transient double error = 0;

	/**
	 * Input function for this neuron
	 */
	protected InputFunction inputFunction;

	/**
	 * Transfer function for this neuron
	 */
	protected TransferFunction transferFunction;

        /**
         * Neuron's label
         */
        private String label;
        
	/**
	 * Creates an instance of Neuron with the weighted sum, input function 
	 * and Step transfer function. This is the original McCulloch-Pitts 
	 * neuron model.
	 */
	public Neuron() {
		this.inputFunction = new WeightedSum();
		this.transferFunction = new Step();
                this.inputConnections = new Connection[0];
                this.outConnections = new Connection[0];
	}

	/**
	 * Creates an instance of Neuron with the specified input and transfer functions.
	 * 
	 * @param inputFunction
	 *            input function for this neuron
	 * @param transferFunction
	 *            transfer function for this neuron
	 */
	public Neuron(InputFunction inputFunction, TransferFunction transferFunction) {
                if (inputFunction == null) {
                    throw new NeurophException("Input function cannot be null!");
                }
                
                if (transferFunction == null) {
                    throw new NeurophException("Transfer function cannot be null!");
                }                
            
		this.inputFunction = inputFunction;
		this.transferFunction = transferFunction;
                this.inputConnections = new Connection[0];
                this.outConnections = new Connection[0];                
	}

	/**
	 * Calculates neuron's output
	 */
	public void calculate() {
                if ((this.inputConnections.length > 0)) {
			this.netInput = this.inputFunction.getOutput(this.inputConnections);
		}

                this.output = this.transferFunction.getOutput(this.netInput);
	}

	/**
	 * Sets input and output activation levels to zero
	 */
	public void reset() {
		this.setInput(0d);
		this.setOutput(0d);
	}

	/**
	 * Sets neuron's input
	 * 
	 * @param input
	 *            input value to set
	 */
	public void setInput(double input) {
		this.netInput = input;
	}

	/**
	 * Returns total net input
	 * 
	 * @return total net input
	 */
	public double getNetInput() {
		return this.netInput;
	}

	/**
	 * Returns neuron's output
	 * 
	 * @return neuron output
	 */
	public double getOutput() {
		return this.output;
	}

	/**
	 * Returns true if there are input connections for this neuron, false
	 * otherwise
	 * 
	 * @return true if there is input connection, false otherwise
	 */
	public boolean hasInputConnections() {
		return (this.inputConnections.length > 0);
	}
        
        public boolean hasOutputConnectionTo(Neuron neuron) {
            for(Connection connection : outConnections) {
                if (connection.getToNeuron() == neuron) {
                    return true;
                }
            }            
            return false;            
        }
        
        public boolean hasInputConnectionFrom(Neuron neuron) {
            for(Connection connection : inputConnections) {
                if (connection.getFromNeuron() == neuron) {
                    return true;
                }
            }            
            return false;            
        }

	/**
	 * Adds the specified input connection
	 * 
	 * @param connection
	 *            input connection to add
	 */
	public void addInputConnection(Connection connection) {     
            // check whaeather connection is  null
            if (connection == null) {
                throw new NeurophException("Attempt to add null connection to neuron!");
            }
                  
            // make sure that connection instance is pointing to this neuron
            if (connection.getToNeuron() != this) {
                throw new NeurophException("Cannot add input connection - bad toNeuron specified!");
            } 
            
            // if it allready has connection from same neuron do nothing
            if (this.hasInputConnectionFrom(connection.getFromNeuron())) {
                return;
            }            
            
            this.inputConnections =  Arrays.copyOf(inputConnections, inputConnections.length+1);     // grow existing connections  array to make space for new connection
            this.inputConnections[inputConnections.length - 1] = connection;             
            Neuron fromNeuron = connection.getFromNeuron();
            fromNeuron.addOutputConnection(connection);                    
	}

	/**
	 * Adds input connection from specified neuron
	 *
	 * @param fromNeuron
	 *            neuron to connect from
	 */
	public void addInputConnection(Neuron fromNeuron) {
		Connection connection = new Connection(fromNeuron, this);
		this.addInputConnection(connection);
	}

	/**
	 * Adds input connection with the given weight, from given neuron
	 * 
	 * @param fromNeuron
	 *            neuron to connect from
	 * @param weightVal
	 *	      connection weight value
	 * 
	 */	
	public void addInputConnection(Neuron fromNeuron, double weightVal) {
		Connection connection = new Connection(fromNeuron, this, weightVal);
		this.addInputConnection(connection);
	}
	/**
	 * Adds the specified output connection
	 * 
	 * @param connection output connection to add
	 */
	protected void addOutputConnection(Connection connection) {            
            // First do some checks
            // check whaeather connection is  null
            if (connection == null) {
                throw new NeurophException("Attempt to add null connection to neuron!");
            }
                  
            // make sure that connection instance is pointing to this neuron
            if (connection.getFromNeuron() != this) {
                throw new NeurophException("Cannot add output connection - bad fromNeuron specified!");
            } 
            
            // if this neuron is allready connected to neuron specified in connection do nothing
            if (this.hasOutputConnectionTo(connection.getToNeuron())) {
                return;
            }  
            
            // Now we can safely add new connection
            // grow existing connections  array to make space for new connection
            this.outConnections =  Arrays.copyOf(outConnections, outConnections.length+1);     
           
            // add new connection to the end of array    
            this.outConnections[outConnections.length - 1] = connection;  
	}

	/**
	 * Returns input connections for this neuron
	 * 
	 * @return input connections of this neuron
	 */
	public final Connection[] getInputConnections() {
		return inputConnections;
	}

	/**
	 * Returns output connections from this neuron
	 * 
	 * @return output connections from this neuron
	 */
	public final Connection[] getOutConnections() {
		return outConnections;
	}
        
        protected void removeInputConnection(Connection conn) {
            for (int i = 0; i < inputConnections.length; i++) {
                if (inputConnections[i] == conn) {
                    for (int j = i; j < inputConnections.length - 1; j++) {
                        inputConnections[j] = inputConnections[j + 1];
                    }
                    
                    inputConnections[inputConnections.length-1] = null;
                    
                    if (inputConnections.length > 0) {
                        this.inputConnections = Arrays.copyOf(inputConnections, inputConnections.length-1); 
                    }                                        
                    break;                    
                }                                
            }
        }
        
        protected void removeOutputConnection(Connection conn) {
            for (int i = 0; i < outConnections.length; i++) {
                if (outConnections[i] == conn) {
                    for (int j = i; j < outConnections.length - 1; j++) {
                        outConnections[j] = outConnections[j + 1];
                    }
                    
                    outConnections[outConnections.length-1] = null;
                    
                    if (outConnections.length > 0) {
                        this.outConnections = Arrays.copyOf(outConnections, outConnections.length-1); 
                    }                                        
                    break;                    
                }                                
            }            
        }        

	/**
	 * Removes input connection which is connected to specified neuron
	 * 
	 * @param fromNeuron
	 *            neuron which is connected as input
	 */
	public void removeInputConnectionFrom(Neuron fromNeuron) {
            
            // run through all input connections
            for(int i = 0; i < inputConnections.length; i++) {
                        // and look for specified fromNeuron
			if (inputConnections[i].getFromNeuron() == fromNeuron) {
                            fromNeuron.removeOutputConnection(inputConnections[i]);    
                            this.removeInputConnection(inputConnections[i]);                                   
                            break;
			}
            }
                                    // when you find it shift all neurons after it to the left
//            				for(int j = i; j<inputConnections.length-1; j++) {
//                                    inputConnections[j] = inputConnections[j+1];
//                                }
//                                // then set last position in array to null
//                                inputConnections[inputConnections.length-1] = null;
//                                
//                                
//                                fromNeuron.removeOutputConnection(conn);
//                                
//                                break;
            
//            
//            // resize array in order to remove last element
//            if (inputConnections.length > 0) {
//                this.inputConnections = Arrays.copyOf(inputConnections, inputConnections.length-1); 
//            }
            
            // also delete reference to connection from the other side, since both neurons are 
            // pointing to the same connection instance
           // fromNeuron.removeOutputConnectionTo(this);
            
	}
        
	public void removeOutputConnectionTo(Neuron toNeuron) {
            for(int i = 0; i < outConnections.length; i++) {
                        // and look for specified fromNeuron
			if (outConnections[i].getToNeuron() == toNeuron) {
                            toNeuron.removeInputConnection(outConnections[i]);    
                            this.removeOutputConnection(outConnections[i]);  
                            break;
			}
            }            
            
//            for(int i = 0; i < outConnections.length; i++) {
//			if (outConnections[i].getToNeuron() == toNeuron) {
//				for(int j = i; j<outConnections.length-1; j++) {
//                                    outConnections[j] = outConnections[j+1];
//                                }
//                                outConnections[outConnections.length-1] = null;
//                                break;
//			}
//            }
//            
//                if (outConnections.length > 0) {
//                     this.outConnections = Arrays.copyOf(outConnections, outConnections.length-1);
//                }
	}        
        
        public void removeAllInputConnections() {
            // run through all input connections
            for(int i = 0; i < inputConnections.length; i++) {
                inputConnections[i].getFromNeuron().removeOutputConnection(inputConnections[i]);    
                inputConnections[i] = null;                                   
            }
            
            this.inputConnections = new Connection[0];
        }
        
        public void removeAllOutputConnections() {
            for(int i=0; i<outConnections.length; i++) {
                outConnections[i].getToNeuron().removeInputConnection(outConnections[i]);
                outConnections[i] = null;
            }            
            this.outConnections = new Connection[0];                   
        }
        
        public void removeAllConnections() {
            removeAllInputConnections();
            removeAllOutputConnections();
        }

	/**
	 * Gets input connection from the specified neuron * @param fromNeuron
	 * neuron connected to this neuron as input
	 */
	public Connection getConnectionFrom(Neuron fromNeuron) {	
		for(Connection connection : this.inputConnections) {
			if (connection.getFromNeuron() == fromNeuron)
				return connection;		
		}
		return null;
	}

	/**
	 * Sets input function
	 * 
	 * @param inputFunction
	 *            input function for this neuron
	 */
	public void setInputFunction(InputFunction inputFunction) {
		this.inputFunction = inputFunction;
	}

	/**
	 * Sets transfer function
	 * 
	 * @param transferFunction
	 *            transfer function for this neuron
	 */
	public void setTransferFunction(TransferFunction transferFunction) {
		this.transferFunction = transferFunction;
	}

	/**
	 * Returns input function
	 * 
	 * @return input function
	 */
	public InputFunction getInputFunction() {
		return this.inputFunction;
	}

	/**
	 * Returns transfer function
	 * 
	 * @return transfer function
	 */
	public TransferFunction getTransferFunction() {
		return this.transferFunction;
	}

	/**
	 * Sets reference to parent layer for this neuron (layer in which the neuron
	 * is located)
	 * 
	 * @param parent
	 *            reference on layer in which the cell is located
	 */
	public void setParentLayer(Layer parent) {
		this.parentLayer = parent;
	}

	/**
	 * Returns reference to parent layer for this neuron
	 * 
	 * @return parent layer for this neuron
	 */
	public Layer getParentLayer() {
		return this.parentLayer;
	}

	/**
	 * Returns weights vector of input connections
	 * 
	 * @return weights vector of input connections
	 */
	public Weight[] getWeights() {
		Weight[] weights = new Weight[inputConnections.length];
		for(int i = 0; i< inputConnections.length; i++) {
			weights[i] = inputConnections[i].getWeight();
		}
		return weights;
	}

	/**
	 * Returns error for this neuron. This is used by supervised learing rules.
	 * 
	 * @return error for this neuron which is set by learning rule
	 */
	public double getError() {
		return error;
	}

	/**
	 * Sets error for this neuron. This is used by supervised learing rules.
	 * 
	 * @param error
	 *            neuron error
	 */
	public void setError(double error) {
		this.error = error;
	}

	/**
	 * Sets this neuron output
	 * 
	 * @param output
	 *            value to set
	 */
	public void setOutput(double output) {
		this.output = output;
	}

	/**
	 * Randomize all input weights
	 */
	public void randomizeWeights() {
		for(Connection connection : this.inputConnections) {
			connection.getWeight().randomize();
		}		
	}

	/**
	 * Randomize all input weights within specified value range
	 */
	public void randomizeWeights(double minWeight, double maxWeight) {
		for(Connection connection : this.inputConnections) {
			connection.getWeight().randomize(minWeight, maxWeight);
		}
	}

        /**
         * Initialize weights for all input connections to specified value
         * @param value the weight value
         */
        public void initializeWeights(double value) {
             for(Connection connection : this.inputConnections) {
                  connection.getWeight().setValue(value);
             }
        }

        /**
         * Randomize weights for all input connections to using random number generator
         * @param generator the random number generator
         */
        public void randomizeWeights(Random generator) {
              for(Connection connection : this.inputConnections) {
                   connection.getWeight().randomize(generator);
              }
        }


        /**
         * Returns label for this neuron
         * @return label for this neuron
         */
        public String getLabel() {
            return label;
        }

        /**
         * Sets the label for this neuron
         * @param label neuron label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }



}
