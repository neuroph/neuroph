package org.neuroph.netbeans.visual;

import java.io.PrintWriter;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.SupervisedHebbianLearning;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author zoran
 */
public class TrainingController implements Thread.UncaughtExceptionHandler {
    
    private NeuralNetAndDataSet neuralNetAndDataSet;
    private NeuralNetwork neuralNet;
    boolean isPaused; // we should be able to get this from neuralnetwork/learning rule

    public TrainingController(NeuralNetAndDataSet neuralNetAndDataSet) {
        this.neuralNetAndDataSet = neuralNetAndDataSet;
        this.neuralNet = neuralNetAndDataSet.getNetwork();
    }
    
    public void setLmsParams(Double learningRate, Double maxError,
            Integer maxIterations) {
        LMS lms = (LMS) this.neuralNet.getLearningRule();
        lms.setLearningRate(learningRate);
        lms.setMaxError(maxError);
        lms.setMaxIterations(maxIterations);
    }

    public void setHebbianParams(Double learningRate, Double maxError,
            Integer maxIterations) {
        SupervisedHebbianLearning hebbian = (SupervisedHebbianLearning) this.neuralNet
                .getLearningRule();
        hebbian.setLearningRate(learningRate);
    }

    public void setKohonenParams(Double learningRate, Integer Iphase,
            Integer IIphase) {
        KohonenLearning kl = (KohonenLearning) this.neuralNet.getLearningRule();
        kl.setLearningRate(learningRate.doubleValue());
        kl.setIterations(Iphase.intValue(), IIphase.intValue());
    }

    public void setStepDRParams(Double learningRate, Double maxError, Integer maxIterations) {
        BinaryDeltaRule sdr = (BinaryDeltaRule) this.neuralNet.getLearningRule();
        sdr.setLearningRate(learningRate);
        sdr.setMaxError(maxError);
        sdr.setMaxIterations(maxIterations);
    }    

    public NeuralNetAndDataSet getNeuralNetAndDataSet() {
        return neuralNetAndDataSet;
    }
    
    public void train() {
        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        io.select();

        PrintWriter out = io.getOut();
        out.println("Starting neural network training...");
        out.print("Training network " + neuralNet.getLabel());
        out.println(" using data set " + neuralNetAndDataSet.getDataSet().getLabel());
           
        isPaused = false;
        neuralNet.learnInNewThread(neuralNetAndDataSet.getDataSet());
        neuralNet.getLearningThread().setUncaughtExceptionHandler(this);
    }    
    
    public void pause() {
        neuralNet.pauseLearning();
        isPaused=true;
    }
    
    public void resume() {
        neuralNet.resumeLearning();        
        isPaused=false;
    }

    public boolean isPaused() {
        return isPaused;
    }
        
    public void stop() {
         neuralNet.stopLearning();
         isPaused=false;
    }
    
    public boolean isStoppedTraining() {
        return neuralNet.getLearningRule().isStopped();
    }    
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        io.select();

        PrintWriter out = io.getOut();
        out.println("Training error: " + e.getMessage());
    }    
    
    
}
