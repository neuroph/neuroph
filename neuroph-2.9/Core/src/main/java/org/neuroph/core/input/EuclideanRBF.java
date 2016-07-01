/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.core.input;

import java.util.List;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 *
 * @author zoran
 */
public class EuclideanRBF extends InputFunction {

    @Override
    public double getOutput(List<Connection> inputConnections) {
	//	double output = 0d;

               double sqrSum = 0d;
		for(Connection connection : inputConnections) {
			Neuron neuron = connection.getFromNeuron();
			Weight weight = connection.getWeight();
			double diff = neuron.getOutput() - weight.getValue();
                        sqrSum += diff * diff;
		}
               
		return  0.5 *Math.sqrt(sqrSum) / (double)inputConnections.size(); // ovo trebaprebaciti u novu funkciju transfera sa odgovarajucim izvodom
    }
    
}
