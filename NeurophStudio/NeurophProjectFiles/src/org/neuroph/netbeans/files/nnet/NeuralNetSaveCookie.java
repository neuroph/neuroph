package org.neuroph.netbeans.files.nnet;

import java.io.IOException;
import org.neuroph.core.NeuralNetwork;
import org.openide.cookies.SaveCookie;

/**
 * This class provides save support for neural network files as SaveCookie implementation
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 */
public class NeuralNetSaveCookie implements SaveCookie {

    private  NeuralNetworkDataObject neuralNetDataObject;

    public NeuralNetSaveCookie(NeuralNetworkDataObject neuralNetDataObject) {
        this.neuralNetDataObject = neuralNetDataObject;
    }
    
    public void save() throws IOException {    
        NeuralNetwork neuralNet = neuralNetDataObject.getLookup().lookup(NeuralNetwork.class);
        String filePath = neuralNetDataObject.getPrimaryFile().getPath();
        neuralNet.save(filePath);
    }
}
