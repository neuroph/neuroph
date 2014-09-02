package org.neuroph.contrib.graphml;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
/**
 * Approximate XOR gate using a multilayer percepton with 2 inputs, 3 hidden neurons and 1 output. 
 * 
 * The example is adapted from http://neuroph.sourceforge.net/tutorials/MultiLayerPerceptron.html
 * 
 * @author fernando carrillo fernando@carrillo.at 
 *
 */
public class ExampleNetworXOR 
{
	/**
	 * Training data with two input neurons. 
	 * Truth: 
	 * 0,0: 0
	 * 0,1: 1
	 * 1,0: 1 
	 * 1,1: 0 
	 */
	public static DataSet getTrainingData() {
		DataSet data = new DataSet(2, 1);
		
		data.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
		data.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
		data.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
		data.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
		
		return data; 
	}
	
	/**
	 * Returns multilayer percepton learned to approximate the XOR gate. 
	 * @return
	 */
	public static MultiLayerPerceptron getNetwork() {
		
		MultiLayerPerceptron mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);
		
		
		mlPerceptron.learn( getTrainingData() );
		
		return mlPerceptron; 
	}
}
