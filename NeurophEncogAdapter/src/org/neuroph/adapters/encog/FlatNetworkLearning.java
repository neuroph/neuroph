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
package org.neuroph.adapters.encog;

import java.io.Serializable;
import org.encog.EncogError;
import org.encog.neural.flat.FlatNetwork;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.flat.FlatLearningType;

/**
 * Learning rule for flat networks.  The learning type is specified by the the learningType
 * property.  Currently the flat networks can be trained in the following three ways.
 * 
 * Classic momentum based propagation.  The learning rate and momentum are specified by using the
 * setLearningRate and setMomentum on the FlatNetworkLearning class.
 * 
 * Resilient Propagation.  No parameters are needed for this learning method.  RPROP is the best
 * general purpose training method.
 *
 * Manhattan update rule.  Not at all a good general purpose learning method, only useful in 
 * some situations.  A learning rate must be specified by using the setLearningRate method on
 * the FlatNetworkLearning class.
 *
 * 
 * @author Jeff Heaton (http://www.jeffheaton.com)
 *
 * @see FlatNetworkPlugin
 */
public class FlatNetworkLearning extends SupervisedLearning implements Serializable {



	/**
	 * The object serial id.
	 */
	
	private static final long serialVersionUID = 1L;
	/**
	 * The flat network to train.
	 */
	private FlatNetwork flat;
	
	/**
	 * The last training set used.  This property is used to tell if the training set has 
	 * changed since the last iteration.
	 */
	private transient EngineIndexableSet lastTrainingSet;
	
	/**
	 * The last type of learning used.  This property is used to tell if the training set has
	 * changed since the last iteration.
	 */
	private FlatLearningType lastLearningType;
	
	/**
	 * The flat trainer that is in use.
	 */
	private transient TrainFlatNetwork training;
	
	/**
	 * The learning rate.  This value is used for backprop and Manhattan.
	 */
	private double learningRate;
	
	/**
	 * The momentum.  This value is used for backpropagation training.
	 */
	private double momentum;
	
	/**
	 * The number of threads to use.  The default, zero, specifies to use the processor count
	 * to determine an optimal number of threads.
	 */
	private int numThreads;
	
	/**
	 * The type of flat learning to be used.
	 */
	private FlatLearningType learningType;

	/**
	 * Constructor, create a flat network learning rule from the specified flat network.
	 * @param flat The flat network.
	 */
	public FlatNetworkLearning(FlatNetwork flat) {
		init(flat);
	}

	/**
	 * Constructor, create a flat network learning rule from a Neuroph network.  This neuroph
	 * network must have already had the flat network plugin installed.
	 * @param network The neuroph network to use.
	 */
	public FlatNetworkLearning(NeuralNetwork network) {
		FlatNetworkPlugin plugin = (FlatNetworkPlugin) network
				.getPlugin(FlatNetworkPlugin.class);
		FlatNetwork flat = plugin.getFlatNetwork();
		if (this.flat == null)
			throw new EncogError(
					"This learning rule only works with a network that has a FlatNetworkPlugin attached.");
		init(flat);
	}
	
	/**
	 * Internal method used to setup the learning rule.
	 * @param flat The network that will be trained.
	 */
	private void init(FlatNetwork flat)
	{
		this.flat = flat;
		this.learningType = FlatLearningType.ResilientPropagation;
		this.learningRate = 0.7;
		this.momentum = 0.3;
	}

	/**
	 * This method is not used.
	 * @param patternError Not used.
	 */
	@Override
	protected void updateNetworkWeights(double[] patternError) {
		throw new EncogError(
				"Method (updateNetworkWeights) is unimplemented and should not have been called.");

	}

	/**
	 * Perform one learning epoch.
	 * @param trainingSet The training set to use.
	 */
	@Override
	public void doLearningEpoch(DataSet trainingSet) {

		this.previousEpochError = this.totalNetworkError;

		// have we changed learning types, or training sets?  If so, create a whole new trainer.
		if (this.lastLearningType != this.learningType || this.lastTrainingSet != trainingSet) {
			
			this.lastTrainingSet = trainingSet;
			
			switch( this.learningType )
			{
				case ResilientPropagation:
					this.training = new TrainFlatNetworkResilient(this.flat,
							this.lastTrainingSet);
					break;
					
				case ManhattanUpdateRule:
					this.training = new TrainFlatNetworkManhattan(this.flat,
							this.lastTrainingSet, this.learningRate);
					break;
					
				case BackPropagation:
					this.training = new TrainFlatNetworkBackPropagation(this.flat,
							this.lastTrainingSet, this.getLearningRate(), this.getMomentum());					
					break;
			}
			
			this.training.setNumThreads(this.numThreads);
			
			// remember the last state of the learning type and trianing set so that we can 
			// tell if this changes in the next iteration.
			this.lastLearningType = this.learningType;
			this.lastTrainingSet = trainingSet;
		}
		

		// perform a training iteration
		this.training.iteration();
		this.totalNetworkError = this.training.getError();

		// should Neuroph training stop
		if (hasReachedStopCondition()) {
			stopLearning();
		}
	}

	/**
	 * @return The learning rate.  This value is used for Backprop and Manhattan training.
	 */
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Set the learning rate.  This value is used for Backprop and Manhattan training.
	 */
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/**
	 * @return The momentum, this is is used for Backprop.
	 */
	public double getMomentum() {
		return momentum;
	}

	/**
	 * Set the momentum, this is used for Backprop.
	 * @param momentum
	 */
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	/**
	 * @return The number of threads to use.  Zero requests that the learning 
	 * rule choose a number of processors.
	 */
	public int getNumThreads() {
		return numThreads;
	}

	/**
	 * Set the number of threads to use.
	 * @param numThreads
	 */
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}

	/**
	 * @return The type of learning to use.
	 */
	public FlatLearningType getLearningType() {
		return learningType;
	}

	/**
	 * Set the type of learning to use.
	 * @param learningType The type of learning to use.
	 */
	public void setLearningType(FlatLearningType learningType) {
		this.learningType = learningType;
	}

        @Override
        protected void addToSquaredErrorSum(double[] outputError) {
            throw new EncogError("Not supported, and should not be called.");
        }
}
