package org.neuroph.contrib.bpbench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * Base class for benchmarking of backpropagation algorithms. Defines list of
 * trainings and number of repetitions of each treaning.
 *
 * @author Mladen Savic <mladensavic94@gmail.com>
 */
public class BackPropBenchmarks {

    /**
     * List of all training`s (combinations of different parameters) to execute.
     */
    private List<AbstractTraining> listOfTrainings;
    /**
     * Number of iterations each training must be completed;
     */
    private int noOfRepetitions = 1;

    /**
     * Create an instance with empty list of training`s
     */
    public BackPropBenchmarks() {
        this.listOfTrainings = new ArrayList<>();
    }

    /**
     * Create an instance with given list of training`s
     *
     * @param listOfTasks list of training`s
     */
    public BackPropBenchmarks(List<AbstractTraining> listOfTasks) {
        this.listOfTrainings = listOfTasks;
    }

    /**
     * Adds new training to list
     *
     * @param training
     */
    public void addTraining(AbstractTraining training) {
        this.listOfTrainings.add(training);
    }

    /**
     * Executes all training`s from list with predefined number of repetitions
     * and resets neural net
     */
    public void run() {
        for (AbstractTraining training : listOfTrainings) {
            for (int i = 0; i < noOfRepetitions; i++) {
                training.testNeuralNet();
                training.getNeuralNet().randomizeWeights();
            }
            System.out.println(training.getStats());
        }

    }

    /**
     * Returns number of repetitions for each training
     *
     * @return number of repetitions
     */
    public int getNoOfRepetitions() {
        return noOfRepetitions;
    }

    /**
     * Adds number of repetitions
     *
     * @param noOfRepetitions
     */
    public void setNoOfRepetitions(int noOfRepetitions) {
        this.noOfRepetitions = noOfRepetitions;
    }
/**
 * Creates all training`s using list training types, settings and neural network and execute run method
 * @param trainingTypeCollection
 * @param settingsCollection
 * @param trainingSet
 * @param mlp 
 */
   public void startBenchmark(List<Class<? extends AbstractTraining>> trainingTypeCollection, List<TrainingSettings> settingsCollection, DataSet trainingSet, MultiLayerPerceptron mlp) {
        for (TrainingSettings trainingSettings : settingsCollection) {
            for (Class<? extends AbstractTraining> trainingType : trainingTypeCollection) {
                AbstractTraining training = null;
                if (trainingType.equals(BackpropagationTraining.class)) {
                    training = new BackpropagationTraining(mlp, trainingSet, trainingSettings);                   
                } else if (trainingType.equals(MomentumTraining.class)) {
                    training = new MomentumTraining(mlp, trainingSet, trainingSettings);
                } else if (trainingType.equals(QuickpropagationTraining.class)) {
                    training = new QuickpropagationTraining(mlp, trainingSet, trainingSettings);
                } else if (trainingType.equals(ResilientTraining.class)) {
                    training = new ResilientTraining(mlp, trainingSet, trainingSettings);
                }
                this.addTraining(training);
            }
        }
        this.run();
        
    }
/**
 * Creates all training`s using list training types, settings and execute run method
 * @param trainingTypeCollection
 * @param settingsCollection
 * @param trainingSet 
 */
   public void startBenchmark(List<Class<? extends AbstractTraining>> trainingTypeCollection, List<TrainingSettings> settingsCollection, DataSet trainingSet) {
        for (TrainingSettings trainingSettings : settingsCollection) {
            for (Class<? extends AbstractTraining> trainingType : trainingTypeCollection) {
                AbstractTraining training = null;
                if (trainingType.equals(BackpropagationTraining.class)) {
                    training = new BackpropagationTraining(trainingSet, trainingSettings);
                } else if (trainingType.equals(MomentumTraining.class)) {
                    training = new MomentumTraining(trainingSet, trainingSettings);
                } else if (trainingType.equals(QuickpropagationTraining.class)) {
                    training = new QuickpropagationTraining(trainingSet, trainingSettings);
                } else if (trainingType.equals(ResilientTraining.class)) {
                    training = new ResilientTraining(trainingSet, trainingSettings);
                }
                this.addTraining(training);
            }
        }
        this.run();
    }
/**
 * Save result of benchmarking in given location as csv
 * @param filePath 
 */
    public void saveResults(String filePath) {
        try {
            ExportUtil.exportToCSV(filePath, listOfTrainings);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
