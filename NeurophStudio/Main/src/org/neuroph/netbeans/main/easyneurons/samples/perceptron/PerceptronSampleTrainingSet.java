package org.neuroph.netbeans.main.easyneurons.samples.perceptron;

import java.util.Observable;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.DataSet;
import org.neuroph.netbeans.visual.NeuralNetworkTraining;

/**
 *
 * @author Marko
 */
public class PerceptronSampleTrainingSet extends Observable {

    DataSet dataSet = new DataSet(2, 1);
    NeuralNetwork neuralNetwork = null;
    NeuralNetworkTraining trainingControler = new NeuralNetworkTraining(neuralNetwork, dataSet);

    public NeuralNetworkTraining getControler() {
        return trainingControler;
    }

    public DataSet getTrainingSet() {
        return dataSet;
    }

    public NeuralNetwork getNeuralNetwork()
    {
        return neuralNetwork;
    }
    
    public void setTrainingSet(DataSet dataSet, NeuralNetwork nn, NeuralNetworkTraining tc) {
        this.dataSet = dataSet;
        this.neuralNetwork = nn;
        this.trainingControler = tc;
        setChanged();
        notifyObservers();
    }

}
