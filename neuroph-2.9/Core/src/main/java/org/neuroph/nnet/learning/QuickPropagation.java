package org.neuroph.nnet.learning;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 *
 * @author Mladen
 */
public class QuickPropagation extends BackPropagation {

    // ove dve konstante sam preuzeo iz radova koje si mi poslao - ne konstante vec d amogu da se setuju po potrebi
    private final static double maximumGrowthFactor = 1.75;
  //  private final static double shrinkFactor = maximumGrowthFactor / (1.0 + maximumGrowthFactor);
    // hes mapu koristim da cuvam vrednosti prethodnih iteracija - koristi training data

    @Override
    public void updateNeuronWeights(Neuron neuron) {
        double error = neuron.getError();
        for (Connection con : neuron.getInputConnections()) {
            
            Weight<QuickPropData> w = con.getWeight();
            QuickPropData qpData = w.getTrainingData();

            double input = con.getInput();
            if (input == 0) continue;            
            
            double previousWeightChange = qpData.previousWeightChange;
            double previousError = qpData.previousError;
                
// 1. previous and current gradient (error) are in same direction but current error is smaller than previous
// 2. current slope is in the opposite direction from the previous one, that means that we have crossed over the minimum and that we are now on the opposite side of the valley. In this case, the next step will place us somewhere between the current and previous positions. 	
//   If the current slope is opposite in sign from the previous slope, the quadratic term is used alone. 
// 3. ako su u istom smeru ali tekuci gradijent je veci ili skoro isti kao prethodni - shrink
            double currentWeightChange = 0;

            if ((previousError * error > 0) && (error < previousError)) {    // gradijenti istog znaka i tekuci gradijent je manji od prethodnog (1 slucaj)
                currentWeightChange = (error / (previousError - error)) * previousWeightChange * input; // quick prop  // ovde bi trebalo dodati epsilon

                if (currentWeightChange > (maximumGrowthFactor * previousWeightChange)) {
                    currentWeightChange = maximumGrowthFactor * previousWeightChange;
                }

            } else if ((previousError * error > 0) && (error >= previousError)) { // gradijenti istog znaka i tekuci gradijent je veci od prethodnog (3 slucaj)
                currentWeightChange = maximumGrowthFactor * previousWeightChange;

            } else if (previousError * error < 0) { // gradijenti razlicitog znaka (2 slucaj)
                currentWeightChange = (error / (previousError - error)) * previousWeightChange * input; // quick prop 

                // sta kad ej negativno
                if (Math.abs(currentWeightChange) >= Math.abs((maximumGrowthFactor * previousWeightChange))) {
                    currentWeightChange = maximumGrowthFactor * previousWeightChange;
                }
            } else { // gradijent je 0, standardni backprop
                currentWeightChange = learningRate * error * input;
            }
            
            w.weightChange += currentWeightChange;
            
//            if (currentWeightChange > 10) {
//                System.out.println(getCurrentIteration() + " iteration : "+ currentWeightChange);
//            }
            qpData.previousWeightChange = currentWeightChange;
            qpData.previousError = error;
            
        }
       
    }

    @Override
    protected void onStart() {
        super.onStart(); //To change body of generated methods, choose Tools | Templates.
        for (Layer layers : neuralNetwork.getLayers()) {
            for (Neuron neuron : layers.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    //connection.getWeight().setTrainingData(new QuickPropData());
                    connection.setWeight(new Weight<QuickPropData>());                    
                }
            }
        }

    }
    
    public static class QuickPropData {
        private double previousWeightChange;
        private double previousError;
    }
    
    
}
// strata implementacija
//            if (previousWeightChange < 0) { // gradijenti su razliciti
//                if (error < 0) {
//                   w.weightChange += learningRate * error * input;
//                }
//                
//                if (error <= (shrinkFactor * previousError)) {
//                    w.weightChange += maximumGrowthFactor * previousWeightChange * input;
//                } else {
//                    w.weightChange += (error / (previousError - error)) * previousWeightChange * input;
//                }
//
//            } else if (previousWeightChange > 0) { // gradijenti su isti -mora i tekuci da bude manji od prethodnog
//                if (error > 0) {
//                    w.weightChange += learningRate * error * input ;
//                }
//                
//                if (error >= (shrinkFactor * previousError)) {
//                    w.weightChange += maximumGrowthFactor * previousWeightChange *  input;
//                } else {
//                    w.weightChange += (error / (previousError - error)) * previousWeightChange * input;
//                }
//            } else { // gradijent je 0, standardni backprop
//                w.weightChange += learningRate * error * input;
//            }