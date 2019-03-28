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

import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.BAM;
import org.neuroph.nnet.CompetitiveNetwork;
import org.neuroph.nnet.Hopfield;
import org.neuroph.nnet.Instar;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.MaxNet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Outstar;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.nnet.learning.ResilientPropagation;

/**
 * Provides methods to create various neural networks.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuralNetworkFactory {

	/**
	 * Creates and returns a new instance of Adaline network
	 * @param inputsCount number of inputs of Adaline network
	 * @return instance of Adaline network
	 */
	public static Adaline createAdaline(int inputsCount) {
		Adaline nnet = new Adaline(inputsCount);
		return nnet;
	}


	/**
	 * Creates  and returns a new instance of Perceptron network
	 * @param inputNeuronsCount number of neurons in input layer
         * @param outputNeuronsCount number of neurons in output layer
         * @param transferFunctionType type of transfer function to use
	 * @return instance of Perceptron network
	 */	
	public static Perceptron createPerceptron(int inputNeuronsCount, int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		Perceptron nnet = new Perceptron(inputNeuronsCount, outputNeuronsCount, transferFunctionType);
		return nnet;
	}

	/**
	 * Creates  and returns a new instance of Perceptron network
	 * @param inputNeuronsCount number of neurons in input layer
         * @param outputNeuronsCount number of neurons in output layer
         * @param transferFunctionType type of transfer function to use
         * @param learningRule learning rule class
	 * @return instance of Perceptron network
	 */
	public static Perceptron createPerceptron(int inputNeuronsCount, int outputNeuronsCount, TransferFunctionType transferFunctionType, Class learningRule) {
		Perceptron nnet = new Perceptron(inputNeuronsCount, outputNeuronsCount, transferFunctionType);

                if (learningRule.getName().equals(PerceptronLearning.class.getName()))  {
                    nnet.setLearningRule(new PerceptronLearning());
                } else if (learningRule.getName().equals(BinaryDeltaRule.class.getName())) {
                    nnet.setLearningRule(new BinaryDeltaRule());
                }

		return nnet;
	}

	/**
	 * Creates and returns a new instance of Multi Layer Perceptron
	 * @param layersStr space separated number of neurons in layers
	 * @param transferFunctionType transfer function type for neurons
	 * @return instance of Multi Layer Perceptron
	 */
	public static MultiLayerPerceptron createMLPerceptron(String layersStr, TransferFunctionType transferFunctionType) {
		ArrayList<Integer> layerSizes = VectorParser.parseInteger(layersStr);
		MultiLayerPerceptron nnet = new MultiLayerPerceptron(layerSizes,
				transferFunctionType);
		return nnet;
	}

	/**
	 * Creates and returns a new instance of Multi Layer Perceptron
	 * @param layersStr space separated number of neurons in layers
	 * @param transferFunctionType transfer function type for neurons
	 * @return instance of Multi Layer Perceptron
	 */
	public static MultiLayerPerceptron createMLPerceptron(String layersStr, TransferFunctionType transferFunctionType, Class learningRule,  boolean useBias, boolean connectIO) {
		ArrayList<Integer> layerSizes = VectorParser.parseInteger(layersStr);
                NeuronProperties neuronProperties = new NeuronProperties(transferFunctionType, useBias);
		MultiLayerPerceptron nnet = new MultiLayerPerceptron(layerSizes, neuronProperties);
                
                // set learning rule - TODO: use reflection here
                if (learningRule.getName().equals(BackPropagation.class.getName()))  {
                    nnet.setLearningRule(new BackPropagation());
                } else if (learningRule.getName().equals(MomentumBackpropagation.class.getName())) {
                    nnet.setLearningRule(new MomentumBackpropagation());
                } else if (learningRule.getName().equals(DynamicBackPropagation.class.getName())) {
                    nnet.setLearningRule(new DynamicBackPropagation());
                } else if (learningRule.getName().equals(ResilientPropagation.class.getName())) {
                    nnet.setLearningRule(new ResilientPropagation());
                } 

                // connect io
                if (connectIO) {
                    nnet.connectInputsToOutputs();
                }

		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of Hopfield network
	 * @param neuronsCount number of neurons in Hopfield network
	 * @return instance of Hopfield network
	 */
	public static Hopfield createHopfield(int neuronsCount) {
		Hopfield nnet = new Hopfield(neuronsCount);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of BAM network
	 * @param inputNeuronsCount number of input neurons
         * @param outputNeuronsCount number of output neurons
	 * @return instance of BAM network
	 */
	public static BAM createBam(int inputNeuronsCount, int outputNeuronsCount) {
		BAM nnet = new BAM(inputNeuronsCount, outputNeuronsCount);
		return nnet;
	}	

	/**
	 * Creates and returns a new instance of Kohonen network
	 * @param inputNeuronsCount number of input neurons
         * @param outputNeuronsCount number of output neurons
	 * @return instance of Kohonen network
	 */
	public static Kohonen createKohonen(int inputNeuronsCount, int outputNeuronsCount) {
		Kohonen nnet = new Kohonen(inputNeuronsCount, outputNeuronsCount);
		return nnet;
	}

	/**
	 * Creates and returns a new instance of Hebbian network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @param transferFunctionType neuron's transfer function type
	 * @return instance of Hebbian network
	 */
	public static SupervisedHebbianNetwork createSupervisedHebbian(int inputNeuronsCount,
			int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		SupervisedHebbianNetwork nnet = new SupervisedHebbianNetwork(inputNeuronsCount,
				outputNeuronsCount, transferFunctionType);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of Unsupervised Hebbian Network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @param transferFunctionType neuron's transfer function type
	 * @return instance of Unsupervised Hebbian Network
	 */
	public static UnsupervisedHebbianNetwork createUnsupervisedHebbian(int inputNeuronsCount,
			int outputNeuronsCount, TransferFunctionType transferFunctionType) {
		UnsupervisedHebbianNetwork nnet = new UnsupervisedHebbianNetwork(inputNeuronsCount,
				outputNeuronsCount, transferFunctionType);
		return nnet;
	}	

	/**
	 * Creates and returns a new instance of Max Net network
	 * @param neuronsCount number of neurons (same num in input and output layer)
	 * @return instance of Max Net network
	 */
	public static MaxNet createMaxNet(int neuronsCount) {
		MaxNet nnet = new MaxNet(neuronsCount);
		return nnet;
	}
	
	/**
	 * Creates and returns a new instance of Instar network
	 * @param inputNeuronsCount umber of input neurons
	 * @return instance of Instar network
	 */
	public static Instar createInstar(int inputNeuronsCount) {
		Instar nnet = new Instar(inputNeuronsCount);
		return nnet;
	}	
	
	/**
	 * Creates and returns a new instance of Outstar network
	 * @param outputNeuronsCount number of output neurons
	 * @return instance of Outstar network
	 */
	public static Outstar createOutstar(int outputNeuronsCount) {
		Outstar nnet = new Outstar(outputNeuronsCount);
		return nnet;
	}	

	/**
	 * Creates and returns a new instance of competitive network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @return instance of CompetitiveNetwork
	 */
	public static CompetitiveNetwork createCompetitiveNetwork(
			int inputNeuronsCount, int outputNeuronsCount) {
		CompetitiveNetwork nnet = new CompetitiveNetwork(inputNeuronsCount, outputNeuronsCount);
		return nnet;
	}

	/**
	 * Creates and returns a new instance of RBF network
	 * @param inputNeuronsCount number of neurons in input layer
	 * @param rbfNeuronsCount number of neurons in RBF layer
	 * @param outputNeuronsCount number of neurons in output layer
	 * @return instance of RBF network
	 */
	public static RBFNetwork createRbfNetwork(int inputNeuronsCount,
			int rbfNeuronsCount, int outputNeuronsCount) {
		RBFNetwork nnet = new RBFNetwork(inputNeuronsCount, rbfNeuronsCount,
				outputNeuronsCount);
		return nnet;
	}

	/**
	 * Sets default input and output neurons for network (first layer as input,
	 * last as output)
	 */
	public static void setDefaultIO(NeuralNetwork nnet) {
               ArrayList<Neuron> inputNeuronsList = new ArrayList<>();
                Layer firstLayer = nnet.getLayerAt(0);
                for (Neuron neuron : firstLayer.getNeurons() ) {
                    if (!(neuron instanceof BiasNeuron)) {  // dont set input to bias neurons
                        inputNeuronsList.add(neuron);
                    }
                }

		List<Neuron> outputNeurons = ((Layer) nnet.getLayerAt(nnet.getLayersCount()-1)).getNeurons();

		nnet.setInputNeurons(inputNeuronsList);
		nnet.setOutputNeurons(outputNeurons); 
	}

}
