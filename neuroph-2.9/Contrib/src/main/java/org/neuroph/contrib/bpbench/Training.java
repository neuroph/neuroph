/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.bpbench;

import java.util.Arrays;
import org.neuroph.contrib.eval.ClassifierEvaluator;
import org.neuroph.contrib.eval.Evaluation;
import org.neuroph.contrib.eval.EvaluationResult;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mladen
 */
public abstract class Training implements LearningEventListener {

    private final NeuralNetwork neuralNet;
    private final DataSet dataset;
    
    private TrainingStatistics stats;
    private TrainingSettings settings;
   
    public abstract void testNeuralNet();
    public abstract void setParameters(BackPropagation bp);
    
    public ConfusionMatrix createMatrix(){
        Evaluation eval = new Evaluation();
        eval.addEvaluator(new ClassifierEvaluator.MultiClass(dataset.getColumnNames()));
        return eval.evaluateDataSet(neuralNet, dataset).getConfusionMatrix();
    }
   
    public Training(NeuralNetwork neuralNet, DataSet dataset, TrainingSettings settings) {
        if(neuralNet != null){
            this.neuralNet = neuralNet;
        }
        else  {
            this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, dataset.getInputSize(), settings.getHiddenNeurons(), dataset.getOutputSize());
        }
        this.dataset = dataset;
        this.stats = new TrainingStatistics();
        this.settings = settings;
    }

    public TrainingSettings getSettings() {
        return settings;
    }

    public void setSettings(TrainingSettings settings) {
        this.settings = settings;
    }

    public DataSet getDataset() {
        return dataset;
    }

  

    public NeuralNetwork getNeuralNet() {
        return neuralNet;
    }

 

    @Override
    public void handleLearningEvent(LearningEvent le) {
        BackPropagation bp = (BackPropagation) le.getSource();
        if (le.getEventType() != LearningEvent.Type.LEARNING_STOPPED) {
            System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
        }
       
    }
/*
   public void testNeuralNetwork(NeuralNetwork neuralNet, DataSet testSet) {

        for (DataSetRow testSetRow : testSet.getRows()) {
            //Poredim poziciju jedinice u desired output i max vrednost u networkOutput
           
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
            double[] networkOutput = neuralNet.getOutput();
            int desiredPos = -1;
            int outputPos = -1;
            double desiredMax = 0;
            double outputMax = 0;
            for (int i = 0; i < networkOutput.length; i++) {
                if(networkOutput[i] > outputMax){
                    outputMax = networkOutput[i];
                    outputPos = i;
                }
                if(testSetRow.getDesiredOutput()[i] > desiredMax){
                    desiredMax = testSetRow.getDesiredOutput()[i];
                    desiredPos = i;
                }
            }
            if(desiredPos == outputPos) stats.correctGuess++;
           
  
            //System.out.print("Input: " + Arrays.toString(testSetRow.getInput()));
            System.out.print("Desired output: " + Arrays.toString(testSetRow.getDesiredOutput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }
    }
*/
    public TrainingStatistics getStats() {
        return stats;
    }

    public void setStats(TrainingStatistics stats) {
        this.stats = stats;
    }

}
