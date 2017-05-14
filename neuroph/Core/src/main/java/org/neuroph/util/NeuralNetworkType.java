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

/**
 * Contains neural network types and labels.
 */
public enum NeuralNetworkType {
	ADALINE("Adaline"),
	PERCEPTRON("Perceptron"),
	MULTI_LAYER_PERCEPTRON("Multi Layer Perceptron"),
	HOPFIELD("Hopfield"),
	KOHONEN("Kohonen"),
	NEURO_FUZZY_REASONER("Neuro Fuzzy Reasoner"),
	SUPERVISED_HEBBIAN_NET("Supervised Hebbian network"),
	UNSUPERVISED_HEBBIAN_NET("Unsupervised Hebbian network"),
	COMPETITIVE("Competitive"),
	MAXNET("Maxnet"),
	INSTAR("Instar"),
	OUTSTAR("Outstar"),
	RBF_NETWORK("RBF Network"),
	BAM("BAM"),
        BOLTZMAN("Boltzman"),
        COUNTERPROPAGATION("CounterPropagation"),
        INSTAR_OUTSTAR("InstarOutstar"),
        PCA_NETWORK("PCANetwork"),
	RECOMMENDER("Recommender");

	private String typeLabel;
	
	private NeuralNetworkType(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	
	public String getTypeLabel() {
		return typeLabel;
	}
}
