package org.neuroph.contrib.eval.classification;


import org.neuroph.contrib.eval.classification.ClassificationResult;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Class used to test if there is statistical significant difference between two classifiers
 */
public class McNemarTest {

    private int[][] contigencyMatrix = new int[2][2];

    /**
     * @param network1 first trained neurl netowrk
     * @param network2 second trained neural network
     * @param dataSet  data set used for performance evaluation
     * @return if there exists significant difference between two classification models
     */
    public boolean evaluateNetworks(NeuralNetwork network1, NeuralNetwork network2, DataSet dataSet) {
        for (DataSetRow dataRow : dataSet.getRows()) {
            forwardPass(network1, dataRow);
            forwardPass(network2, dataRow);
           
            double[] networkOutput1 = network1.getOutput();
            double[] networkOutput2 = network2.getOutput();
            
            int maxNeuronIdx1 = Utils.maxIdx(networkOutput1);
            int maxNeuronIdx2 = Utils.maxIdx(networkOutput2);         
            
            ClassificationResult output1 = new ClassificationResult(maxNeuronIdx1, networkOutput1[maxNeuronIdx1]);
            ClassificationResult output2 = new ClassificationResult(maxNeuronIdx2, networkOutput2[maxNeuronIdx2]);     

//            ClassificationResult output1 = ClassificationResult.fromMaxOutput(network1.getOutput());
//            ClassificationResult output2 = ClassificationResult.fromMaxOutput(network2.getOutput());

            //are their results different
            if (output1.getClassIdx() != output2.getClassIdx()) {
                //if first one is correct and second incorrect
                if (output1.getClassIdx() == getDesiredClass(dataRow.getDesiredOutput())) {
                    contigencyMatrix[1][0]++;
                    //if first is incorrect and second is correct
                } else {
                    contigencyMatrix[0][1]++;
                }
            } else {
                //if both are correct
                if (output1.getClassIdx() == getDesiredClass(dataRow.getDesiredOutput())) {
                    contigencyMatrix[1][1]++;
                    //if both are incorrect
                } else {
                    contigencyMatrix[0][0]++;
                }
            }
        }

        printContingencyMatrix();

        double a = Math.abs(contigencyMatrix[0][1] - contigencyMatrix[1][0]) - 1;
        double hiSquare = (a * a) / (contigencyMatrix[0][1] + contigencyMatrix[1][0]);


        System.out.println(hiSquare);
        return hiSquare > 3.841;
    }

    private void printContingencyMatrix() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(contigencyMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private int getDesiredClass(double[] output) {

        for (int i = 0; i < output.length; i++) {
            if (output[i] == 1) {
                return i;
            }
        }
        return -1;
    }

    private void forwardPass(NeuralNetwork neuralNetwork, DataSetRow dataRow) {
        neuralNetwork.setInput(dataRow.getInput());
        neuralNetwork.calculate();
    }

}
