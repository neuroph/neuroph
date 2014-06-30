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

package org.neuroph.contrib;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.learning.UnsupervisedHebbianLearning;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 * E-commerce recommender neural network based on hebbian learning.
 * Still under development, mostly hard-coded for now
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class RecommenderNetwork extends NeuralNetwork {
	private static final long serialVersionUID = 1L;
	
	int inputLayerIdx = 0,
		typeLayerIdx = 1,
		brandLayerIdx = 2,
		priceLayerIdx = 3,
		promoLayerIdx = 4,
		outputLayerIdx = 5;

	public RecommenderNetwork() {
//            for (int i = 0; i < 6; i++) {
//                this.addLayer(new Layer());
//            }
           // this.createDemoNetwork();
	}	
	
	public void createDemoNetwork() {
		int productsCount = 20;
		int typesCount = 3;
		int brandsCount = 3;
		int priceCount = 3;
		int promoCount = 3;
				
		this.setNetworkType(NeuralNetworkType.RECOMMENDER);
		//this.getLayers().clear();
		// init neuron settings for this type of network
		NeuronProperties neuronProperties = new NeuronProperties();
		neuronProperties.setProperty("transferFunction", TransferFunctionType.RAMP);
		// for sigmoid and tanh transfer functions
		neuronProperties.setProperty("transferFunction.slope", new Double(1)); 
		
		// create input layer		
		Layer inputLayer = LayerFactory.createLayer(productsCount,
				neuronProperties);
		this.addLayer(inputLayer);
		createProductLabels(inputLayer);

		
		// create product types layer		
		Layer typeLayer = LayerFactory.createLayer(typesCount,
				neuronProperties);
		createTypeLabels(typeLayer);
		this.addLayer(typeLayer);
		
		
		// create brands layer		
		Layer brandLayer = LayerFactory.createLayer(brandsCount,
				neuronProperties);
		createBrandLabels(brandLayer);
		this.addLayer(brandLayer);	
		
		
		// create price layer		
		Layer priceLayer = LayerFactory.createLayer(priceCount, neuronProperties);
		createPriceLabels(priceLayer);
		this.addLayer(priceLayer);	
		
		// create price layer		
		Layer promoLayer = LayerFactory.createLayer(promoCount, neuronProperties);
		createPromoLabels(promoLayer);
		this.addLayer(promoLayer);		
				
		// create output layer
		Layer outputLayer = LayerFactory.createLayer(productsCount,
				neuronProperties);
		this.addLayer(outputLayer);
		createProductLabels(outputLayer);
		
		createTypeConnections();
		createBrandConnections();
		createPriceConnections();
		createPromoConnections();

        
		// create reccurent self connections in output layer
		for(Neuron neuron : this.getLayerAt(outputLayerIdx).getNeurons()) {
			neuron.addInputConnection(neuron, 1);
		}
		
		// set input and output cells for this network
		NeuralNetworkFactory.setDefaultIO(this);	
		
		// dont learn the self connections
		// moze cak i posle svakog prolaza da se primenjuje hebbianovo pravilo a ne samo nakon kupovine
		// napravi vise varijanti
		// ako kupuje onda moze da se primenjje winner takes all hebbian learning
		this.setLearningRule(new UnsupervisedHebbianLearning());
	}
	
	private void createTypeConnections() {
		// input connections for the first product type
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(i);
			Neuron toNeuron = this.getLayerAt(typeLayerIdx).getNeuronAt(0);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// input connections for the second product type
		for(int i = 7; i < 14; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(i);
			Neuron toNeuron = this.getLayerAt(typeLayerIdx).getNeuronAt(1);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// input connections for the third product type
		for(int i = 14; i < 20; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(i);
			Neuron toNeuron = this.getLayerAt(typeLayerIdx).getNeuronAt(2);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}		
		
		
		// output connections for the first product type
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(typeLayerIdx).getNeuronAt(0);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(i);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the second product type
		for(int i = 7; i < 14; i++) {
			Neuron fromNeuron = this.getLayerAt(typeLayerIdx).getNeuronAt(1);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(i);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the third product type
		for(int i = 14; i < 20; i++) {
			Neuron fromNeuron = this.getLayerAt(typeLayerIdx).getNeuronAt(2);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(i);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}			
	}
	
	private void createBrandConnections() {
		int samsung[] = {0, 1, 7, 8, 9, 14, 15};
		int lg[] = {2, 3, 10, 11, 16, 17};
		int sony[] = {4, 5, 6, 12, 13, 18, 19};
		
		// create samsung input sonnections
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(samsung[i]);
			Neuron toNeuron = this.getLayerAt(brandLayerIdx).getNeuronAt(0);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// create input connections for LG
		for(int i = 0; i < 6; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(lg[i]);
			Neuron toNeuron = this.getLayerAt(brandLayerIdx).getNeuronAt(1);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// create input connections for sony
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(sony[i]);
			Neuron toNeuron = this.getLayerAt(brandLayerIdx).getNeuronAt(2);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}		
		
		
		// output connections for the first brand
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(brandLayerIdx).getNeuronAt(0);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(samsung[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the second brand
		for(int i = 0; i < 6; i++) {
			Neuron fromNeuron = this.getLayerAt(brandLayerIdx).getNeuronAt(1);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(lg[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the third brand
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(brandLayerIdx).getNeuronAt(2);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(sony[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}			
	}	
	
	private void createPriceConnections() {
		int low[] = {0, 2, 4, 7, 10, 16, 18};
		int mid[] = {3, 5, 8, 11, 12, 14, 19};
		int high[] = {1, 6, 9, 13, 15, 17};		
		
		// input connections for the first price class
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(low[i]);
			Neuron toNeuron = this.getLayerAt(priceLayerIdx).getNeuronAt(0);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// input connections for the second price class
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(mid[i]);
			Neuron toNeuron = this.getLayerAt(priceLayerIdx).getNeuronAt(1);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// input connections for the third price class
		for(int i = 0; i < 6; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(high[i]);
			Neuron toNeuron = this.getLayerAt(priceLayerIdx).getNeuronAt(2);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}		
		
		
		// output connections for the first price class
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(priceLayerIdx).getNeuronAt(0);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(low[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the second price class
		for(int i = 0; i < 7; i++) {
			Neuron fromNeuron = this.getLayerAt(priceLayerIdx).getNeuronAt(1);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(mid[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the third price class
		for(int i = 0; i < 6; i++) {
			Neuron fromNeuron = this.getLayerAt(priceLayerIdx).getNeuronAt(2);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(high[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}			
	}		
	
	private void createPromoConnections() {
		
		int sales[] = {0, 10, 19};
		int new_products[] = {6, 9};
		int bestsellers[] = {3, 12, 14};		
		
		// input connections for the first promo type
		for(int i = 0; i < sales.length; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(sales[i]);
			Neuron toNeuron = this.getLayerAt(promoLayerIdx).getNeuronAt(0);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// input connections for the second promo type
		for(int i = 0; i < new_products.length; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(new_products[i]);
			Neuron toNeuron = this.getLayerAt(promoLayerIdx).getNeuronAt(1);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// input connections for the third promo type
		for(int i = 0; i < bestsellers.length; i++) {
			Neuron fromNeuron = this.getLayerAt(inputLayerIdx).getNeuronAt(bestsellers[i]);
			Neuron toNeuron = this.getLayerAt(promoLayerIdx).getNeuronAt(2);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}		

		// output connections for the first promo type
		for(int i = 0; i < sales.length; i++) {
			Neuron fromNeuron = this.getLayerAt(promoLayerIdx).getNeuronAt(0);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(sales[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the second promo type
		for(int i = 0; i < new_products.length; i++) {
			Neuron fromNeuron = this.getLayerAt(promoLayerIdx).getNeuronAt(1);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(new_products[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}
		
		// output connections for the third promo type
		for(int i = 0; i < bestsellers.length; i++) {
			Neuron fromNeuron = this.getLayerAt(promoLayerIdx).getNeuronAt(2);
			Neuron toNeuron = this.getLayerAt(outputLayerIdx).getNeuronAt(bestsellers[i]);
			this.createConnection(fromNeuron, toNeuron, 0.1);
		}			
	}	
	
	private void createProductLabels(Layer layer) {
		layer.getNeuronAt(0).setLabel("Samsung LCD TV LE-32A330");
		layer.getNeuronAt(1).setLabel("Samsung LCD TV LE-32A558");
		layer.getNeuronAt(2).setLabel("LG LCD TV 32LG2000");
		layer.getNeuronAt(3).setLabel("LG LCD TV 32LG5010");
		layer.getNeuronAt(4).setLabel("Sony LCD TV KDL-32L4000K");
		layer.getNeuronAt(5).setLabel("Sony LCD TV KDL-32S4000");
		layer.getNeuronAt(6).setLabel("Sony LCD TV KDL-32W4000K");
		layer.getNeuronAt(7).setLabel("Samsung Digital Camera S760");
		layer.getNeuronAt(8).setLabel("Samsung Digital Camera L100");
		layer.getNeuronAt(9).setLabel("Samsung Digital Camera S850");
		layer.getNeuronAt(10).setLabel("LG Digital Camera DMCLS80E");
		layer.getNeuronAt(11).setLabel("LG Digital Camera DMCLZ8E");
		layer.getNeuronAt(12).setLabel("Sony Digital Camera DSCW120S");
		layer.getNeuronAt(13).setLabel("Sony Digital Camera DSCW130S");
		layer.getNeuronAt(14).setLabel("Samsung Mobile Phone E251");
		layer.getNeuronAt(15).setLabel("Samsung Mobile Phone U600");
		layer.getNeuronAt(16).setLabel("Sony Mobile Phone KP100");
		layer.getNeuronAt(17).setLabel("Sony Mobile Phone KE850");
		layer.getNeuronAt(18).setLabel("LG Mobile Phone K330");
		layer.getNeuronAt(19).setLabel("LG Mobile Phone K660");
	}	
	
	private void createTypeLabels(Layer layer) {
		layer.getNeuronAt(0).setLabel("LCD TV");
		layer.getNeuronAt(1).setLabel("Digital Camera");
		layer.getNeuronAt(2).setLabel("Mobile Phone");
	}
	
	private void createBrandLabels(Layer layer) {	
		layer.getNeuronAt(0).setLabel("Samsung");
		layer.getNeuronAt(1).setLabel("LG");
		layer.getNeuronAt(2).setLabel("Sony");
	}	
	
	private void createPriceLabels(Layer layer) {
		layer.getNeuronAt(0).setLabel("Low Price");
		layer.getNeuronAt(1).setLabel("Mid Price");
		layer.getNeuronAt(2).setLabel("High Price");
	}	
	
	private void createPromoLabels(Layer layer) {
		layer.getNeuronAt(0).setLabel("Sales");
		layer.getNeuronAt(1).setLabel("New");
		layer.getNeuronAt(2).setLabel("Bestseller");
	}	
	
	
}
