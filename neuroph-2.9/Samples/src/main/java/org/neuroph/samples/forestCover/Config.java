/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
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
package org.neuroph.samples.forestCover;

public class Config {

    //File name where is saved main data set
    private String dataFilePath;
    //File name where we will save training data set
    private String trainingFileName;
    //File name where we will save test data set
    private String testFileName;
    //File name where we will save balanced data set (3000 of 
    //every type of tree created from training file)    
    private String balancedFileName;
    //File name where we will save normalized balanced data set
    private String normalizedBalancedFileName;
    //File name where we will save trained neural network
    private String trainedNetworkFileName;

    private int inputCount;
    private int firstHiddenLayerCount;
    private int secondHiddenLayerCount;
    private int outputCount;

    public Config() {

        dataFilePath = "data_sets/cover type.txt";
        trainingFileName = "data_sets/cover_type_data/training.txt";
        testFileName = "data_sets/cover_type_data/test.txt";
        balancedFileName = "data_sets/cover_type_data/balanceTraining.txt";
        normalizedBalancedFileName = "data_sets/cover_type_data/normalizedBalanceTraining.txt";
        trainedNetworkFileName = "data_sets/cover_type_data/trainedNetwork.txt";

        inputCount = 54;
        firstHiddenLayerCount = 40;
        secondHiddenLayerCount = 20;
        outputCount = 7;

    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getTrainingFileName() {
        return trainingFileName;
    }

    public void setTrainingFileName(String trainingFileName) {
        this.trainingFileName = trainingFileName;
    }

    public String getTestFileName() {
        return testFileName;
    }

    public void setTestFileName(String testFileName) {
        this.testFileName = testFileName;
    }

    public String getBalancedFileName() {
        return balancedFileName;
    }

    public void setBalancedFileName(String balancedFileName) {
        this.balancedFileName = balancedFileName;
    }

    public String getNormalizedBalancedFileName() {
        return normalizedBalancedFileName;
    }

    public void setNormalizedBalancedFileName(String normalizedBalanceFileName) {
        this.normalizedBalancedFileName = normalizedBalanceFileName;
    }

    public String getTrainedNetworkFileName() {
        return trainedNetworkFileName;
    }

    public void setTrainedNetworkFileName(String trainedNetworkFileName) {
        this.trainedNetworkFileName = trainedNetworkFileName;
    }

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public int getFirstHiddenLayerCount() {
        return firstHiddenLayerCount;
    }

    public void setFirstHiddenLayerCount(int firstHiddenLayerCount) {
        this.firstHiddenLayerCount = firstHiddenLayerCount;
    }

    public int getSecondHiddenLayerCount() {
        return secondHiddenLayerCount;
    }

    public void setSecondHiddenLayerCount(int secondHiddenLayerCount) {
        this.secondHiddenLayerCount = secondHiddenLayerCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public void setOutputCount(int outputCount) {
        this.outputCount = outputCount;
    }
}
