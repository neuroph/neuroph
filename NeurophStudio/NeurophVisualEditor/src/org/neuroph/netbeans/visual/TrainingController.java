package org.neuroph.netbeans.visual;

import java.io.PrintWriter;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.eval.ClassifierEvaluator;
import org.neuroph.eval.CrossValidation;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.SupervisedHebbianLearning;
import org.neuroph.util.data.sample.SubSampling;
import org.openide.util.Exceptions;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author zoran
 */
public class TrainingController implements Thread.UncaughtExceptionHandler {

    private NeuralNetAndDataSet neuralNetAndDataSet;
    private NeuralNetwork<?> neuralNet;
    private boolean isPaused; // we should be able to get this from neuralnetwork/learning rule
    private boolean useCrossvalidation;
    private int numberOfCrossvalSubsets;
    private int[] subsetDistribution;
    private CrossValidation crossval = null;
    private int crossValNNCounter = 0;
    boolean saveNetworks;
    private boolean allowSamplesRepetition;

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

    public boolean isSetUseCrossvalidation() {
        return useCrossvalidation;
    }

    public void setUseCrossvalidation(boolean useCrossvalidation) {
        this.useCrossvalidation = useCrossvalidation;
    }

    public int getNumberOfCrossvalSubsets() {
        return numberOfCrossvalSubsets;
    }

    public void setNumberOfCrossvalSubsets(int numberOfSubsets) {
        this.numberOfCrossvalSubsets = numberOfSubsets;
    }

    public NeuralNetAndDataSet getNeuralNetAndDataSet() {
        return neuralNetAndDataSet;
    }

    public void train() {
        InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
        io.select();

        final PrintWriter out = io.getOut();
        out.println("Starting neural network training...");
        out.print("Training network " + neuralNet.getLabel());
        out.println(" using data set " + neuralNetAndDataSet.getDataSet().getLabel());

        isPaused = false;

        if (useCrossvalidation == false) {
            // use thread pool here
            Thread t = new Thread( new Runnable () {
                
                @Override
                public void run() {
                    neuralNet.learn(neuralNetAndDataSet.getDataSet());
                }
            });
            t.setUncaughtExceptionHandler(this);
            t.setName("Training thread");
            t.start();

        } else {

            if (subsetDistribution != null) {
              //  crossval = new CrossValidation(neuralNet, neuralNetAndDataSet.getDataSet(), subsetDistribution);
            } else {
                crossval = new CrossValidation(neuralNet, neuralNetAndDataSet.getDataSet(), numberOfCrossvalSubsets);
            }
            
            if (allowSamplesRepetition)
                ((SubSampling)crossval.getSampling()).setAllowRepetition(true);
            
            String[] classNames = new String[neuralNet.getOutputsCount()]; 
            int i = 0;
            for (Neuron n : neuralNet.getOutputNeurons()) {
                classNames[i] = n.getLabel();
                i++;
            }
            
            crossval.addEvaluator(new ClassifierEvaluator.MultiClass(classNames)); // add multi class here manualy to make it independent from data set

            out.println("Running crossvalidation");

            neuralNet.getLearningRule().addListener(new LearningEventListener() {

                @Override
                public void handleLearningEvent(LearningEvent le) {
                    if (le.getEventType() == LearningEvent.Type.LEARNING_STOPPED) {
                        // save network here
                        if (saveNetworks) {
                            neuralNet.setLabel("crosValNet-"+crossValNNCounter);
                            NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNet);
                            crossValNNCounter++;
                        }
                    }
                }
            });
            
            
            (new Thread(new Runnable() {
                public void run() {
                    try {
                        crossval.run();
                    } catch (InterruptedException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (ExecutionException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                    out.println(crossval.getResult());
                }
            })).start();
        }

    }

    public void pause() {
        neuralNet.pauseLearning();
        isPaused = true;
    }

    public void resume() {
        neuralNet.resumeLearning();
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void stop() {
        neuralNet.stopLearning();
        isPaused = false;
    }

    public boolean isStoppedTraining() {
        return neuralNet.getLearningRule().isStopped();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (!(e instanceof ConcurrentModificationException)) { // Ugly fix, ovo se desava zbog ArrayListe u learning event listenerima, treba je zameniti sa synchronized Collections.synchronize
            InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
            io.select();

            PrintWriter out = io.getOut();
            out.println("Training exception: " + e.getMessage());
            e.printStackTrace(out);
        }
    }

    public void setCrossvalSubsetsDistribution(int[] dist) {
        subsetDistribution = dist;
    }

    public void setSaveNetworks(boolean selected) {
       this.saveNetworks = selected;
    }

    public void setAllowSamplesRepeat(boolean selected) {
        allowSamplesRepetition = selected;
    }

}
