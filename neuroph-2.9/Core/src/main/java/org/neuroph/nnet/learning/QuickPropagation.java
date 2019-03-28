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
    private double maximumGrowthFactor = 1.75;
  //  private final static double shrinkFactor = maximumGrowthFactor / (1.0 + maximumGrowthFactor);
    // hes mapu koristim da cuvam vrednosti prethodnih iteracija - koristi training data

    @Override
    public void calculateWeightChanges(Neuron neuron) {
        double delta = neuron.getDelta();
        for (Connection con : neuron.getInputConnections()) {
            
            Weight<QuickPropData> w = con.getWeight();
            QuickPropData qpData = w.getTrainingData();

            double input = con.getInput();
            if (input == 0) continue;            
            
            double gradient = delta * input;            
            double previousWeightChange = qpData.previousWeightChange;  //  this is positive gradiend dE/dw  (ili ipak -dE/dw)
            
            // neuronError je delta  a ne gradijent
            //double previousError = qpData.previousError;                // delta    dE/dy * y'  - ovo bi morao da bude gradijent - ne sadrzi input
            double prevGradient = qpData.prevGradient;
            
            double currentWeightChange = 0;
            
            
                
            //1. tekuci gradijent i prethdoni gradijent  su istog znaka, i tekuci gradijent je manji od prethodnog gradijenta
            //∆w(t) = (S(t)/(S(t-1)-S(t))) * ∆w(t−1)
            //
            //
            //2. tekuci gradijent i prethdoni gradijent nisu istog znaka,  (i tekuci gradijent je manji od prethodnog gradijent - da li vazi i ovaj uslov, mislim d amora inace menja znak, ali izgleda da nema veze)
            //∆w(t) = (S(t)/(S(t-1)-S(t))) * ∆w(t−1)
            //
            //
            //3. tekuci gradijent i prethdoni gradijent su istog znaka, i tekuci delta je jednak ili veci od prethodnog delta 

            //1. tekuci delta i prethdoni delta su istog znaka, i tekuci delta je manji od prethodnog delta
            if ((prevGradient * gradient > 0) && (gradient < prevGradient)) {    // gradijenti istog znaka i tekuci gradijent je manji od prethodnog (1 slucaj)
                currentWeightChange = (gradient / (prevGradient - gradient)) * previousWeightChange; // quick prop  // ovde bi trebalo dodati epsilon
                // currentWeightChange = learningRate * error * input;
                if (Math.abs(currentWeightChange) >= Math.abs((maximumGrowthFactor * previousWeightChange))) {
                    currentWeightChange = maximumGrowthFactor * previousWeightChange;
                }
            }
             //3. tekuci delta i prethdoni delta su istog znaka, i tekuci delta je jednak ili veci od prethodnog delta 
            else if ((prevGradient * gradient > 0) && (gradient >= prevGradient)) { // gradijenti istog znaka i tekuci gradijent je veci od prethodnog (3 slucaj)
                currentWeightChange = maximumGrowthFactor * previousWeightChange; // ???
              //  currentWeightChange = previousWeightChange; // ???

            } else if (prevGradient * gradient < 0) { // gradijenti razlicitog znaka (2 slucaj)
                currentWeightChange = (gradient / (prevGradient - gradient)) * previousWeightChange; // quick prop 

                // sta kad ej negativno
                if (Math.abs(currentWeightChange) >= Math.abs((maximumGrowthFactor * previousWeightChange))) {
                    currentWeightChange = maximumGrowthFactor * previousWeightChange;
                }
            } else { // gradijent je 0, standardni backprop
                currentWeightChange = -learningRate * delta * input;
            }
            
            w.weightChange += currentWeightChange;
         //   System.out.println("currentWeightChange: " + currentWeightChange);
//            if (currentWeightChange > 10) {
//                System.out.println(getCurrentIteration() + " iteration : "+ currentWeightChange);
//            }
            qpData.previousWeightChange = currentWeightChange;
            qpData.prevGradient = gradient;
         //   qpData.previousError = delta;
            
        }
       
    }

    @Override
    protected void onStart() {
        super.onStart(); //To change body of generated methods, choose Tools | Templates.
        for (Layer layers : neuralNetwork.getLayers()) {
            for (Neuron neuron : layers.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    //connection.getWeight().setTrainingData(new QuickPropData());
                    Weight<QuickPropData> qpWeight = new Weight<>();
                    qpWeight.setTrainingData(new QuickPropData());
                    connection.setWeight(qpWeight);                    
                }
            }
        }

    }
    
    public static class QuickPropData {
        private double previousWeightChange;
        private double previousError; // we dont need this for weight
        private double prevGradient;
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