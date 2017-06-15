package org.neuroph.eval;

import java.util.ArrayList;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.util.data.sample.Sampling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.util.data.sample.SubSampling;

/**
 * This class implements multithreaded cross validation procedure. 
 * Splits data set into k subsets (folds), trains the network with data from k-1 and tests with one subset
 * Repeats the procedure k times each time using different subset for testing.
 *
 * @author Boris Fulurija
 * @author Lukic Sasa Multithreading
 * @author Zoran Sevarac
 */
public class CrossValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossValidation.class.getName());

    /**
     * Neural network to train
     */
    private NeuralNetwork neuralNetwork;

    /**
     * Data set to use for training
     */
    private DataSet dataSet;

    /**
     * Data set sampling algorithm used. By default uses random subsampling
     * without repetition
     */
    private Sampling sampling;

    private int numberOfFolds;
    private int foldSize;

    /**
     * Evaluation procedure. Holds a collection of evaluators which can be
     * automatically added
     */
    private final Evaluation evaluation = new Evaluation();

    private CrossValidationResult results;

    private void initialize(NeuralNetwork neuralNetwork, DataSet dataSet, int numberOfFolds) {
        this.neuralNetwork = neuralNetwork;
        this.numberOfFolds = numberOfFolds;
        this.dataSet = dataSet;
        if (neuralNetwork.getOutputsCount() == 1) {
            this.evaluation.addEvaluator(new ClassifierEvaluator.Binary(0.5));
        } else {
            this.evaluation.addEvaluator(new ClassifierEvaluator.MultiClass(dataSet.getColumnNames()));
        }
        this.evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
    }

    /**
     * Creates a new instance of crrossvalidation for specified neural network, data set and number of folds.
     *
     * @param neuralNetwork
     * @param dataSet
     * @param foldCount number of folds to use
     */
    public CrossValidation(NeuralNetwork neuralNetwork, DataSet dataSet, int foldCount) { // number of folds/subsets
        initialize(neuralNetwork, dataSet, foldCount);
        this.sampling = new SubSampling(foldCount); // new RandomSamplingWithoutRepetition(numberOfSamples   
    }

    public Sampling getSampling() {
        return sampling;
    }

    public void setSampling(Sampling sampling) {
        this.sampling = sampling;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void run() throws InterruptedException, ExecutionException {

        results = new CrossValidationResult();
        this.results.numberOfFolds = this.numberOfFolds;
        this.results.numberOfInstances = this.dataSet.getRows().size();

        dataSet.shuffle();
        foldSize = dataSet.size() / numberOfFolds;

        ArrayList<CrossValidationWorker> workersTasks = new ArrayList<>();
        for (int foldIdx = 0; foldIdx < numberOfFolds; foldIdx++) {
            workersTasks.add(new CrossValidationWorker(neuralNetwork, dataSet, foldIdx)); // invokeAll Futire
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<EvaluationResult>> evaluationResults = executor.invokeAll(workersTasks);
        executor.shutdown();

        for (Future<EvaluationResult> evaluationResult : evaluationResults) {
            results.addEvaluationResult(evaluationResult.get());
        }

        results.calculateStatistics();
    }

    public void addEvaluator(Evaluator eval) {
        evaluation.addEvaluator(eval);
    }

    public <T extends Evaluator> T getEvaluator(Class<T> type) {
        return evaluation.getEvaluator(type);
    }

    public CrossValidationResult getResult() {
        return results;
    }

    private class CrossValidationWorker implements Callable<EvaluationResult> {

        private final NeuralNetwork neuralNetwork;
        private final DataSet dataSet;
        private final int foldIndex;

        public CrossValidationResult getResults() {
            return results;
        }

        public Evaluation getEvaluation() {
            return evaluation;
        }

        public CrossValidationWorker(NeuralNetwork neuralNetwork, DataSet dataSet, int foldIndex) {
            this.neuralNetwork = neuralNetwork;
            this.dataSet = dataSet;
            this.foldIndex = foldIndex;
        }

        @Override
        public EvaluationResult call() {
            NeuralNetwork neuralNet = SerializationUtils.clone(this.neuralNetwork);

            DataSet trainingSet = new DataSet(dataSet.size() - foldSize);
            DataSet testSet = new DataSet(foldSize);

            int startIndex = foldSize * foldIndex;
            int endIndex = foldSize * (foldIndex + 1);

            for (int i = 0; i < dataSet.size(); i++) {
                if (i >= startIndex && i < endIndex) {
                    testSet.add(dataSet.getRowAt(i));
                } else {
                    trainingSet.add(dataSet.getRowAt(i));
                }
            }
            neuralNet.learn(trainingSet);
            EvaluationResult evaluationResult = new EvaluationResult();
            evaluationResult.setNeuralNetwork(neuralNet);
            evaluationResult = evaluation.evaluateDataSet(neuralNet, testSet);
            return evaluationResult;
        }

        /*private void testNetwork(NeuralNetwork<BackPropagation> neuralNetwork, DataSet testSet) {
        EvaluationResult evaluationResult = evaluation.evaluateDataSet(neuralNetwork, testSet); // this method should return all evaluation results
        
        results.addEvaluationResult(evaluationResult);
        //mean square error
        System.out.println("MeanSquare Error: " + evaluation.getEvaluator(ErrorEvaluator.class).getResult());
        ClassifierEvaluator evaluator;
        if (neuralNetwork.getOutputsCount() == 1) {
        evaluator = evaluation.getEvaluator(ClassifierEvaluator.Binary.class);
        } else {
        evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        }
        
        //confusion matrix
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        System.out.println("Confusion Matrix: \r\n" + confusionMatrix.toString());
        
        //classifivation metrics
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);     // add all of these to result
        
        for (ClassificationMetrics cm : metrics) {
        System.out.println(cm.toString());
        }
        }*/
    }
}
