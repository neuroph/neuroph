package org.neuroph.nnet.learning;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.nnet.Adaline;
import org.neuroph.util.data.norm.MaxMinNormalizer;
import org.neuroph.util.data.norm.Normalizer;
import org.neuroph.util.random.WeightsRandomizer;

/**
 *
 * @author Tijana
 */
public class LMSTest {
	LMS instance;
	DataSet dataSet;
	double maxError;

	@Before
	public void setUp() {
		instance = new LMS();
		double[] x = new double[] { 12.39999962, 14.30000019, 14.5, 14.89999962, 16.10000038, 16.89999962, 16.5,
				15.39999962, 17, 17.89999962, 18.79999924, 20.29999924, 22.39999962, 19.39999962, 15.5, 16.70000076,
				17.29999924, 18.39999962, 19.20000076, 17.39999962, 19.5, 19.70000076, 21.20000076 };
		double[] y = new double[] { 11.19999981, 12.5, 12.69999981, 13.10000038, 14.10000038, 14.80000019, 14.39999962,
				13.39999962, 14.89999962, 15.60000038, 16.39999962, 17.70000076, 19.60000038, 16.89999962, 14,
				14.60000038, 15.10000038, 16.10000038, 16.79999924, 15.19999981, 17, 17.20000076, 18.60000038 };
		dataSet = new DataSet(1, 1);

		for (int i = 0; i < x.length; i++) {
			dataSet.addRow(new DataSetRow(new double[] { x[i] }, new double[] { y[i] }));
		}
		Normalizer n = new MaxMinNormalizer();
		n.normalize(dataSet);
		maxError = 0.01;
		instance.setMaxError(maxError);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDataSetMaxError() {
		Adaline adaline = new Adaline(1);
		adaline.randomizeWeights(new WeightsRandomizer(new Random(123)));

		adaline.setLearningRule(instance);
		adaline.learn(dataSet);
		assertTrue(instance.getTotalNetworkError() < maxError);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDataSetMSE() {
		Adaline adaline = new Adaline(1);
		adaline.randomizeWeights(new WeightsRandomizer(new Random(123)));

		adaline.setLearningRule(instance);
		adaline.learn(dataSet);

		MeanSquaredError mse = new MeanSquaredError();
		for (DataSetRow testSetRow : dataSet.getRows()) {
			adaline.setInput(testSetRow.getInput());
			adaline.calculate();
			double[] networkOutput = adaline.getOutput();
			 mse.addPatternError(new double[]{networkOutput[0]}, new double[]{testSetRow.getDesiredOutput()[0]});
		}
		assertTrue(mse.getTotalError() < maxError);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDataSetIterations() {
		Adaline adaline = new Adaline(1);
		adaline.randomizeWeights(new WeightsRandomizer(new Random(123)));

		adaline.setLearningRule(instance);
		adaline.learn(dataSet);

		int iterations = instance.getCurrentIteration();
		Double[] weights = adaline.getWeights();

		for (int i = 0; i < 5; i++) {
			adaline = new Adaline(1);
			adaline.randomizeWeights(new WeightsRandomizer(new Random(123)));
			adaline.setLearningRule(instance);
			adaline.learn(dataSet);
			Double[] weights1 = adaline.getWeights();
			for (int j = 0; j < weights1.length; j++) {
				assertEquals(weights[j], weights1[j], 0.0);
			}
			assertEquals(iterations, instance.getCurrentIteration(), 0.0);
		}
	}

}