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

import java.io.IOException;
import java.io.Serializable;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;

/**
 * Base class for all neural network learning algorithms. It provides the
 * general principles for training neural network.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
abstract public class LearningRule implements Serializable {

    /**
     * The class fingerprint that is set to indicate serialization compatibility
     * with a previous version of the class
     */
    private static final long serialVersionUID = 1L;
    /**
     * Neural network to train
     */
    protected NeuralNetwork neuralNetwork;
    /**
     * Collection of training elements
     */
    private transient DataSet trainingSet;
    /**
     * Flag to stop learning
     */
    private transient volatile boolean stopLearning = false;
    
    /**
     * List of learning rule listeners
     */
    protected transient javax.swing.event.EventListenerList listeners =
            new javax.swing.event.EventListenerList();

    /**
     * Creates new instance of learning rule
     */
    public LearningRule() {
    }

    /**
     * Sets training set for this learning rule
     *
     * @param trainingSet training set for this learning rule
     */
    public void setTrainingSet(DataSet trainingSet) {
        this.trainingSet = trainingSet;
    }

    /**
     * Gets training set
     *
     * @return training set
     */
    public DataSet getTrainingSet() {
        return trainingSet;
    }

    /**
     * Gets neural network
     *
     * @return neural network
     */
    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    /**
     * Sets neural network for this learning rule
     *
     * @param neuralNetwork neural network for this learning rule
     */
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    /**
     * Prepares the learning rule to run by setting stop flag to false
     */
    protected void onStart() {
        this.stopLearning = false;
    }

    /**
     * Stops learning
     */
    public synchronized void stopLearning() {
        // note: as long as all this method does is assign stopLearning, it doesn't need to be synchronized if stopLearning is a VOLATILE field. - Jon Tait 6-19-2010
        this.stopLearning = true;
    }

    /**
     * Returns true if learning has stopped, false otherwise
     *
     * @return true if learning has stopped, false otherwise
     */
    public synchronized boolean isStopped() {
        // note: as long as all this method does is return stopLearning, it doesn't need to be synchronized if stopLearning is a VOLATILE field. - Jon Tait 6-19-2010
        return this.stopLearning;
    }

    // This methods allows classes to register for LearningEvents
    public void addListener(LearningEventListener listener) {
        listeners.add(LearningEventListener.class, listener);
    }

    // This methods allows classes to unregister for LearningEvents
    public void removeListener(LearningEventListener listener) {
        listeners.remove(LearningEventListener.class, listener);
    }
    
    // This private class is used to fire LearningEvents
    protected void fireLearningEvent(LearningEvent evt) {
        Object[] listeners = this.listeners.getListenerList();
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == LearningEventListener.class) {
                ((LearningEventListener) listeners[i + 1]).handleLearningEvent(evt);
            }
        }
    }
    
    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
         listeners = new javax.swing.event.EventListenerList();    
    }       

    /**
     * Override this method to implement specific learning procedures
     *
     * @param trainingSet training set
     */
    abstract public void learn(DataSet trainingSet);
}