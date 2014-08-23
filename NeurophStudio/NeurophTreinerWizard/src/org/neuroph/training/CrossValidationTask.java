package org.neuroph.training;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.util.Properties;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.data.sample.SubSampling;

/**
 * Move this to training framework
 * @author zoran
 */
public class CrossValidationTask extends Task {
    private NeuralNetwork neuralNet;
    private DataSet dataSet;
    
    Sampling sampling; // sampling method
    DataSet[] dataSetSamples;    // store k subsamples: 1 for testing, k-1 for training.
    int iteration = 0;
    int folds=0;
    
    // dodaj ,u i sampling method  i broj fodlova 2, 10 ili koliko vec, vrsta krossvalidaicje
    public CrossValidationTask(String name) {
        super(name);
    }

    
    /**
     * Runs the crossvalidation procedure for the specified neural network and data seet
     */
    public void run() {
         // partitioning a sample of data into complementary subsets
        
        // In k-fold cross-validation, the original sample is randomly partitioned into k equal size subsamples. (ne moraju da budu komplementarni)
        // k=2 2 fold, k=10
        
        // podeli trening na k subsetova. 1 koristi za testiranje, a k-1 trening
        
        // zapravo trebas da vrtis taskove za trening
        // setuj trening set u procesu i goto training task
            // ovo vec radi onaj sampling task
        // za test set treba da se uradi merenje performnsi
            // iskoristi goai framework ClassifierEvaliator i ClassifierPerformance
        
        // treba vrteti od postojeceg SamplingTask taska
        
        // da li raditi kroz training generator
        // ili odmah posle normalizacije za jedno podesavanje, uraditi kompletnu krosvalidaciju. Mislim da je ovo drugo bolje
        // mogao bih da imam jedan LoopTask za kroscalidaiju pre setProperties Loop-a - mislimd a je to najboja varijanta !!!
    }

    @Override
    public void execute() {
        if (iteration == 0) { // on first execution create subsets and set training and test set
            Properties trainingProperties = (Properties) getProcessVar("trainingProperties");
            Properties crossValProps = (Properties) trainingProperties.get("crossValidationProperties");

            dataSet = (DataSet) getProcessVar("dataSet");
            folds = 2; // (Integer)crossValProps.get("folds") ;        
            int trainingSetPercent = 50;
                
            logMessage("Sampling data set to create training and test sets: " + trainingSetPercent + " % / " + (100 - trainingSetPercent) + "%");

            this.sampling = new SubSampling(trainingSetPercent);
            //dataSetSamples = dataSet.sample(sampling);
            dataSetSamples = sampling.sample(dataSet);
            
            
            // types of subsampling: 
            // koliko subsetova KSubSampling 
            // da li se preklapaju
            // koji procenat

            parentProcess.setVar("testSet", dataSetSamples[0]);
            parentProcess.setVar("trainingSet", dataSetSamples[1]);            
        } else if (iteration+1 <= folds) { // on next execution only set next training sets
            parentProcess.setVar("trainingSet", dataSetSamples[iteration+1]);            
        } else { // when you finish with all training sets reset and startall over on next execution
          iteration = 0;  
        }
    }
    
    
}
