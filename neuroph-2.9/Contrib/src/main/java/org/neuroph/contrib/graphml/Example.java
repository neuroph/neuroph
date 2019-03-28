package org.neuroph.contrib.graphml;

import org.neuroph.core.NeuralNetwork;


/**
 * Example on how to export a neural network to graphml. 
 *  
 * @author fernando carrillo (fernando@carrillo.at)
 *
 */
public class Example 
{	
	/**
	 * 1. Generate trained artificial neural network.
	 * 2. Create GraphmlExport instance. 
	 * 3. Parse the artificial neural network. 
	 * 4. Print to STDOUT
	 *   
	 */
	public Example() {
		NeuralNetwork ann = ExampleNetworXOR.getNetwork(); 
		GraphmlExport ge = new GraphmlExport( ann );
		ge.parse(); 
		ge.printToStdout();
	}
	
	public static void main(String[] args) 
	{
		new Example(); 
	}
}
