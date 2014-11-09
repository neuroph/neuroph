package org.neuroph.contrib.evaluation.stat;


import org.neuroph.contrib.evaluation.domain.ClassificationOutput;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class McNemarTest {

    private int[][] contigencyMatrix = new int[2][2];

    public boolean evaluateNetworks(NeuralNetwork network1, NeuralNetwork network2, DataSet dataSet) {
        for (DataSetRow dataRow : dataSet.getRows()) {
            forwardPass(network1, dataRow);
            forwardPass(network2, dataRow);


            ClassificationOutput output1 = ClassificationOutput.getMaxOutput(network1.getOutput());
            ClassificationOutput output2 = ClassificationOutput.getMaxOutput(network2.getOutput());

            //are their results different
            if (output1.getActualClass() != output2.getActualClass()) {
                //if first one is correct and second incorrect
                if (output1.getActualClass() == getDesiredClass(dataRow.getDesiredOutput())) {
                    contigencyMatrix[1][0]++;
                    //if first is incorrect and second is correct
                } else {
                    contigencyMatrix[0][1]++;
                }
            } else {
                //if both are correct
                if (output1.getActualClass() == getDesiredClass(dataRow.getDesiredOutput())) {
                    contigencyMatrix[1][1]++;
                    //if both are incorrect
                } else {
                    contigencyMatrix[0][0]++;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(contigencyMatrix[i][j]+" ");
            }
            System.out.println();
        }

        double a = Math.abs(contigencyMatrix[0][1] - contigencyMatrix[1][0]) - 1;
        double hiSquare = (a * a) / (contigencyMatrix[0][1] + contigencyMatrix[1][0]);


        System.out.println(hiSquare);
        return hiSquare > 3.841;
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
        neuralNetwork.setInput(dataRow);
        neuralNetwork.calculate();
    }

}
