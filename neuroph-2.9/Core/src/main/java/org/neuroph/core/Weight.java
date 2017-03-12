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
package org.neuroph.core;

import java.util.Objects;
import java.util.Random;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Neuron connection weight.
 *
 * @see Connection
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Weight implements java.io.Serializable, Cloneable {

    /**
     * The class fingerprint that is set to indicate serialization compatibility
     * with a previous version of the class
     */
    private static final long serialVersionUID = 2L;

    /**
     * Weight value
     */
    public double value;

    /**
     * Weight change
     */
    public transient double weightChange;

    /**
     * Training data buffer holds various algorithm specific data which is used
     * for adjusting this weight value during training
     */
    private transient Object trainingData;

    // maybe store deltaWeight and weight value history in transient fields...?
    /**
     * Creates an instance of connection weight with random weight value in
     * range [0..1]
     */
    public Weight() {
        this.value = Math.random() - 0.5d;
        this.weightChange = 0;
    }

    /**
     * Creates an instance of connection weight with the specified weight value
     *
     * @param value weight value
     */
    public Weight(double value) {
        this.value = value;
    }

    /**
     * Increases the weight for the specified amount
     *
     * @param amount amount to add to current weight value
     */
    public void inc(double amount) {
        this.value += amount;
    }

    /**
     * Decreases the weight for specified amount
     *
     * @param amount amount to subtract from the current weight value
     */
    public void dec(double amount) {
        this.value -= amount;
    }

    /**
     * Sets the weight value
     *
     * @param value weight value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns weight value
     *
     * @return value of this weight
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Returns weight value as String
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Sets random weight value
     */
    public void randomize() {
        this.value = Math.random() - 0.5d;
    }

    /**
     * Sets random weight value within specified interval
     */
    public void randomize(double min, double max) {
        this.value = min + Math.random() * (max - min);
    }

    public void randomize(Random generator) {
        this.value = generator.nextDouble();
    }

    /**
     * Returns training data buffer for this weight
     *
     * @return training data buffer for this weight
     */
    public Object getTrainingData() {
        return trainingData;
    }

    public void setTrainingData(Object trainingData) {
        this.trainingData = trainingData;
    }

    /**
     * Returns cloned instance of this weight
     * Important: trainingData will be lost in cloned instance
     * @return cloned instance of this weight
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Weight cloned = (Weight) super.clone();
        cloned.setTrainingData(new Object()); // since we cannot call Object.clone() reset training data to nulll
        return cloned;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 17).
                append(value).
                append(weightChange).
                append(trainingData).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Weight other = (Weight) obj;
        if (Double.doubleToLongBits(this.value) != Double.doubleToLongBits(other.value)) {
            return false;
        }
        if (Double.doubleToLongBits(this.weightChange) != Double.doubleToLongBits(other.weightChange)) {
            return false;
        }
        if (!Objects.equals(this.trainingData, other.trainingData)) {
            return false;
        }
        return true;
    }
    
    
    

}
