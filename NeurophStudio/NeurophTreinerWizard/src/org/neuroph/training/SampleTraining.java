package org.neuroph.training;

import org.neuroph.util.data.norm.MaxNormalizer;



/**
 * TODO: normalizacija, sampling i testiranje, krosvalidacija
 * kao i razni scenariji/tokovi treninga: validacija
 * max iterations max error
 * navodjenje vrednosti i intervala u opsegu min max za sve parametre
 * add normalization method i sampling method settings
 * 
 * @author zoran
 */
public class SampleTraining {
    
    
    public static void main(String[] args) {
        
        Process trainingProcess = new Process();

//---------------------------------------------------------------------         

        // Generates different settings for neural network and training process as Stack<Properties>
        // it would be great to load all these settings from XML file
        // Creates process var: processPropertiesStack                
        // izgenerise sva podesavanja i stavi na stek. i onda se proces izvrsava za jedno po jedno podesavanje sa steka        
        trainingProcess.addTask(new TrainingPropertiesGeneratorTask("trainingPropertiesGenerator", "trainingPropertiesStack"));
        // add  max iterations and max error
        
//---------------------------------------------------------------------        
        
        // DONE!
        // Setting process properties for a single training iteration, by poping one from stack
        // Expects input process var: trainingPropertiesStack
        // Creates process var: trainingProperties
        trainingProcess.addTask(new SetTrainingPropertiesTask("setProperties", "trainingPropertiesStack", "trainingProperties"));
        
//---------------------------------------------------------------------
        
        // DONE!
        // Loads data set from file into the process (if its not allready loaded)
        // Expects input process var:  dataSetProperties
        // Creates process var: dataSet
        DataSetLoaderTask dataLoaderTask = new DataSetLoaderTask("dataSetLoader", "dataSetProperties", "dataSet");
        trainingProcess.addTask(dataLoaderTask);
        
//---------------------------------------------------------------------
        
        // Normalize training set. Note that it is normalizing existing instance of data set
        // Expects process var 'dataSet'
        NormalizationTask normalizationTask = new NormalizationTask("normalizationTask", "dataSet",  new MaxNormalizer());
//        NormalizationTask normalizationTask = new NormalizationTask("normalizationTask", "dataSet",  "normalizationMethod");
        trainingProcess.addTask(normalizationTask);
                
//---------------------------------------------------------------------        
        
        // DONE!
        // Ceates neural network and adds it to the process
        // Expects process var: neuralNetworkProperties
        // Creates process var: neuralNetwork
        trainingProcess.addTask(new MultiLayerPerceptronFactoryTask("neuralNetFactory", "neuralNetworkProperties", "neuralNetwork"));
        
//---------------------------------------------------------------------
        
        // add some sampling task here, we need different settings for percent of data set which will be used for training
        // generate training and test sets as subsets of data set
        // repeat training with different training and test sets in order to get best generalization
        // Expects process var: dataSet and trainingSetPercent
        // Creates process vars: trainingSet, testSet
        // same as NormalizationTask
     //   trainingProcess.setVar("trainingSetPercent", 70); // percent should be set in processProperties, and generated with different values
            trainingProcess.addTask(new SamplingTask("trainingSetSampling"));
            // , "dataSet", "trainingSet", "testSet"
        
//---------------------------------------------------------------------        
        
        // DONE!
        // Trains neural network using and data set within this process
        // Expects process vars: neuralNetwork, trainingSet
        TrainingTask trainingTask = new TrainingTask("training", "neuralNetwork", "trainingSet"); 
        trainingProcess.addTask(trainingTask);
                                
//---------------------------------------------------------------------                
        
        // Tests neural network with  testSet
        // Expects process vars: neuralNetwork, testSet
        // It should output test results
        TestTask testTask = new TestTask("test", "neuralNetwork", "testSet"); // add test set here
        trainingProcess.addTask(testTask);
        
//---------------------------------------------------------------------                

        // koliko puta da ponovi, idx od kog taska
        // broj poavljanaj treba sam da odredi na osnovu processProperties
        // properties problemsa stackom ako se iscitava na vise mesta
        trainingProcess.addTask(new LoopTask("neuralNetFactory", 2)); // repeat while  there are settngs
        
//---------------------------------------------------------------------                        
        
        trainingProcess.addTask(new ReportTask("report"));        
        trainingProcess.execute();        
       
//---------------------------------------------------------------------                        
        // summary report
        // how many trainings
        // mse min max avg sd err, iterations

    }
    
}
