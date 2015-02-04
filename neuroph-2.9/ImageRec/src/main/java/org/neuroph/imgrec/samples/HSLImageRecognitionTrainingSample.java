package org.neuroph.imgrec.samples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionHSLData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mihailo Stupar
 * @author Zoran Sevarac
 */
public class HSLImageRecognitionTrainingSample {
    
    public static void main (String [] args) throws IOException {
        
        // path to image directory
        String imageDir ="/home/zoran/Downloads/MihailoHSLTest/trening";
        
        // image names - used for output neuron labels
        List<String> imageLabels = new ArrayList();
        imageLabels.add("bird");                                                   
        imageLabels.add("cat");
        imageLabels.add("dog");
                

        // create dataset
        Map<String,FractionHSLData> map = ImageRecognitionHelper.getFractionHSLDataForDirectory (new File(imageDir), new Dimension(20, 20));
        DataSet dataSet = ImageRecognitionHelper.createHSLTrainingSet(imageLabels, map);

        // create neural network
        List <Integer> hiddenLayers = new ArrayList<>();
        hiddenLayers.add(12);
        NeuralNetwork nnet = ImageRecognitionHelper.createNewNeuralNetwork("someNetworkName", new Dimension(20,20), ColorMode.COLOR_HSL, imageLabels, hiddenLayers, TransferFunctionType.SIGMOID);

        // set learning rule parameters
        MomentumBackpropagation mb = (MomentumBackpropagation)nnet.getLearningRule();
        mb.setLearningRate(0.2);
        mb.setMaxError(0.9);
        mb.setMomentum(1);
      
        // traiin network
        System.out.println("NNet start learning...");
        nnet.learn(dataSet);
        System.out.println("NNet learned");        
    }
      
}
