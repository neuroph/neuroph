package org.neuroph.contrib.bpbench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mladen
 */
public class BackPropBenchmarks {
 
    private List<Training> listOfTrainings;

    public BackPropBenchmarks() {
        this.listOfTrainings = new ArrayList<>();
    }

    public BackPropBenchmarks(List<Training> listOfTasks) {
        this.listOfTrainings = listOfTasks;
    }
    
    public void addTraining(Training training){
        this.listOfTrainings.add(training);
    }
    public void run(){
        for (Training training : listOfTrainings) {
            training.testNeuralNet();
            
        }
        
    }
    //TrainingSettingsGenerator - generise testove za raspon vrednosti learning rate min i max, generise i broj skrivenih neurona
    public static void main(String[] args) throws IOException {
        // lista treninga mreza sa razlicitim algoritmima
        // napravi klasu Training (neuronsku mrezu i dataset, mozda podesavanje treninga)
        //  Driver: testirati standardni backprop, momentum, resilient i quickprop
        // za svaki trening statistike: min, max, mean, std
        
        //Trening je jedna iteracija algoritma ili n ponavljanja?????
        //Upotreba niti za testiranje?
        //Matrica konfuzije da se izracuna!
        BackPropBenchmarks bpb = new BackPropBenchmarks();
        DataSet trainingSet = DataSet.createFromFile("iris_data_normalised.txt", 4, 3, ",");
        MultiLayerPerceptron mlp = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 4, 7, 3);
        
        TrainingSettings settings = new TrainingSettings(0.2, 0, 10, 0, 0.1, true);
        //Training t1 = new MomentumTraining((NeuralNetwork)mlp, trainingSet);
        Training t2 = new BackpropagationTraining((NeuralNetwork)mlp, trainingSet, settings);
        bpb.addTraining(t2);
       // bpb.addTraining(t1);
        for (int i = 0; i < 10; i++) {
             bpb.run();
        }
         
       
        /*
        RunBackpropagation x = new RunBackpropagation();
        RunQuickPropagation q = new RunQuickPropagation();
        
        PrintWriter out = new PrintWriter(new FileWriter("rezultati.txt"));
        int backprop = 0;
        int quickprop = 0;
//       for (int i = 0; i < 5; i++) {
//            x.nauci();
//            out.println("BACK! iter: " + x.noOfIter);
//            backprop += x.noOfIter;
//        }
        out.println("BACK! ukupno iteracija: " + backprop);
       for (int i = 0; i < 5; i++) {
            q.nauci();
            out.println("QUICK! iter: " + q.noOfIter);
            quickprop += q.noOfIter;
        }
        out.println("QUICK! ukupno iteracija: " + quickprop);
        out.flush();
        out.close();
*/
    }
}
