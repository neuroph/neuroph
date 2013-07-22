package org.neuroph.netbeans.visual;

import java.io.PrintWriter;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.SupervisedHebbianLearning;
import org.neuroph.util.VectorParser;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

public class NeuralNetworkTraining implements Thread.UncaughtExceptionHandler {

    NeuralNetwork neuralNet;
    DataSet trainingSet;
    
    boolean showErrorGraph;

    public NeuralNetworkTraining(NeuralNetwork neuralNet, DataSet trainingSet) {
        this.neuralNet = neuralNet;
        this.trainingSet = trainingSet;
    }

    public NeuralNetworkTraining(NeuralNetwork neuralNet) {
        this.neuralNet = neuralNet;
    }

    public DataSet getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(DataSet trainingSet) {
        this.trainingSet = trainingSet;
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

    public void setLmsParams(Double learningRate, Double maxError,
            Integer maxIterations) {
        LMS lms = (LMS) this.neuralNet.getLearningRule();
        lms.setLearningRate(learningRate);
        lms.setMaxError(maxError);
        lms.setMaxIterations(maxIterations);
    }

    public void setHebbianParams(Double learningRate, Double maxError,
            Integer maxIterations) {
        SupervisedHebbianLearning hebbian = (SupervisedHebbianLearning) this.neuralNet
                .getLearningRule();
        hebbian.setLearningRate(learningRate);
    }

    public void setKohonenParams(Double learningRate, Integer Iphase,
            Integer IIphase) {
        KohonenLearning kl = (KohonenLearning) this.neuralNet.getLearningRule();
        kl.setLearningRate(learningRate.doubleValue());
        kl.setIterations(Iphase.intValue(), IIphase.intValue());
    }

    public void setStepDRParams(Double learningRate, Double maxError, Integer maxIterations) {
        BinaryDeltaRule sdr = (BinaryDeltaRule) this.neuralNet.getLearningRule();
        sdr.setLearningRate(learningRate);
        sdr.setMaxError(maxError);
        sdr.setMaxIterations(maxIterations);
    }

    public void calculate() {
        neuralNet.calculate();
    }

    public void train() {
        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        io.select();

        PrintWriter out = io.getOut();
        out.println("Starting neural network training...");
        out.print("Training network " + neuralNet.getLabel());
        out.println(" using data set " + trainingSet.getLabel());
                
        neuralNet.learnInNewThread(trainingSet);
        neuralNet.getLearningThread().setUncaughtExceptionHandler(this);
    }

    public NeuralNetwork getNetwork() {
        return this.neuralNet;
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
     * @param trainingSet the training set that will be compared with input
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

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        io.select();

        PrintWriter out = io.getOut();
        out.println("Training error: " + e.getMessage());

    }

    public boolean getShowErrorGraph() {
        return showErrorGraph;
    }

    public void setShowErrorGraph(boolean showErrorGraph) {
        this.showErrorGraph = showErrorGraph;
    }
    
    
    
    
}
