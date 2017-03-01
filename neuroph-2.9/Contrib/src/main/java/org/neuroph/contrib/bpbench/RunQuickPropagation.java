package org.neuroph.contrib.bpbench;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.nnet.learning.QuickPropagation;


/**
 *
 * @author Mladen
 */
public class RunQuickPropagation implements LearningEventListener {

    public int noOfIter;
    public double error;

    public void nauci() {
        noOfIter = 0;
        error = Double.MAX_VALUE;
                
        DataSet trainingSet = DataSet.createFromFile("iris_data_normalised.txt", 4, 3, ",");
        QuickPropagation qp = new QuickPropagation();
        qp.setBatchMode(true);
        qp.setLearningRate(0.35);
        qp.setMaxError(0.02);
        
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 4, 7, 3);
        mlp.setLearningRule(qp);
        
        LearningRule learningRule = mlp.getLearningRule();
        learningRule.addListener(this);

        System.out.println("Training neural network...");
        mlp.learn(trainingSet);

        // test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(mlp, trainingSet);
    }

    /**
     * Prints network output for the each element from the specified training
     * set.
     *
     * @param neuralNet neural network
     * @param trainingSet training set
     */
    public static void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();

            System.out.print("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }

    @Override
    public void handleLearningEvent(LearningEvent le) {
        BackPropagation bp = (BackPropagation) le.getSource();
        if (le.getEventType() != LearningEvent.Type.LEARNING_STOPPED) {
            System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
        }
        if (noOfIter < bp.getCurrentIteration()) {
            noOfIter = bp.getCurrentIteration();
        }
        if (error > bp.getTotalNetworkError()) {
            error = bp.getTotalNetworkError();
        }
    }

}
