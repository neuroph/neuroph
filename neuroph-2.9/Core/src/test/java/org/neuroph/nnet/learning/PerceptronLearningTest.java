package org.neuroph.nnet.learning;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.random.WeightsRandomizer;

/**
 *
 * @author Tijana
 */
public class PerceptronLearningTest {

    PerceptronLearning instance;
    DataSet dataSet;
    double maxError;

    @Before
    public void setUp() {
        instance = new PerceptronLearning();
        dataSet = new DataSet(2, 1);
        dataSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        dataSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        dataSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        dataSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

        maxError = 0.4;
        instance.setMaxError(maxError);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDataSetMaxError() {
       Perceptron perceptron = new Perceptron(2, 1);
        perceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        perceptron.setLearningRule(instance);
        perceptron.learn(dataSet);
        assertTrue(instance.getTotalNetworkError() < maxError);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDataSetMSE() {
        Perceptron perceptron = new Perceptron(2, 1);
        perceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        perceptron.setLearningRule(instance);
        perceptron.learn(dataSet);

        MeanSquaredError mse = new MeanSquaredError();
        for (DataSetRow testSetRow : dataSet.getRows()) {
            perceptron.setInput(testSetRow.getInput());
            perceptron.calculate();
            double[] networkOutput = perceptron.getOutput();
            mse.addPatternError(new double[]{networkOutput[0]}, new double[]{testSetRow.getDesiredOutput()[0]});
        }
        assertTrue(mse.getTotalError() < maxError);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDataSetIterations() {
        Perceptron perceptron = new Perceptron(2, 1);
        perceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));

        perceptron.setLearningRule(instance);
        perceptron.learn(dataSet);

        int iterations = instance.getCurrentIteration();

        for (int i = 0; i < 5; i++) {
            perceptron = new Perceptron(2, 1);
            perceptron.randomizeWeights(new WeightsRandomizer(new Random(123)));
            perceptron.setLearningRule(instance);
            perceptron.learn(dataSet);
            assertEquals(iterations, instance.getCurrentIteration(), 0.0);
        }
    }

}
