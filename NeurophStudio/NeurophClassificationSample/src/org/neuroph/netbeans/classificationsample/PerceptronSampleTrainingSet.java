package org.neuroph.netbeans.classificationsample;

import java.util.Observable;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;

/**
 *
 * @author Marko
 */
public class PerceptronSampleTrainingSet extends Observable {

    DataSet dataSet = new DataSet(2, 1);
    NeuralNetwork neuralNetwork = null;
    NeuralNetAndDataSet trainingControler = new NeuralNetAndDataSet(neuralNetwork, dataSet);

    public NeuralNetAndDataSet getControler() {
        return trainingControler;
    }

    public DataSet getTrainingSet() {
        return dataSet;
    }

    public NeuralNetwork getNeuralNetwork()
    {
        return neuralNetwork;
    }
    
    public void setTrainingSet(DataSet dataSet, NeuralNetwork nn, NeuralNetAndDataSet tc) {
        this.dataSet = dataSet;
        this.neuralNetwork = nn;
        this.trainingControler = tc;
        setChanged();
        notifyObservers();
    }

}
