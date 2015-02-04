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
package org.neuroph.core.learning;

import java.io.Serializable;
import java.util.Iterator;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.ErrorFunction;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.core.learning.stop.MaxErrorStop;
import org.neuroph.core.learning.stop.StopCondition;

// TODO:  random pattern order

/**
 * Base class for all supervised learning algorithms.
 * It extends IterativeLearning, and provides general supervised learning principles.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class SupervisedLearning extends IterativeLearning implements
        Serializable {

    /**
     * The class fingerprint that is set to indicate serialization
     * compatibility with a previous version of the class
     */
    private static final long serialVersionUID = 3L;
    /**
     * Total network error in previous epoch
     */
    protected transient double previousEpochError;
    /**
     * Max allowed network error (condition to stop learning)
     */
    protected double maxError = 0.01d;
    /**
     * Stopping condition: training stops if total network error change is smaller than minErrorChange
     * for minErrorChangeIterationsLimit number of iterations
     */
    private double minErrorChange = Double.POSITIVE_INFINITY;
    /**
     * Stopping condition: training stops if total network error change is smaller than minErrorChange
     * for minErrorChangeStopIterations number of iterations
     */
    private int minErrorChangeIterationsLimit = Integer.MAX_VALUE;
    /**
     * Count iterations where error change is smaller then minErrorChange
     */
    private transient int minErrorChangeIterationsCount;
    /**
     * Setting to determine if learning (weights update) is in batch mode
     * False by default.
     */
    private boolean batchMode = false;

    private ErrorFunction errorFunction;

    /**
     * Creates new supervised learning rule
     */
    public SupervisedLearning() {
        super();
        this.errorFunction = new MeanSquaredError();
        // create stop condition structure based on settings               
        this.stopConditions.add(new MaxErrorStop(this));        
    }

    /**
     * Trains network for the specified training set and maxError
     *
     * @param trainingSet training set to learn
     * @param maxError    learning stop condition. If maxError is reached learning stops
     */
    public void learn(DataSet trainingSet, double maxError) {
        this.maxError = maxError;
        this.learn(trainingSet);
    }

    /**
     * Trains network for the specified training set, maxError and number of iterations
     *
     * @param trainingSet   training set to learn
     * @param maxError      learning stop condition. if maxError is reached learning stops
     * @param maxIterations maximum number of learning iterations
     */
    public void learn(DataSet trainingSet, double maxError, int maxIterations) {
        this.maxError = maxError;
        this.setMaxIterations(maxIterations);
        this.learn(trainingSet);
    }

    @Override
    protected void onStart() {
        super.onStart(); // reset iteration counter
        this.minErrorChangeIterationsCount = 0;
        this.previousEpochError = 0d;

        // this is now done in constructor
//        this.errorFunction = new MeanSquaredError();
//        // create stop condition structure based on settings               
//        this.stopConditions.add(new MaxErrorStop(this));
    }

    @Override
    protected void beforeEpoch() {
        this.previousEpochError = errorFunction.getTotalError();
        this.errorFunction.reset();
    }

    @Override
    protected void afterEpoch() {

        // calculate abs error change and count iterations if its below specified min error change (used for stop condition)
        double absErrorChange = Math.abs(previousEpochError - errorFunction.getTotalError());
        if (absErrorChange <= this.minErrorChange) {
            this.minErrorChangeIterationsCount++;
        } else {
            this.minErrorChangeIterationsCount = 0;
        }

        // if learning is performed in batch mode, apply accumulated weight changes from this epoch        
        if (this.batchMode == true) {
            doBatchWeightsUpdate();
        }
    }

    /**
     * This method implements basic logic for one learning epoch for the
     * supervised learning algorithms. Epoch is the one pass through the
     * training set. This method  iterates through the training set
     * and trains network for each element. It also sets flag if conditions
     * to stop learning has been reached: network error below some allowed
     * value, or maximum iteration count
     *
     * @param trainingSet training set for training network
     */
    @Override
    public void doLearningEpoch(DataSet trainingSet) {

        // feed network with all elements from training set
        Iterator<DataSetRow> iterator = trainingSet.iterator();
        while (iterator.hasNext() && !isStopped()) {
            DataSetRow dataSetRow = iterator.next();
            // learn current input/output pattern defined by SupervisedTrainingElement
            this.learnPattern(dataSetRow);
        }

        // calculate total network error as MSE. Use MSE so network does not grow with bigger training sets
        //this.totalNetworkError = errorFunction.getTotalError();
        
        // moved stopping condition to separate method hasReachedStopCondition() so it can be overriden / customized in subclasses
        // this condition is allready checked in IterativeLearning.learn(DataSet trainingSet)
//        if (hasReachedStopCondition()) {
//            stopLearning();
//        }
    }

    /**
     * Trains network with the input and desired output pattern from the specified training element
     *
     * @param trainingElement supervised training element which contains input and desired output
     */
    protected void learnPattern(DataSetRow trainingElement) {
        double[] input = trainingElement.getInput();
        this.neuralNetwork.setInput(input);
        this.neuralNetwork.calculate();
        double[] output = this.neuralNetwork.getOutput();
        double[] desiredOutput = trainingElement.getDesiredOutput();
        double[] patternError = errorFunction.calculatePatternError(output, desiredOutput);
        this.updateNetworkWeights(patternError);
    }

    /**
     * This method updates network weights in batch mode - use accumulated weights change stored in Weight.deltaWeight
     * It is executed after each learning epoch, only if learning is done in batch mode.
     *
     * @see SupervisedLearning#doLearningEpoch(org.neuroph.core.data.DataSet)
     */
    protected void doBatchWeightsUpdate() {
        // iterate layers from output to input
        Layer[] layers = neuralNetwork.getLayers();
        for (int i = neuralNetwork.getLayersCount() - 1; i > 0; i--) {
            // iterate neurons at each layer
            for (Neuron neuron : layers[i].getNeurons()) {
                // iterate connections/weights for each neuron
                for (Connection connection : neuron.getInputConnections()) {
                    // for each connection weight apply accumulated weight change
                    Weight weight = connection.getWeight();
                    weight.value += weight.weightChange; // apply delta weight which is the sum of delta weights in batch mode
                    weight.weightChange = 0; // reset deltaWeight
                }
            }
        }
    }


    /**
     * Returns true if stop condition has been reached, false otherwise.
     * Override this method in derived classes to implement custom stop criteria.
     *
     * @return true if stop condition is reached, false otherwise
     */
//    protected boolean hasReachedStopCondition() {
//        // da li ovd etreba staviti da proverava i da li se koristi ovaj uslov??? ili staviti da uslov bude automatski samo s ajaako malom vrednoscu za errorChange Doule.minvalue
//        return (this.totalNetworkError < this.maxError) || this.errorChangeStalled();
//      //  return stopConditions.isReached();
//    }

    /**
     * Returns true if absolute error change is sufficently small (<=minErrorChange) for minErrorChangeStopIterations number of iterations
     * @return true if absolute error change is stalled (error is sufficently small for some number of iterations)
     */
//    protected boolean errorChangeStalled() {
//        double absErrorChange = Math.abs(previousEpochError - totalNetworkError);
//
//        if (absErrorChange <= this.minErrorChange) {
//            this.minErrorChangeIterationsCount++;
//
//            if (this.minErrorChangeIterationsCount >= this.minErrorChangeIterationsLimit) {
//                return true;
//            }
//        } else {
//            this.minErrorChangeIterationsCount = 0;
//        }
//
//        return false;
//    }


//    public void addStopCondition(StopCondition stopCondition) {        
//        stopConditions.add(stopCondition);
//    }
    
    /**
     * Returns true if learning is performed in batch mode, false otherwise
     *
     * @return true if learning is performed in batch mode, false otherwise
     */
    public boolean isInBatchMode() {
        return batchMode;
    }

    /**
     * Sets batch mode on/off (true/false)
     *
     * @param batchMode batch mode setting
     */
    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    /**
     * Sets allowed network error, which indicates when to stopLearning training
     *
     * @param maxError network error
     */
    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }

    /**
     * Returns learning error tolerance - the value of total network error to stop learning.
     *
     * @return learning error tolerance
     */
    public double getMaxError() {
        return maxError;
    }

    /**
     * Returns total network error in previous learning epoch
     *
     * @return total network error in previous learning epoch
     */
    public double getPreviousEpochError() {
        return previousEpochError;
    }

    /**
     * Returns min error change stopping criteria
     *
     * @return min error change stopping criteria
     */
    public double getMinErrorChange() {
        return minErrorChange;
    }

    /**
     * Sets min error change stopping criteria
     *
     * @param minErrorChange value for min error change stopping criteria
     */
    public void setMinErrorChange(double minErrorChange) {
        this.minErrorChange = minErrorChange;
    }

    /**
     * Returns number of iterations for min error change stopping criteria
     *
     * @return number of iterations for min error change stopping criteria
     */
    public int getMinErrorChangeIterationsLimit() {
        return minErrorChangeIterationsLimit;
    }

    /**
     * Sets number of iterations for min error change stopping criteria
     *
     * @param minErrorChangeIterationsLimit number of iterations for min error change stopping criteria
     */
    public void setMinErrorChangeIterationsLimit(int minErrorChangeIterationsLimit) {
        this.minErrorChangeIterationsLimit = minErrorChangeIterationsLimit;
    }

    /**
     * Returns number of iterations count for for min error change stopping criteria
     *
     * @return number of iterations count for for min error change stopping criteria
     */
    public int getMinErrorChangeIterationsCount() {
        return minErrorChangeIterationsCount;
    }

    public ErrorFunction getErrorFunction() {
        return errorFunction;
    }

    public void setErrorFunction(ErrorFunction errorFunction) {
        this.errorFunction = errorFunction;
    }


    public double getTotalNetworkError() {
        return errorFunction.getTotalError();
    }

    /**
     * This method should implement the weights update procedure for the whole network
     * for the given output error vector.
     *
     * @param outputError output error vector for some network input (aka. patternError, network error)
     *                    usually the difference between desired and actual output
     */
    abstract protected void updateNetworkWeights(double[] outputError);
}
