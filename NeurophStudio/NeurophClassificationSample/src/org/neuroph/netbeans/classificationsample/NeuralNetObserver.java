package org.neuroph.netbeans.classificationsample;

import java.util.Observable;
import java.util.Observer;
import org.neuroph.core.NeuralNetwork;

/**
 *
 * @author Marko
 */
public class NeuralNetObserver implements Observer{

     private NeuralNetwork nnet = new NeuralNetwork();

     public NeuralNetwork getNnet() {
        return nnet;
    }

     public void setNeuralNet(NeuralNetwork nn)
    {
        nnet = nn;
    }

    public void update(Observable o, Object arg) {
         setNeuralNet(((org.neuroph.netbeans.classificationsample.PerceptronSampleTrainingSet)o).getNeuralNetwork());
    }



}
