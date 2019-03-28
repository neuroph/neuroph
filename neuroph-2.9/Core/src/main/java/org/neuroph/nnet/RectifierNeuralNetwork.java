package org.neuroph.nnet;

import java.util.List;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.RectifiedLinear;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.nnet.comp.neuron.InputNeuron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.random.HeZhangRenSunUniformWeightsRandomizer;

public class RectifierNeuralNetwork extends NeuralNetwork<BackPropagation> {
	private static final long serialVersionUID = 1L;

	public RectifierNeuralNetwork(List<Integer> neuronsInLayers) {
		//this.setNetworkType(NeuralNetworkType.RECTIFIER);

		NeuronProperties inputNeuronProperties = new NeuronProperties(InputNeuron.class, Linear.class);
        Layer layer = LayerFactory.createLayer(neuronsInLayers.get(0), inputNeuronProperties);

        this.addLayer(layer);

        // create layers
        Layer prevLayer = layer;

        for (int layerIdx = 1; layerIdx < neuronsInLayers.size()-1; layerIdx++) {
            Integer neuronsNum = neuronsInLayers.get(layerIdx);
            layer = LayerFactory.createLayer(neuronsNum, RectifiedLinear.class);

            this.addLayer(layer);
            ConnectionFactory.fullConnect(prevLayer, layer);

            prevLayer = layer;
        }

        int numberOfOutputNeurons = neuronsInLayers.get(neuronsInLayers.size() - 1);
        Layer outputLayer = LayerFactory.createLayer(numberOfOutputNeurons, Sigmoid.class);
        this.addLayer(outputLayer);
        ConnectionFactory.fullConnect(prevLayer, outputLayer);

        NeuralNetworkFactory.setDefaultIO(this); // set input and output cells for network
        this.setLearningRule(new MomentumBackpropagation());
        this.randomizeWeights(new HeZhangRenSunUniformWeightsRandomizer());
	}

}
