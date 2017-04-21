package org.neuroph.contrib.bpbench;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mladen
 */
public class BackPropBenchmarks {
 
    private List<Training> listOfTrainings;
    private int noOfRepetitions;

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
            for (int i = 0; i < noOfRepetitions; i++) {
                training.testNeuralNet();
               
                training.getNeuralNet().randomizeWeights();
            }
             System.out.println(training.getStats());
        }
        
    }

    public int getNoOfRepetitions() {
        return noOfRepetitions;
    }

    public void setNoOfRepetitions(int noOfRepetitions) {
        this.noOfRepetitions = noOfRepetitions;
    }
  
}
