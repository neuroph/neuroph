package org.neuroph.nnet;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.layer.InputLayer;
import org.neuroph.nnet.comp.neuron.BiasNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * Under development: Learning rule BackProp Through Time required
 * @author zoran
 */
public class ElmanNetwork extends NeuralNetwork {

    public ElmanNetwork(int inputNeuronsCount, int hiddenNeuronsCount, int contextNeuronsCount, int outputNeuronsCount) {
        createNetwork(inputNeuronsCount, hiddenNeuronsCount, contextNeuronsCount, outputNeuronsCount);
    }
    // three layers: input, hidden, output
    // as mlp add context layer
    // elman connect output of hidden layer to input of context layer
    // output of context to input of hidden layer 
    
    
    
    
    
	private void createNetwork(int inputNeuronsCount, int hiddenNeuronsCount, int contextNeuronsCount, int outputNeuronsCount) {

                // create input layer
                InputLayer inputLayer = new InputLayer(inputNeuronsCount);
                inputLayer.addNeuron(new BiasNeuron());
                addLayer(inputLayer);
                
		NeuronProperties neuronProperties = new NeuronProperties();
               // neuronProperties.setProperty("useBias", true);
		neuronProperties.setProperty("transferFunction", TransferFunctionType.SIGMOID);                
            
                Layer hiddenLayer = new Layer(hiddenNeuronsCount, neuronProperties); 
                hiddenLayer.addNeuron(new BiasNeuron());
                addLayer(hiddenLayer);
                
                ConnectionFactory.fullConnect(inputLayer, hiddenLayer);
                
                Layer contextLayer = new Layer(contextNeuronsCount, neuronProperties); 
                addLayer(contextLayer); // we might also need bias for context neurons?
                                                                               
                Layer outputLayer = new Layer(outputNeuronsCount, neuronProperties); 
                addLayer(outputLayer);
                
                ConnectionFactory.fullConnect(hiddenLayer, outputLayer);
                
                ConnectionFactory.forwardConnect(hiddenLayer, contextLayer); // forward or full connectivity?
                ConnectionFactory.fullConnect(contextLayer, hiddenLayer);
                
                                
		// set input and output cells for network
                  NeuralNetworkFactory.setDefaultIO(this);

                  // set learnng rule
		this.setLearningRule(new BackPropagation());
				
	}
    
}
