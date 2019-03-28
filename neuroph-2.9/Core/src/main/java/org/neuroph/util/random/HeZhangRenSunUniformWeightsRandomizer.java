package org.neuroph.util.random;

import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;

/**
 * Sources:
 *   https://arxiv.org/abs/1502.01852     Delving Deep into Rectifiers: Surpassing Human-Level Performance on ImageNet Classification
 *   https://stats.stackexchange.com/questions/47590/what-are-good-initial-weights-in-a-neural-network
 *   https://github.com/keras-team/keras/blob/master/keras/initializers.py
 *   
 * @author Jon Tait
 */
public class HeZhangRenSunUniformWeightsRandomizer extends WeightsRandomizer {


    /**
     * "He" uniform distribution [-limit, limit] where limit is 3 * sqrt(2 / fan in)
     * 
     * @param neuron neuron to randomize
     */
    protected void randomize(Neuron neuron) {
        final int numberOfInputConnections = neuron.getInputConnections().size();
        if(numberOfInputConnections == 0) return;
        final double stddev = Math.sqrt(2D / numberOfInputConnections);
        final double limit = 3 * stddev;
        for (Connection connection : neuron.getInputConnections()) {
        	final double randomNumber = randomGen.nextDouble(); // NOTE: this isn't perfect since the value can be 0 but not 1 so we don't quite have the desired range!
        	final double newRandomWeight = (2 * limit * randomNumber) - limit;
			connection.getWeight().setValue(newRandomWeight);
        }
    }
}
