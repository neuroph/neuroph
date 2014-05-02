package org.neuroph.contrib;

import java.util.ArrayList;
import java.util.List;

import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.comp.neuron.InputNeuron;

public class CNNTestUtil {

	public static List<Neuron> createNeurons(int numberOfNeurons, double input) {
		List<Neuron> neurons = new ArrayList<Neuron>();
		for (int i = 0; i < numberOfNeurons; i++) {
			Neuron inputNeuron = new Neuron();
			inputNeuron.setInput(input);
			neurons.add(inputNeuron);
		}
		return neurons;
	}

	public static List<InputNeuron> createInputNeurons(int numberOfNeurons, double input) {
		List<InputNeuron> neurons = new ArrayList<InputNeuron>();
		for (int i = 0; i < numberOfNeurons; i++) {
			InputNeuron inputNeuron = new InputNeuron();
			inputNeuron.setInput(input);
			neurons.add(inputNeuron);
		}
		return neurons;
	}

	public static void fillFeatureMapWithNeurons(Layer2D featureMap, double neuronValue) {
		int numberOfNeurons = featureMap.getWidth() * featureMap.getHeight();
		List<Neuron> neurons = CNNTestUtil.createNeurons(numberOfNeurons, neuronValue);
		for (Neuron neuron : neurons) {
			featureMap.addNeuron(neuron);
		}
	}

	public static void fillFeatureMapWithNeurons(Layer2D featureMap) {
		fillFeatureMapWithNeurons(featureMap, 0);
	}

}
