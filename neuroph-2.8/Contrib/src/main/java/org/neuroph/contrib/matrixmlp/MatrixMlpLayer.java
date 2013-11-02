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

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.nnet.comp.neuron.BiasNeuron;

/**
 * Matrix based layer optimized for backpropagation
 * @author Zoran Sevarac
 */
public class MatrixMlpLayer implements MatrixLayer {
    
    Layer sourceLayer;

    /**
     * Number of neurons in this layer
     */
    int neuronsCount = 0;

    /**
     * Number of inputs from previous layer
     */
    int inputsCount = 0;
    
    double[][] weights;
    double[][] deltaWeights;
    boolean useBias;
//    double[] biases;
//    double[] deltaBiases;

    double[] inputs;
    double[] netInput;
    double[] outputs;
    double[] errors;

    TransferFunction transferFunction = new Tanh();
    MatrixLayer previousLayer = null;
    MatrixLayer nextLayer = null;


    // vidi konstruktore za MLayer
//    public MatrixBackPropLayer(int inputSize, int outputSize, TransferFunction transferFunction) {
//        inputs = new double[inputSize];
//        outputs = new double[outputSize];
//        weights = new double[outputSize][inputSize];
//
//        this.transferFunction = transferFunction;
//    }



      public MatrixMlpLayer(Layer sourceLayer, MatrixLayer previousLayer, TransferFunction transferFunction) {
          this.sourceLayer = sourceLayer;
          this.previousLayer = previousLayer;
          if (!(previousLayer instanceof MatrixInputLayer)) ((MatrixMlpLayer)previousLayer).setNextLayer(this);
          this.transferFunction = transferFunction;

          this.neuronsCount = sourceLayer.getNeuronsCount();
//          if (sourceLayer.getNeuronAt(neuronsCount-1) instanceof BiasNeuron) this.neuronsCount = this.neuronsCount -1;

          this.inputsCount = previousLayer.getOutputs().length;

          outputs = new double[neuronsCount];
//          biases = new double[neuronsCount];
//          deltaBiases = new double[neuronsCount];
          inputs = new double[inputsCount];
          netInput = new double[neuronsCount];
          weights = new double[neuronsCount][inputsCount];
          deltaWeights = new double[neuronsCount][inputsCount];

          errors = new double[neuronsCount];

          copyNeuronsToMatrices();
      }


    public MatrixLayer getPreviousLayer() {
        return previousLayer;
    }

    public void setPreviousLayer(MatrixLayer previousLayer) {
        this.previousLayer = previousLayer;
    }

    public MatrixLayer getNextLayer() {
        return nextLayer;
    }

    public void setNextLayer(MatrixLayer nextLayer) {
        this.nextLayer = nextLayer;
    }

    

    // maybe omit the sourceLayer parmeter
    public void copyNeuronsToMatrices() {

        int neuronIdx = 0, connIdx = 0;
        for(Neuron neuron : this.sourceLayer.getNeurons()) {
            if (neuron instanceof BiasNeuron) {
                this.useBias = true;
            }
            outputs[neuronIdx] = neuron.getOutput();
            // should we copy net inputs also? weightedSums or netInputs = neuron.getNetInput()
            connIdx = 0;
            for(Connection conn : neuron.getInputConnections()) {
                weights[neuronIdx][connIdx] = conn.getWeight().getValue();
                connIdx++;
            }
            neuronIdx++;
        }
    }

    public void copyMatricesToNeurons() {
        // assume full conectivity
    }

    public double[] getInputs() {
        return this.inputs;
    }

    public double[] getOutputs() {
        return this.outputs;
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(double[] outputs) {
        this.outputs = outputs;
    }

    public double[][] getWeights() {
        return weights;
    }

//    public double[] getBiases() {
//        return biases;
//    }

//    public void setBiases(double[] biases) {
//        this.biases = biases;
//    }

    
    public void getInputsFromPreviousLayer() {
        this.inputs = this.previousLayer.getOutputs();
    }


    final public void calculate() {
        this.inputs = previousLayer.getOutputs();
       	for (int i = 0; i < neuronsCount; i++) {
            netInput[i] = 0;
            for (int j = 0; j < inputs.length; j++) {
                    netInput[i] += inputs[j] * weights[i][j];
            }
//            netInput[i] = netInput[i] + biases[i];
            outputs[i] = transferFunction.getOutput(netInput[i]);
	}

            if (useBias)
                outputs[neuronsCount-1] = 1; // this one is bias neuron
    }

    public int getNeuronsCount() {
        return outputs.length;
    }

   public double[] getErrors() {
        return errors;
    }

    public void setErrors(double[] errors) {
        this.errors = errors;
    }

    public TransferFunction getTransferFunction() {
        return transferFunction;
    }

    public double[] getNetInput() {
        return netInput;
    }

    public void saveCurrentWeights() {
        System.arraycopy(weights, 0, deltaWeights, 0, weights.length);
   //     System.arraycopy(biases, 0, deltaBiases, 0, biases.length);

    }

//    public double[] getDeltaBiases() {
//        return deltaBiases;
//    }

    public double[][] getDeltaWeights() {
        return deltaWeights;
    }

    
    public void sync() {
        // synchronize matrix and object structures
    }
}
