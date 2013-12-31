package org.neuroph.bci.mindwave;

import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author zoran
 */
public class BciHelper {
    
    public static NeuralNetwork createNewNeuralNetwork(String neuralNetworkName, TransferFunctionType  transferFunction, Integer numberOfInputs, List<Integer> layersNeuronsCount, Integer numberOfOutputs, String[] classes) {
    
		layersNeuronsCount.add(0, numberOfInputs);
		layersNeuronsCount.add(numberOfOutputs);
		
		NeuralNetwork neuralNetwork = new MultiLayerPerceptron(layersNeuronsCount, transferFunction);
		neuralNetwork.setLabel(neuralNetworkName);
//		PluginBase imageRecognitionPlugin = new ImageRecognitionPlugin(samplingResolution, colorMode);
//		neuralNetwork.addPlugin(imageRecognitionPlugin);

                neuralNetwork.setLearningRule(new MomentumBackpropagation());

		Neuron[] outputNeurons = neuralNetwork.getOutputNeurons();
		
		for(int i=0; i<outputNeurons.length; i++) {
                    outputNeurons[i].setLabel(classes[i]);
		}                
                
            return neuralNetwork;        
        
    }
    
    
}
