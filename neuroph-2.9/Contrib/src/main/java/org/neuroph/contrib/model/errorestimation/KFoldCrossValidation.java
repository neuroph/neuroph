package org.neuroph.contrib.model.errorestimation;

import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.contrib.eval.ClassificationEvaluator;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.data.sample.Sampling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.neuroph.contrib.eval.ErrorEvaluator;
import org.neuroph.contrib.eval.Evaluator;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.util.data.sample.SubSampling;

/**
 * Skeleton class which makes easy to implement concrete ErrorEstimationMethod
 * algorithms
 *
 *
 * Generate folds using some data sampling method iterate all folds: train with
 * one and test with othersing parameters
 *
 * print and summarize results
 *
 * mke it easy to be used with existing training procedure: providing neural
 * network and learn
 *
 *
 */
public class KFoldCrossValidation {

    private static Logger LOGGER = LoggerFactory.getLogger(KFoldCrossValidation.class.getName());
    
    /**
     * Neural network to train
     */
    private NeuralNetwork neuralNetwork;
    
    /**
     * Data set to use for training
     */
    private DataSet dataSet;

    /**
     * Data set sampling algorithm used. 
     * By default uses random subsampling without repetition
     */
    private Sampling sampling;

    /**
     * Evaluation procedure. Holds a collection of evaluators which can be automaticaly added
     */
    private Evaluation evaluation = new Evaluation();


    /**
     * Default constructor for creating KFold error estimation
     *
     * @param foldCount defines number of folds used in sampling algorithm
     */
    public KFoldCrossValidation(NeuralNetwork neuralNetwork, DataSet dataSet, int foldCount) { // number of folds
        this.neuralNetwork = neuralNetwork;
        this.dataSet = dataSet;    
        this.sampling = new SubSampling(foldCount); // new RandomSamplingWithoutRepetition(numberOfSamples                      
    }

    public void setSampling(Sampling sampling) {
        this.sampling = sampling;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }
    

    // kfolding is done here
    // provide neural network and data set - thi is the main entry point for crossvalidation
    public void run() {
     //   evaluation.addEvaluator(ClassificationEvaluator.createForDataSet(dataSet)); // this should be added elseewhere
        
        // create subsets of the entire datasets that will be used for k-folding
        List<DataSet> dataSets = sampling.sample(dataSet);

        //TODO Good place for parallelization. // But in order to make this possible NeuralNetwork must be cloneable or immutable
        for (int i = 0; i < dataSets.size(); i++) {
            neuralNetwork.randomizeWeights();       // we shouldnt do this - we should clone the original network
            neuralNetwork.learn(dataSets.get(i));   // train neural network with i-th data set fold

            for (int j = 0; j < dataSets.size(); j++) { // next do the testing with all other dataset folds
                if (j == i) {
                    continue; // dont use for testing the same dataset that was used for training
                }

              // testNetwork(neuralNetwork, dataSets.get(j));
               evaluation.evaluateDataSet(neuralNetwork,  dataSets.get(j));
               // we should also save all these trained network along w ith their evaluation results or at least store them intor array...
                
            }
        }
    }
    
    public void addEvaluator(Evaluator eval) {
        evaluation.addEvaluator(eval);
    }
    
    public <T extends Evaluator> T getEvaluator(Class<T> type) { 
        return evaluation.getEvaluator(type);
    }

    // TODO: dont sysout - store somewhere these results so they can be displayed
    // 
    private void testNetwork(NeuralNetwork<BackPropagation> neuralNetwork, DataSet testSet) {
        evaluation.evaluateDataSet(neuralNetwork, testSet);
        // works for binary what if we have multiple classes - how to get results for multiple classes here? 
  //      results.add(evaluation.getEvaluator(ClassificationMetricsEvaluator.class).getResult()[0]); // MUST BE FIXED!!!!! get all and add thm all to results
       
        System.out.println("##############################################################################");
        System.out.println("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult());
        System.out.println("##############################################################################");
      
        // TODO: deal with BinaryClassifiers too here
        ClassificationEvaluator evaluator = evaluation.getEvaluator(ClassificationEvaluator.MultiClass.class);
        
        
        ConfusionMatrix confusionMatrix = evaluator.getConfusionMatrix();        
        
        System.out.println("Confusion Matrix: \r\n"+confusionMatrix.toString());
                      
        System.out.println("##############################################################################");
        System.out.println("Classification metrics: ");        
        ClassificationMetrics[] metrics = evaluator.getResult();     // add all of these to result 
     
        for(ClassificationMetrics cm : metrics)
            System.out.println(cm.toString());

        System.out.println("##############################################################################");        
    }



}
