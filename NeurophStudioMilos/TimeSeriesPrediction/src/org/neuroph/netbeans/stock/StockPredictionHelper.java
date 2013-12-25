package org.neuroph.netbeans.stock;

import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 * Provides methods for creating neural network and training set for stock prediction
 * @author Tomek
 */
public class StockPredictionHelper {

        public static DataSet createTrainingSet(String trainingLabel, double normScale, int memory, int frequency, int stepsAhead) {
            DataSet trainingSet = new DataSet(memory, stepsAhead);
            trainingSet.setLabel(trainingLabel);
            
            // get stock data
            List<Double> stockData = StockPredictionManager.getInstance().getStockData();

            // normalize and create data set
            for (int i = 0; i < stockData.size() - memory - stepsAhead; i++) {
                double[] input = new double[memory];
                double[] output = new double[stepsAhead];
                
                // get input vector
                int inputColCounter = 0;
                for (int j = i; j < i + memory*frequency; j += frequency) {
                    if(j < stockData.size()){
                        input[inputColCounter] = stockData.get(j)/normScale;
                        inputColCounter++;
                    }
                }
                
                // get output vector - this works only for output vector with one output value - where stepsAhead == 1
                int outputColCounter = 0;
                //for (int k = i; k < i + memory * frequency; k += frequency) { 
                    if (i + memory * frequency < stockData.size()) {
                        output[outputColCounter] = stockData.get(i + memory * frequency) / normScale;
                        trainingSet.addRow(new DataSetRow(input, output));
                    }
                //}
            }

            return trainingSet;
    }

    public static NeuralNetwork createNewNeuralNetwork(String label, DataSet trainingSet, TransferFunctionType transferFunctionType, int input, ArrayList<Integer> hidden, int output) {
        int[] neuronsCount = new int[hidden.size()+2];
        neuronsCount[0] = input;
        for(int i=0; i < hidden.size(); i++) {
            neuronsCount[i+1] = hidden.get(i).intValue();
        }      
        neuronsCount[hidden.size()+1] = output;
        
        NeuralNetwork neuralNet = new MultiLayerPerceptron(transferFunctionType, neuronsCount);
	neuralNet.setLabel(label);

        return neuralNet;
    }

}
