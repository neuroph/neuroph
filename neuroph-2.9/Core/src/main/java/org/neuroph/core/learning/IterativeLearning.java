/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.core.learning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.stop.MaxIterationsStop;
import org.neuroph.core.learning.stop.StopCondition;

/**
 * Base class for all iterative learning algorithms. It provides the iterative
 * learning procedure for all of its subclasses.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class IterativeLearning extends LearningRule implements
        Serializable {

    /**
     * The class fingerprint that is set to indicate serialization compatibility
     * with a previous version of the class
     */
    private static final long serialVersionUID = 1L;
    /**
     * Learning rate parametar
     */
    protected double learningRate = 0.1d;
    /**
     * Current iteration counter
     */
    protected int currentIteration = 0;
    /**
     * Max training iterations (when to stopLearning training) TODO: this field
     * should be private, to force use of setMaxIterations from derived classes,
     * so iterationsLimited flag is also set at the sam etime.Wil that break
     * backward compatibility with serialized networks?
     */
    private int maxIterations = Integer.MAX_VALUE;
    
    /**
     * Flag for indicating if the training iteration number is limited
     */
    private boolean iterationsLimited = false;
   
    
    protected List<StopCondition> stopConditions;
    /**
     * Flag for indicating if learning thread is paused
     */
    private transient volatile boolean pausedLearning = false;

    /**
     * Creates new instance of IterativeLearning learning algorithm
     */
    public IterativeLearning() {
        super();
        this.stopConditions = new ArrayList<>();
    }

    /**
     * Returns learning rate for this algorithm
     *
     * @return learning rate for this algorithm
     */
    public double getLearningRate() {
        return this.learningRate;
    }

    /**
     * Sets learning rate for this algorithm
     *
     * @param learningRate learning rate for this algorithm
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Sets iteration limit for this learning algorithm
     *
     * @param maxIterations iteration limit for this learning algorithm
     */
    public void setMaxIterations(int maxIterations) {
        if (maxIterations > 0) {
            this.maxIterations = maxIterations;
            this.iterationsLimited = true;
        }
    }

    /**
     * Returns max iterations limit of this learning algorithm
     *
     * @return max iteration limit of this learning algorithm
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    public boolean isIterationsLimited() {
        return iterationsLimited;
    }

    /**
     * Returns current iteration of this learning algorithm
     *
     * @return current iteration of this learning algorithm
     */
    public int getCurrentIteration() {
        return this.currentIteration; // why boxed integer here?
    }

    /**
     * Returns true if learning thread is paused, false otherwise
     *
     * @return true if learning thread is paused, false otherwise
     */
    public boolean isPausedLearning() {
        return pausedLearning;
    }

    /**
     * Pause the learning
     */
    public void pause() {
        this.pausedLearning = true;
    }

    /**
     * Resumes the paused learning
     */
    public void resume() {
        this.pausedLearning = false;
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * This method is executed when learning starts, before the first epoch.
     * Used for initialisation.
     */
    @Override
    protected void onStart() {
        super.onStart();
        
        if (this.iterationsLimited) {
            this.stopConditions.add(new MaxIterationsStop(this));
        }

        this.currentIteration = 0;
    }

    protected void beforeEpoch() {
    }

    protected void afterEpoch() {
    }

    @Override
    final public void learn(DataSet trainingSet) {
        setTrainingSet(trainingSet); // set this field here su subclasses can access it 
        onStart();

        while (!isStopped()) {
            beforeEpoch();
            doLearningEpoch(trainingSet);
            this.currentIteration++;
            afterEpoch();

            // now check if stop condition is satisfied
            if (hasReachedStopCondition()) {
                stopLearning();
            } else if (!iterationsLimited && (currentIteration == Integer.MAX_VALUE)) {
                // if counter has reached max value and iteration number is not limited restart iteration counter
                this.currentIteration = 1;
            }

            // notify listeners that epoch has ended
            fireLearningEvent(new LearningEvent(this, LearningEvent.Type.EPOCH_ENDED));

            // Thread safe pause when learning is paused
            if (this.pausedLearning) {
                synchronized (this) {
                    while (this.pausedLearning) {
                        try {
                            this.wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }

        }
        onStop();
        fireLearningEvent(new LearningEvent(this, LearningEvent.Type.LEARNING_STOPPED));
    }

    protected boolean hasReachedStopCondition() {
        for (StopCondition stop : stopConditions) {
            if (stop.isReached()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Trains network for the specified training set and number of iterations
     *
     * @param trainingSet training set to learn
     * @param maxIterations maximum numberof iterations to learn
     *
     */
    public void learn(DataSet trainingSet, int maxIterations) {
        this.setMaxIterations(maxIterations);
        this.learn(trainingSet);
    }

    /**
     * Runs one learning iteration for the specified training set and notfies
     * observers. This method does the the doLearningEpoch() and in addtion
     * notifes observrs when iteration is done.
     *
     * @param trainingSet training set to learn
     */
    public void doOneLearningIteration(DataSet trainingSet) {
        beforeEpoch();
        doLearningEpoch(trainingSet);
        afterEpoch();
        // notify listeners        
        fireLearningEvent(new LearningEvent(this, LearningEvent.Type.LEARNING_STOPPED)); 
    }

    /**
     * Override this method to implement specific learning epoch - one learning
     * iteration, one pass through whole training set
     *
     * @param trainingSet training set
     */
    abstract public void doLearningEpoch(DataSet trainingSet);
}