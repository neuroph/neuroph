package org.neuroph.contrib.autotrain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.contrib.eval.classification.ConfusionMatrix;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Milan Brkic - milan.brkic1@yahoo.com
 */
public class AutoTrainer implements Trainer {

    private final static Logger LOGGER = Logger.getLogger(AutoTrainer.class.getName());
    private List<TrainingSettings> trainingSettingsList;

    private List<TrainingResult> results;

    // Range(min, Max)
    private int maxHiddenNeurons;
    private int minHiddenNeurons;
    private int hiddenNeuronsStep = 1;

    private double minLearningRate;
    private double maxLearningRate;
    private double learningRateStep = 0.1;

    private double maxMomentum = 0.9;

    private int splitPercentage = 100;
    private boolean splitTrainTest = false;

    private double maxErrorMin;
    private double maxErrorMax;
    private double maxErrorStep = 0.01;

    private int maxIterations;
    private TransferFunctionType transferFunction;

    private boolean generateStatistics = false;
    private int repeat = 1;

    /**
     *
     */
    public AutoTrainer() {
        trainingSettingsList = new ArrayList<>();
        results = new ArrayList<>();
        transferFunction = TransferFunctionType.SIGMOID;
    }

    public AutoTrainer(double maxError, int maxIterations) {
        this();
        this.maxErrorMin = maxError;
        this.maxIterations = maxIterations;
    }

    /**
     * Get results.
     *
     * @return List of TrainingResult. If nothing was trained, method returns
     * empty ArrayList().
     *
     */
    public List<TrainingResult> getResults() {
        return results;
    }

    /**
     * Set range for hidden neurons of neural network. Auto trainer is looping
     * through that range with increment equals to step.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setHiddenNeurons(int minHiddenNeurons, int maxHiddenNeurons, int step) {
        this.minHiddenNeurons = minHiddenNeurons;
        this.maxHiddenNeurons = maxHiddenNeurons;
        this.hiddenNeuronsStep = step;
        return this;
    }

    /**
     * Set range for hidden neurons of neural network. Auto trainer is looping
     * through that range.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setHiddenNeurons(int minHiddenNeurons, int maxHiddenNeurons) {
        this.minHiddenNeurons = minHiddenNeurons;
        this.maxHiddenNeurons = maxHiddenNeurons;
        return this;
    }

    /**
     * Set range for hidden neurons of neural network. Auto trainer is looping
     * through that range with increment equals to step.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setHiddenNeurons(Range range, int step) {
        this.minHiddenNeurons = (int) range.getMin();
        this.maxHiddenNeurons = (int) range.getMax();
        this.hiddenNeuronsStep = step;
        return this;
    }

    /**
     * Set range for hidden neurons of neural network. Auto trainer is looping
     * through that range.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setHiddenNeurons(Range range) {
        this.minHiddenNeurons = (int) range.getMin();
        this.maxHiddenNeurons = (int) range.getMax();
        return this;
    }

    /**
     * Set range for maximum error of neural network. Auto trainer is looping
     * through that range with increment equals to step.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setMaxError(double maxErrorMin, double maxErrorMax, double step) {
        this.maxErrorMin = maxErrorMin;
        this.maxErrorMax = maxErrorMax;
        this.maxErrorStep = step;
        return this;
    }

    /**
     * Set range for maximum error of neural network. Auto trainer is looping
     * through that range.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setMaxError(double maxErrorMin, double maxErrorMax) {
        this.maxErrorMin = maxErrorMin;
        this.maxErrorMax = maxErrorMax;
        return this;
    }

    /**
     * Set range for maximum error of neural network. Auto trainer is looping
     * through that range with increment equals to step.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setMaxError(Range range, double step) {
        this.maxErrorMin = range.getMin();
        this.maxErrorMax = range.getMax();
        this.maxErrorStep = step;
        return this;
    }

    /**
     * Set range for maximum error of neural network. Auto trainer is looping
     * through that range.
     *
     * @param range given for hidden neurons
     */
    public AutoTrainer setMaxError(Range range) {
        this.maxErrorMin = range.getMin();
        this.maxErrorMax = range.getMax();
        return this;
    }

    /**
     * Set range for learning rate of neural network. Auto trainer is looping
     * through that range with increment equals to step.
     *
     * @param range given for learning rate
     */
    public AutoTrainer setLearningRate(double minLearningRate, double maxLearningRate, double step) {
        this.minLearningRate = minLearningRate;
        this.maxLearningRate = maxLearningRate;
        learningRateStep = step;
        return this;
    }

    /**
     * Set range for learning rate of neural network. Auto trainer is looping
     * through that range.
     *
     * @param range given for learning rate
     */
    public AutoTrainer setLearningRate(double minLearningRate, double maxLearningRate) {
        this.minLearningRate = minLearningRate;
        this.maxLearningRate = maxLearningRate;
        return this;
    }

    /**
     * Set range for learning rate of neural network. Auto trainer is looping
     * through that range with increment equals to step.
     *
     * @param range given for learning rate
     */
    public AutoTrainer setLearningRate(Range range, double step) {
        this.minLearningRate = range.getMin();
        this.maxLearningRate = range.getMax();
        learningRateStep = step;
        return this;
    }

    /**
     * Set range for learning rate of neural network. Auto trainer is looping
     * through that range.
     *
     * @param range given for learning rate
     */
    public AutoTrainer setLearningRate(Range range) {
        this.minLearningRate = range.getMin();
        this.maxLearningRate = range.getMax();
        return this;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public AutoTrainer setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    public TransferFunctionType getTransferFunction() {
        return transferFunction;
    }

    public AutoTrainer setTransferFunction(TransferFunctionType transferFunction) {
        this.transferFunction = transferFunction;
        return this;
    }

    /**
     *
     * @param maxMomentum
     */
    public void setMaxMomentum(double maxMomentum) {
        this.maxMomentum = maxMomentum;
    }

    /**
     * Repeat neural network with same parameters specified number of times and
     * create statistic.
     *
     * @param times to repeat network
     */
    public AutoTrainer repeat(int times) {
        this.repeat = times;
        generateStatistics = true;
        return this;
    }

    /**
     *
     * @return if statistic is enabled
     */
    protected boolean generatesStatistics() {
        return generateStatistics;
    }

    /**
     * Set percentage of training set (in percents).
     *
     * @param trainingPrecent new value of splitPercentage
     */
    public AutoTrainer setTrainTestSplit(int trainingPrecent) {
        this.splitPercentage = trainingPrecent;
        this.splitTrainTest = true;
        return this;
    }

    /**
     *
     * @return true if split
     */
    protected boolean isSplitForTesting() {
        return splitTrainTest;
    }

    private void generateTrainingSettings() {
        double pom = minLearningRate;
        for (int hiddenNeurons = minHiddenNeurons; hiddenNeurons <= maxHiddenNeurons; hiddenNeurons += hiddenNeuronsStep) {
            for (double learningRate = minLearningRate; learningRate <= maxLearningRate; learningRate += learningRateStep) {
                //MOMENTUM for (double momentum = 0.1; momentum < maxMomentum; momentum += 0.1) { proveriti za sta je potreban momentum i kako se koristi!
                for (double maxError = maxErrorMin; maxError <= maxErrorMax; maxError += maxErrorStep) {
                    TrainingSettings ts = new TrainingSettings()
                            .setHiddenNeurons(hiddenNeurons)
                            .setLearningRate(learningRate)
                            .setMaxError(maxError)
                            .setMaxIterations(getMaxIterations());

                    this.trainingSettingsList.add(ts);
                    //}
                }
            }
            minLearningRate = pom;
        }
        LOGGER.log(Level.INFO, "Generated : {0} settings.", this.trainingSettingsList.size());
    }

    // todo: remove
    @Override
    public void train(NeuralNetwork neuralNet, DataSet dataSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * You can get results calling getResults() method.
     *
     * @param neuralNetwork type of neural net
     * @param dataSet
     */
    @Override
    public void train(DataSet dataSet) {// mozda da se vrati Training setting koji je najbolje resenje za dati dataset.??
        generateTrainingSettings();
        List<TrainingResult> statResults = null;
        DataSet trainingSet, testSet; // validationSet;

        if (splitTrainTest) {
            List<DataSet> dataSplit = dataSet.split(splitPercentage, 100-splitPercentage); //opet ne radi Maven za neuroph 2.92
            trainingSet = dataSplit.get(0);
            testSet = dataSplit.get(1);
        } else {
            trainingSet = dataSet;
            testSet = dataSet;
        }

        if (generateStatistics) {
            statResults = new ArrayList<>();
        }

        int trainingNo = 0;
        for (TrainingSettings trainingSetting : trainingSettingsList) {
            System.out.println("-----------------------------------------------------------------------------------");
            trainingNo++;
            System.out.println("##TRAINING: " + trainingNo);
            trainingSetting.setTrainingSet(splitPercentage);
            trainingSetting.setTestSet(100 - splitPercentage);
            //int subtrainNo = 0;
             
            for (int subtrainNo = 1; subtrainNo <= repeat; subtrainNo++) {
                System.out.println("#SubTraining: " + subtrainNo);

                MultiLayerPerceptron neuralNet
                        = new MultiLayerPerceptron(dataSet.getInputSize(), trainingSetting.getHiddenNeurons(), dataSet.getOutputSize());

                BackPropagation bp = neuralNet.getLearningRule();

                bp.setLearningRate(trainingSetting.getLearningRate());
                bp.setMaxError(trainingSetting.getMaxError());
                bp.setMaxIterations(trainingSetting.getMaxIterations());

                neuralNet.learn(trainingSet);
//                  testNeuralNetwork(neuralNet, testSet); // not implemented
                ConfusionMatrix cm = new ConfusionMatrix(new String[]{""});
                TrainingResult result = new TrainingResult(trainingSetting, bp.getTotalNetworkError(), bp.getCurrentIteration(),cm);
                System.out.println(subtrainNo + ") iterations: " + bp.getCurrentIteration());

                if (generateStatistics) {
                    statResults.add(result);
                } else {
                    results.add(result);
                }

            }

            if (generateStatistics) {
                TrainingResult trainingStats = calculateTrainingStatistics(trainingSetting, statResults);
                results.add(trainingStats);
                statResults.clear();
            }

        }

    }

    private TrainingResult calculateTrainingStatistics(TrainingSettings ts, List<TrainingResult> results) {
        System.out.println("working on statistic...");
        TrainingResult result = new TrainingResult(ts);

        TrainingStatistics iterationsStat = TrainingStatistics.calculateIterations(results);
        TrainingStatistics MSEStat = TrainingStatistics.calculateMSE(results);

        result.setMSE(MSEStat);
        result.setIterationStat(iterationsStat);

        return result;
    }

    private void testNeuralNetwork(MultiLayerPerceptron neuralNet, DataSet testSet) {
        // not implemented
        for (DataSetRow testSetRow : testSet.getRows()) {
            neuralNet.setInput(testSetRow.getInput());
            neuralNet.calculate();
        }
    }

}
