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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.NotImplementedException;

import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Step;
import org.neuroph.core.transfer.TransferFunction;

/**
 * <pre>
 * Basic general neuron model according to McCulloch-Pitts neuron model.
 * Different neuron models can be created by using different input and transfer functions for instances of this class,
 * or by deriving from this class. The neuron is basic processing element of neural network.
 * This class implements the following behaviour:
 *
 * output = transferFunction( inputFunction(inputConnections) )
 * </pre>
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see InputFunction
 * @see TransferFunction
 */
public class Neuron implements Serializable, Cloneable /*, Callable<Void>*/ {

//    @Override
//    public Void call() throws Exception {
//        calculate();
//        return null;
//    }
    /**
     * The class fingerprint that is set to indicate serialization compatibility
     * with a previous version of the class
     */
    private static final long serialVersionUID = 4L;

    /**
     * Parent layer for this neuron
     */
    protected Layer parentLayer;

    /**
     * Collection of neuron's input connections (connections to this neuron)
     */
    protected List<Connection> inputConnections;

    /**
     * Collection of neuron's output connections (connections from this to other
     * neurons)
     */
    protected List<Connection> outConnections;

    /**
     * Total net input for this neuron. Represents total input for this neuron
     * received from input function.
     */
    protected transient double totalInput = 0;

    /**
     * Neuron output
     */
    protected transient double output = 0;

    /**
     * Local error (delta) for this neuron *
     */
    protected transient double delta = 0;

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
     * Creates an instance of Neuron with default settings: weighted sum input
     * function and Step transfer function. This is the basic McCulloch-Pitts
     * neuron model.
     */
    public Neuron() {
        this.inputFunction = new WeightedSum();
        this.transferFunction = new Step();
        this.inputConnections = new ArrayList<>();
        this.outConnections = new ArrayList<>();
    }

    /**
     * Creates an instance of Neuron with the specified input and transfer
     * functions.
     *
     * @param inputFunction input function for this neuron
     * @param transferFunction transfer function for this neuron
     */
    public Neuron(InputFunction inputFunction, TransferFunction transferFunction) {
        if (inputFunction == null) {
            throw new IllegalArgumentException("Input function cannot be null!");
        }

        if (transferFunction == null) {
            throw new IllegalArgumentException("Transfer function cannot be null!");
        }

        this.inputFunction = inputFunction;
        this.transferFunction = transferFunction;
        this.inputConnections = new ArrayList<>();
        this.outConnections = new ArrayList<>();
    }

    /**
     * Calculates neuron's output
     */
    public void calculate() {
        this.totalInput = inputFunction.getOutput(inputConnections);
        this.output = transferFunction.getOutput(totalInput);
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
     * @param input input value to set
     */
    public void setInput(double input) {
        this.totalInput = input;
    }

    /**
     * Returns total net input
     *
     * @return total net input
     */
    public double getNetInput() {
        return this.totalInput;
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
        return (inputConnections.size() > 0);
    }

    /**

     * Check the connection to neuron
     *
     * @param neuron neuron connection to be checked
     *
     * @return true if there is output connection, false otherwise
     */
    public boolean hasOutputConnectionTo(Neuron toNeuron) {
        for (Connection connection : outConnections) {
            if (connection.getToNeuron() == toNeuron) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the connection from neuron
     *
     * @param neuron neuron connection to be checked
     *
     * @return true if there is input connection, false otherwise
     */
    public boolean hasInputConnectionFrom(Neuron neuron) {
        for (Connection connection : inputConnections) {
            if (connection.getFromNeuron() == neuron) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the specified input connection
     *
     * @param connection input connection to add
     */
    public void addInputConnection(Connection connection) {
        // check whether connection is  null
        if (connection == null) {
            throw new IllegalArgumentException("Attempt to add null connection to neuron!");
        }

        // make sure that connection instance is pointing to this neuron
        if (connection.getToNeuron() != this) {
            throw new IllegalArgumentException("Cannot add input connection - bad toNeuron specified!");
        }

        // if it already has connection from same neuron do nothing
        if (this.hasInputConnectionFrom(connection.getFromNeuron())) {
            return;
        }

        this.inputConnections.add(connection);

        Neuron fromNeuron = connection.getFromNeuron();
        fromNeuron.addOutputConnection(connection);
    }

    /**
     * Adds input connection from specified neuron.
     *
     * @param fromNeuron neuron to connect from
     */
    public void addInputConnection(Neuron fromNeuron) {
        Connection connection = new Connection(fromNeuron, this);
        this.addInputConnection(connection);
    }

    /**
     * Adds input connection with the given weight, from given neuron
     *
     * @param fromNeuron neuron to connect from
     * @param weightVal connection weight value
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
        // check whether connection is  null
        if (connection == null) {
            throw new IllegalArgumentException("Attempt to add null connection to neuron!");
        }

        // make sure that connection instance is pointing to this neuron
        if (connection.getFromNeuron() != this) {
            throw new IllegalArgumentException("Cannot add output connection - bad fromNeuron specified!");
        }

        // if this neuron is already connected to neuron specified in connection do nothing
        if (this.hasOutputConnectionTo(connection.getToNeuron())) {
            return;
        }

        // Now we can safely add new connection
        this.outConnections.add(connection);
    }

    /**
     * Returns input connections for this neuron
     *
     * @return input connections of this neuron
     */
    public final List<Connection> getInputConnections() {
        return Collections.unmodifiableList(inputConnections);
    }

    /**
     * Returns output connections from this neuron
     *
     * @return output connections from this neuron
     */
    public final List<Connection> getOutConnections() {
        return Collections.unmodifiableList(outConnections);
    }

    protected void removeInputConnection(Connection conn) {
        inputConnections.remove(conn);
    }

    protected void removeOutputConnection(Connection conn) {
        outConnections.remove(conn);
    }

    /**
     * Removes input connection which is connected to specified neuron
     *
     * @param fromNeuron neuron which is connected as input
     */
    public void removeInputConnectionFrom(Neuron fromNeuron) {
        // run through all input connections
        for (Connection c : inputConnections) {
            // and look for specified fromNeuron
            if (c.getFromNeuron() == fromNeuron) {
                fromNeuron.removeOutputConnection(c);
                this.removeInputConnection(c);
                break; // assumes that a pair of neurons can only be connected once
            }
        }
    }

    public void removeOutputConnectionTo(Neuron toNeuron) {
        // run through all output connections
        for (Connection c : outConnections) {
            // and look for specified toNeuron
            if (c.getToNeuron() == toNeuron) {
                toNeuron.removeInputConnection(c);
                this.removeOutputConnection(c);
                break; // assumes that a pair of neurons can only be connected once
            }
        }
    }

    public void removeAllInputConnections() {
        inputConnections.clear();
    }

    public void removeAllOutputConnections() {
        outConnections.clear();
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
        for (Connection connection : this.inputConnections) {
            if (connection.getFromNeuron() == fromNeuron) {
                return connection;
            }
        }
        return null;
    }

    /**
     * Sets input function
     *
     * @param inputFunction input function for this neuron
     */
    public void setInputFunction(InputFunction inputFunction) {
        this.inputFunction = inputFunction;
    }

    /**
     * Sets transfer function
     *
     * @param transferFunction transfer function for this neuron
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
     * @param parent reference on layer in which the cell is located
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
        Weight[] weights = new Weight[inputConnections.size()];
        for (int i = 0; i < inputConnections.size(); i++) {
            weights[i] = inputConnections.get(i).getWeight();
        }
        return weights;
    }

    /**
     * Returns delta (error) for this neuron. This is used by backpropagation
     * learning rules.
     *
     * @return error for this neuron which is set by learning rule
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Sets delta for this neuron. This is used by backpropagation learning
     * rules.
     *
     * @param delta neuron delta
     */
    public void setDelta(double delta) {
        this.delta = delta;
    }

    /**
     * Sets this neuron output
     *
     * @param output value to set
     */
    public void setOutput(double output) {
        this.output = output;
    }

    /**
     * Initialize weights for all input connections to specified value
     *
     * @param value the weight value
     */
    public void initializeWeights(double value) {
        for (Connection connection : this.inputConnections) {
            connection.getWeight().setValue(value);
        }
    }

    /**
     * Returns label for this neuron
     *
     * @return label for this neuron
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label for this neuron
     *
     * @param label neuron label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not yer implemented");
        // return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
