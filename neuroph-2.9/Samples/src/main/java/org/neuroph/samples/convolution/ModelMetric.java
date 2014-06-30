package org.neuroph.samples.convolution;

import java.util.PriorityQueue;
import org.neuroph.nnet.learning.ConvolutionalBackpropagation;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class ModelMetric {

	public static void calculateModelMetric(NeuralNetwork<ConvolutionalBackpropagation> neuralNetwork, DataSet testSet) {

		int totalWrong = 0;
		int totalRight = 0;

		for (DataSetRow testSetRow : testSet.getRows()) {
			neuralNetwork.setInput(testSetRow.getInput());
			neuralNetwork.calculate();

			PriorityQueue<Output> desiredOutput = new PriorityQueue<Output>();
			PriorityQueue<Output> actualOutput = new PriorityQueue<Output>();

			double[] outputResult = testSetRow.getDesiredOutput();
			double[] actualResult = neuralNetwork.getOutput();

			for (int i = 0; i < outputResult.length; i++) {
				desiredOutput.add(new Output(i, outputResult[i]));
				actualOutput.add(new Output(i, actualResult[i]));
			}
			
//			System.out.println(desiredOutput.peek());
//			System.out.println(actualOutput.peek());
			

			if (desiredOutput.peek().id == actualOutput.peek().id) {
				totalRight++;
			} else {
				totalWrong++;
			}
			// System.out.println("Desired: " +
			// Arrays.toString(testSetRow.getDesiredOutput()));
			// System.out.println("Actual: " + Arrays.toString(networkOutput));
		}
		
		System.out.println("RIGHT: "+ totalRight);
		System.out.println("WRONG: "+ totalWrong);

	}

	private static class Output implements Comparable<Output> {

		int id;
		double output;

		public Output(int id, double output) {
			super();
			this.id = id;
			this.output = output;
		}

	
		@Override
		public boolean equals(Object obj) {
			if (obj ==null)
				return false;
			if (obj == this)
				return true;
			if (!(obj instanceof ModelMetric))
				return false;
			Output that = (Output) obj;
			return this.id == that.id;
		}

		public int compareTo(Output o) {
			if (output < o.output) {
				return 1;
			} else if (output > o.output) {
				return -1;
			} else
				return 0;
		}
		
		@Override
		public String toString() {
			String object = "ID: "+id+ ", Output: " + output;
			return object;
		}
	}
	


}
