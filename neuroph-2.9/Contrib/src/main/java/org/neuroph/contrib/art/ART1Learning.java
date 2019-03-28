package org.neuroph.contrib.art;

import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.UnsupervisedLearning;
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.nnet.comp.neuron.CompetitiveNeuron;

/**
 *
 * @author ja
 */
public class ART1Learning extends UnsupervisedLearning {

    /**
     *
     */
    public ART1Learning() {
		super();
    }

	/**
	 * This method does one learning epoch for the unsupervised learning rules.
	 * It iterates through the training set and trains network weights for each
	 * element. The learning stops after one epoch.
	 *
	 * @param trainingSet
	 *            training set for training network
	 */
	@Override
	public void doLearningEpoch(DataSet trainingSet) {
		super.doLearningEpoch(trainingSet);
		stopLearning(); // stop learning after one learning epoch - because we don't have any stopping criteria  for unsupervised learning...
	}

	/**
	 * Adjusts weights for the winning neuron
	 */
        @Override
	protected void updateNetworkWeights() {
		// find active neuron in output layer

		CompetitiveNeuron winningNeuron = ((CompetitiveLayer) neuralNetwork
				.getLayerAt(1)).getWinner();

		List<Connection> inputConnections = winningNeuron.getConnectionsFromOtherLayers();

		for(Connection connection : inputConnections) {
			double weight = connection.getWeight().getValue();
			double input = connection.getInput();
			double deltaWeight = this.learningRate * (input - weight);
			connection.getWeight().inc(deltaWeight);
		}
	}

}
