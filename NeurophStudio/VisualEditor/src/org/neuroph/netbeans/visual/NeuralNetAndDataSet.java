package org.neuroph.netbeans.visual;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.VectorParser;

public class NeuralNetAndDataSet  {

    NeuralNetwork neuralNet;
    DataSet dataSet;
    
    boolean showErrorGraph;

    public NeuralNetAndDataSet() {
    }

    
    
    public NeuralNetAndDataSet(NeuralNetwork neuralNet, DataSet dataSet) {
        this.neuralNet = neuralNet;
        this.dataSet = dataSet;
    }

    public NeuralNetAndDataSet(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet trainingSet) {
        this.dataSet = trainingSet;
    }

    public void setInput(String netIn) {
        double[] inVect = VectorParser.parseDoubleArray(netIn);
        neuralNet.setInput(inVect);
        neuralNet.calculate();
    }

    public void stopTraining() {
        neuralNet.stopLearning();
    }

    public void pause() {
        neuralNet.pauseLearning();
    }

    public void resume() {
        neuralNet.resumeLearning();
    }

    public boolean isStoppedTraining() {
        return neuralNet.getLearningRule().isStopped();
    }

    public void calculateNetwork() {
        neuralNet.calculate();
    }

    public NeuralNetwork getNetwork() {
        return this.neuralNet;
    }

    public void setNeuralNet(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }
    
    

    public void randomize() {
        neuralNet.randomizeWeights();
    }

    public void reset() {
        neuralNet.reset();
        //neuralNet.notifyChange();
    }

    /**
     * This method checks compatibility of neural network and training set. It
     * compares if the number of inputs and outputs of neural network matches
     * the number of inputs and outputs of training set
     *
     * @param neuralNetwork the neural network that will be compared with input
     * training set
     * @param dataSet the training set that will be compared with input
     * neural network
     * @return returns boolean value of the comparison test. Returns true if the
     * number of inputs and outputs of the input neural network are the same as
     * the number of inputs and outputs of the input training set
     */
    public boolean checkCompatibility(NeuralNetwork neuralNetwork, DataSet trainingSet) {
        int inputsNumberOfNeuralNetwork = neuralNetwork.getInputNeurons().length;
        int outputsNumberOfNeuralNetwork = neuralNetwork.getOutputNeurons().length;

        int inputsNumberOfTrainingSet = trainingSet.getRowAt(0).getInput().length;
        int outputsNumberOfTrainingSet = 0;
        //checks if the training set is Supervised type or Unsupervised
        if (trainingSet.getRowAt(0).isSupervised()) {
            if (trainingSet.getRowAt(0).isSupervised()) {
                DataSetRow supervisedTrainingSetElement = trainingSet.getRowAt(0);
                outputsNumberOfTrainingSet = supervisedTrainingSetElement.getDesiredOutput().length;
            }
        }

        if (outputsNumberOfTrainingSet != 0) {
            //compatibility check for Supervised training set
            if (inputsNumberOfNeuralNetwork == inputsNumberOfTrainingSet && outputsNumberOfNeuralNetwork == outputsNumberOfTrainingSet) {
                return true;
            }
        } else {
            //compatibility check for Unsupervised training set
            if (inputsNumberOfNeuralNetwork == inputsNumberOfTrainingSet) {
                return true;
            }
        }
        return false;
    }



    public boolean getShowErrorGraph() {
        return showErrorGraph;
    }

    public void setShowErrorGraph(boolean showErrorGraph) {
        this.showErrorGraph = showErrorGraph;
    }
    
    
    
    
}
