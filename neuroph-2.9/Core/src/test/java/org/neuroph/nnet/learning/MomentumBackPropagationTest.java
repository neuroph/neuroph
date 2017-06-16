package org.neuroph.nnet.learning;

import java.util.ArrayList;
import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.WeightsRandomizer;

/**
 *
 * @author Tijana, Zoran
 */
public class MomentumBackPropagationTest {

    MomentumBackpropagation instance;
    DataSet xorDataSet;
    DataSet irisDataSet;
    double maxError;

    @Before
    public void setUp() {
        instance = new MomentumBackpropagation();
        instance.setMomentum(0.5);
        xorDataSet = new DataSet(2, 1);
        xorDataSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        xorDataSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        xorDataSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        xorDataSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
        maxError = 0.01;
        instance.setLearningRate(0.5);
        instance.setMaxError(maxError);
        String inputFileName = "src\\test\\resources\\iris_normalized.txt";
        irisDataSet = DataSet.createFromFile(inputFileName, 4, 3, ",", false);

    }

    @Test
    public void testXorMaxError() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 1);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        myMlPerceptron.setLearningRule(instance);
        myMlPerceptron.learn(xorDataSet);

        assertTrue(instance.getTotalNetworkError() < maxError);
    }

    @Test
    public void testXorMSE() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 1);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        myMlPerceptron.setLearningRule(instance);
        myMlPerceptron.learn(xorDataSet);

        MeanSquaredError mse = new MeanSquaredError();
        for (DataSetRow testSetRow : xorDataSet.getRows()) {
            myMlPerceptron.setInput(testSetRow.getInput());
            myMlPerceptron.calculate();
            double[] networkOutput = myMlPerceptron.getOutput();
            mse.addPatternError(networkOutput, testSetRow.getDesiredOutput());
        }
        assertTrue(mse.getTotalError() < maxError);
    }

    @Test
    public void testXorIterations() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 1);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        myMlPerceptron.setLearningRule(instance);
        myMlPerceptron.learn(xorDataSet);

        int iterations = instance.getCurrentIteration();
        Double[] weights = myMlPerceptron.getWeights();

        for (int i = 0; i < 5; i++) {
            myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 2, 3, 1);
            myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));
            myMlPerceptron.setLearningRule(instance);
            myMlPerceptron.learn(xorDataSet);
            Double[] weights1 = myMlPerceptron.getWeights();
            for (int j = 0; j < weights1.length; j++) {
                assertEquals(weights[j], weights1[j], 0.0);
            }
            assertEquals(iterations, instance.getCurrentIteration(), 0.0);
        }
    }

    @Test
    public void testIrisMaxError() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(4, 16, 3);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));
        myMlPerceptron.setLearningRule(instance);

        myMlPerceptron.learn(irisDataSet);

        assertTrue(instance.getTotalNetworkError() < maxError);
    }

 /*   @Test
    public void testIrisMSE() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(4, 16, 3);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        myMlPerceptron.setLearningRule(instance);
        myMlPerceptron.learn(irisDataSet);

        MeanSquaredError mse = new MeanSquaredError();
        for (DataSetRow testSetRow : irisDataSet.getRows()) {
            myMlPerceptron.setInput(testSetRow.getInput());
            myMlPerceptron.calculate();
            double[] networkOutput = myMlPerceptron.getOutput();
            mse.addPatternError(networkOutput, testSetRow.getDesiredOutput());
        }
        System.out.print(mse.getTotalError());
        assertTrue(mse.getTotalError() < maxError);
    }*/

    @Test
    public void testIrisIterations() {
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(4, 16, 3);
        myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));
        myMlPerceptron.setLearningRule(instance);
        myMlPerceptron.learn(irisDataSet);

        int iterations = instance.getCurrentIteration();
        Double[] weights = myMlPerceptron.getWeights();
        for (int i = 0; i < 5; i++) {
            myMlPerceptron = new MultiLayerPerceptron(4, 16, 3);
            myMlPerceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));
            myMlPerceptron.setLearningRule(instance);
            myMlPerceptron.learn(irisDataSet);
            Double[] weights1 = myMlPerceptron.getWeights();
            for (int j = 0; j < weights1.length; j++) {
                assertEquals(weights[j], weights1[j], 0.0);
            }
            assertEquals(iterations, instance.getCurrentIteration(), 0.0);
        }
    }

}
