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

package org.neuroph.util.benchmark;

import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;

/**
 * This class is example of custom benchmarking task for Multi Layer Perceptorn network
 * Note that this benchmark only measures the speed at implementation level - the
 * speed of data flow forward and backward through network
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MyBenchmarkTask extends BenchmarkTask {    
    private MultiLayerPerceptron network;
    private DataSet trainingSet;
    
    public MyBenchmarkTask(String name) {
        super(name);
    }

    /**
     * Benchmrk preparation consists of training set and neural networ creatiion.
     * This method generates training set with 100 rows, where every row has 10 input and 5 output elements
     * Neural network has two hiddden layers with 8 and 7 neurons, and runs learning rule for 2000 iterations
     */
    @Override
    public void prepareTest() {
        int trainingSetSize = 100;
        int inputSize = 10;
        int outputSize = 5;
        
        this.trainingSet = new DataSet(inputSize, outputSize);
        
        for (int i = 0; i < trainingSetSize; i++) {
            double[] input = new double[inputSize];
            for( int j=0; j<inputSize; j++)
                input[j] = Math.random();

            double[] output = new double[outputSize];
            for( int j=0; j<outputSize; j++)
                output[j] = Math.random();            
            
            DataSetRow trainingSetRow = new DataSetRow(input, output);
            trainingSet.addRow(trainingSetRow);
        }
        
        
        network = new MultiLayerPerceptron(inputSize, 8, 7, outputSize);
        ((MomentumBackpropagation)network.getLearningRule()).setMaxIterations(2000);
        
    }

    @Override
    public void runTest() {
        network.learn(trainingSet);
    }
}