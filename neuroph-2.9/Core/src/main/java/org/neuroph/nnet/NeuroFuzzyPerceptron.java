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

package org.neuroph.nnet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.Min;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.Trapezoid;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * The NeuroFuzzyReasoner class represents Neuro Fuzzy Reasoner architecture.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class NeuroFuzzyPerceptron extends NeuralNetwork {
	
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	public NeuroFuzzyPerceptron(double[][] pointsSets, double[][] timeSets) {
		List<Integer> inputSets = new ArrayList<>();
		inputSets.add(Integer.valueOf(4));
		inputSets.add(Integer.valueOf(3));

		this.createStudentNFR(2, inputSets, 4, pointsSets, timeSets);
	}

	public NeuroFuzzyPerceptron(int inputNum, Vector<Integer> inputSets, int outNum) {
		this.createNetwork(inputNum, inputSets, outNum);
	}

	// build example network for student classification
	private void createStudentNFR(int inputNum, List<Integer> inputSets, int outNum,
			double[][] pointsSets, double[][] timeSets) {

		// set network type
		this.setNetworkType(NeuralNetworkType.NEURO_FUZZY_REASONER);

		// createLayer input layer
		NeuronProperties neuronProperties = new NeuronProperties();
		Layer inLayer = LayerFactory.createLayer(inputNum, neuronProperties);
		this.addLayer(inLayer);

		// createLayer fuzzy set layer
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.TRAPEZOID);
		Iterator<Integer> e = inputSets.iterator();
		int fuzzySetsNum = 0;
		while (e.hasNext()) {
			Integer i = e.next();
			fuzzySetsNum = fuzzySetsNum + i.intValue();
		}
		Layer setLayer = LayerFactory.createLayer(fuzzySetsNum,
				neuronProperties);
		this.addLayer(setLayer);

		// TODO: postavi parametre funkcija pripadnosti
		// nizove sa trning elementima iznesi van klase i prosledjuj ih kao
		// parametre
//		Iterator<Neuron> ii = setLayer.getNeuronsIterator();
		Iterator<Integer> en;// =setLayer.neurons();
		int c = 0;
                for(Neuron cell : setLayer.getNeurons()) {
//		while (ii.hasNext()) {
//			Neuron cell = ii.next();
			Trapezoid tf = (Trapezoid) cell.getTransferFunction();

			if (c <= 3) {
				tf.setLeftLow(pointsSets[c][0]);
				tf.setLeftHigh(pointsSets[c][1]);
				tf.setRightLow(pointsSets[c][3]);
				tf.setRightHigh(pointsSets[c][2]);
			} else {
				tf.setLeftLow(timeSets[c - 4][0]);
				tf.setLeftHigh(timeSets[c - 4][1]);
				tf.setRightLow(timeSets[c - 4][3]);
				tf.setRightHigh(timeSets[c - 4][2]);
			}
			c++;
		}

		// povezi prvi i drugi sloj
		int s = 0; // brojac celija sloja skupova (fazifikacije)
		for (int i = 0; i < inputNum; i++) { // brojac ulaznih celija
			Neuron from = inLayer.getNeuronAt(i);
			int jmax = inputSets.get(i).intValue();
			for (int j = 0; j < jmax; j++) {
				Neuron to = setLayer.getNeuronAt(s);
				ConnectionFactory.createConnection(from, to, 1);
				s++;
			}
		}

		// ----------------------------------------------------------

		// createLayer rules layer
		NeuronProperties ruleNeuronProperties = new NeuronProperties(
                        Neuron.class,
                        WeightedSum.class,
                        Linear.class);
		en = inputSets.iterator();
		int fuzzyAntNum = 1;
		while (en.hasNext()) {
			Integer i = en.next();
			fuzzyAntNum = fuzzyAntNum * i.intValue();
		}
		Layer ruleLayer = LayerFactory.createLayer(fuzzyAntNum,
				ruleNeuronProperties);
		this.addLayer(ruleLayer);

		int scIdx = 0; // set cell index

		for (int i = 0; i < inputNum; i++) { // brojac ulaza (grupa fuzzy
												// skupova)
			int setsNum = inputSets.get(i).intValue();

			for (int si = 0; si < setsNum; si++) { // brojac celija fuzzy
													// skupova
				if (i == 0) {
					Neuron from = setLayer.getNeuronAt(si);
					int connPerCell = fuzzyAntNum / setsNum;
					scIdx = si;

					for (int k = 0; k < connPerCell; k++) { // brojac celija
															// hipoteza
						Neuron to = ruleLayer.getNeuronAt(si * connPerCell + k);
						ConnectionFactory.createConnection(from, to, 1);
					} // for
				} // if
				else {
					scIdx++;
					Neuron from = setLayer.getNeuronAt(scIdx);
					int connPerCell = fuzzyAntNum / setsNum;

					for (int k = 0; k < connPerCell; k++) { // brojac celija
															// hipoteza
						int toIdx = si + k * setsNum;
						Neuron to = ruleLayer.getNeuronAt(toIdx);
						ConnectionFactory.createConnection(from, to, 1);
					} // for k
				} // else
			} // for si
		} // for i

		// kreiraj izlazni sloj
		neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.STEP);
		Layer outLayer = LayerFactory.createLayer(outNum, neuronProperties);
		this.addLayer(outLayer);

		ConnectionFactory.fullConnect(ruleLayer, outLayer);

		// inicijalizuj ulazne i izlazne celije
		NeuralNetworkFactory.setDefaultIO(this);

		this.setLearningRule(new LMS());
	}

	/**
	 * Creates custom NFR architecture
	 * 
	 * @param inputNum
	 *            number of getInputsIterator
	 * @param inputSets
	 *            input fuzzy sets
	 * @param outNum
	 *            number of outputs
	 */
	private void createNetwork(int inputNum, Vector<Integer> inputSets, int outNum) {

		// set network type
		this.setNetworkType(NeuralNetworkType.NEURO_FUZZY_REASONER);

		// CREATE INPUT LAYER
		NeuronProperties neuronProperties = new NeuronProperties();
		Layer inLayer = LayerFactory.createLayer(inputNum, neuronProperties);
		this.addLayer(inLayer);

		// CREATE FUZZY SET LAYER
		neuronProperties.setProperty("transferFunction",
				TransferFunctionType.TRAPEZOID);
		Enumeration<Integer> e = inputSets.elements();
		int fuzzySetsNum = 0;
		while (e.hasMoreElements()) {
			Integer i = e.nextElement();
			fuzzySetsNum = fuzzySetsNum + i.intValue();
		}
		Layer setLayer = LayerFactory.createLayer(fuzzySetsNum, neuronProperties);
		this.addLayer(setLayer);

		// TODO: postavi parametre funkcija pripadnosti
		// nizove sa trning elementima iznesi van klase i prosledjuj ih kao
		// parametre
//		Iterator<Neuron> ii = setLayer.getNeuronsIterator();
		Enumeration<Integer> en;// =setLayer.neurons();
		int c = 0;
                for(Neuron cell : setLayer.getNeurons()) {                
//		while (ii.hasNext()) {
//			Neuron cell = ii.next();
			Trapezoid tf = (Trapezoid) cell.getTransferFunction();
			/*
			 * if (c<=3) { tf.setLeftLow(pointsSets[c][0]);
			 * tf.setLeftHigh(pointsSets[c][1]); tf.setRightLow(pointsSets[c][3]);
			 * tf.setRightHigh(pointsSets[c][2]); } else { tf.setLeftLow(timeSets[c-4][0]);
			 * tf.setLeftHigh(timeSets[c-4][1]); tf.setRightLow(timeSets[c-4][3]);
			 * tf.setRightHigh(timeSets[c-4][2]); } c++;
			 */
		}

		// createLayer connections between input and fuzzy set getLayersIterator
		int s = 0; // brojac celija sloja skupova (fazifikacije)
		for (int i = 0; i < inputNum; i++) { // brojac ulaznih celija
			Neuron from = inLayer.getNeuronAt(i);
			int jmax = inputSets.elementAt(i).intValue();
			for (int j = 0; j < jmax; j++) {
				Neuron to = setLayer.getNeuronAt(s);
				ConnectionFactory.createConnection(from, to, 1);
				s++;
			}
		}

		// ----------------------------------------------------------

		// kreiraj sloj pravila
		neuronProperties.setProperty("inputFunction", Min.class);
		neuronProperties.setProperty("transferFunction",
				Linear.class);
		en = inputSets.elements();
		int fuzzyAntNum = 1;
		while (en.hasMoreElements()) {
			Integer i = en.nextElement();
			fuzzyAntNum = fuzzyAntNum * i.intValue();
		}
		Layer ruleLayer = LayerFactory.createLayer(fuzzyAntNum, neuronProperties);
		this.addLayer(ruleLayer);

		// povezi set i rule layer

		int scIdx = 0; // set cell index

		for (int i = 0; i < inputNum; i++) { // brojac ulaza (grupa fuzzy
												// skupova)
			int setsNum = inputSets.elementAt(i).intValue();

			for (int si = 0; si < setsNum; si++) { // brojac celija fuzzy
													// skupova
				if (i == 0) {
					Neuron from = setLayer.getNeuronAt(si);
					int connPerCell = fuzzyAntNum / setsNum;
					scIdx = si;

					for (int k = 0; k < connPerCell; k++) { // brojac celija
															// hipoteza
						Neuron to = ruleLayer.getNeuronAt(si * connPerCell + k);
						ConnectionFactory.createConnection(from, to, 1);
					} // for
				} // if
				else {
					scIdx++;
					Neuron from = setLayer.getNeuronAt(scIdx);
					int connPerCell = fuzzyAntNum / setsNum;

					for (int k = 0; k < connPerCell; k++) { // brojac celija
															// hipoteza
						int toIdx = si + k * setsNum;
						Neuron to = ruleLayer.getNeuronAt(toIdx);
						ConnectionFactory.createConnection(from, to, 1);
					} // for k
				} // else
			} // for si
		} // for i

		// set input and output cells for this network
		neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
		Layer outLayer = LayerFactory.createLayer(outNum, neuronProperties);
		this.addLayer(outLayer);

		ConnectionFactory.fullConnect(ruleLayer, outLayer);

		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);

		this.setLearningRule(new LMS());
	}

}
