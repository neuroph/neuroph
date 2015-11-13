package org.neuroph.contrib.model.modelselection;

import org.neuroph.contrib.model.errorestimation.Bootstrapping;
import org.neuroph.contrib.model.errorestimation.CrossValidation;
import org.neuroph.contrib.eval.classification.ClassificationMetrics;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.neuroph.contrib.eval.ClassifierEvaluator;
import org.neuroph.contrib.model.errorestimation.CrossValidation;

/**
 * @param <T> Type which defined which LearningRule will be used during model optimization
 */
public class MultilayerPerceptronOptimazer<T extends BackPropagation> implements NeurophModelOptimizer {

    private static Logger LOG = LoggerFactory.getLogger(MultilayerPerceptronOptimazer.class);

    /**
     *
     */
    private Set<List<Integer>> allArchitectures = new HashSet<>();

    private List<Integer> optimalArchitecure;
    /**
     * Optimal optimizer which will be selected during optimization process
     */
    private NeuralNetwork<BackPropagation> optimalClassifier;
    /**
     * Average metric scores for selected optimal classififer
     */
    private ClassificationMetrics optimalResult;
    /**
     * Method used for classifier error estimation (KFold, Bootstrap)
     */
    private CrossValidation errorEstimationMethod;
    /**
     * Learning rule used during classifier learning stage
     */
    private BackPropagation learningRule;

    private int maxLayers = 1;
    private int minNeuronsPerLayer = 1;
    private int maxNeuronsPerLayer = 30;
    private int neuronIncrement = 1;

    /**
     * If ErrorEstimationMethod is not provided use KFoldCrossValidation by default
     */
    public MultilayerPerceptronOptimazer() {
       // errorEstimationMethod = new KFoldCrossValidation(10);
    }

    public MultilayerPerceptronOptimazer withMaxLayers(int maxLayers) {
        this.maxLayers = maxLayers;
        return this;
    }

    public MultilayerPerceptronOptimazer withNeuronIncrement(int neuronIncrement) {
        this.neuronIncrement = neuronIncrement;
        return this;
    }

    public MultilayerPerceptronOptimazer withMaxNeurons(int maxNeurons) {
        this.maxNeuronsPerLayer = maxNeurons;
        return this;
    }

    public MultilayerPerceptronOptimazer withMinNeurons(int minNeurons) {
        this.minNeuronsPerLayer = minNeurons;
        return this;
    }


    public MultilayerPerceptronOptimazer withErrorEstimationMethod(CrossValidation errorEstimationMethod) {
        this.errorEstimationMethod = errorEstimationMethod;
        return this;
    }

    public MultilayerPerceptronOptimazer withLearningRule(BackPropagation learningRule) {
        this.learningRule = learningRule;
        return this;
    }


    /**
     * @param dataSet training set used for error estimation
     * @return neural network model with optimized architecture for provided data set
     */
    @Override
    public NeuralNetwork createOptimalModel(DataSet dataSet) {

        List<Integer> neurons = new ArrayList<>();
        neurons.add(minNeuronsPerLayer);
        findArchitectures(1, minNeuronsPerLayer, neurons);

        LOG.info("Total [{}] different network topologies found", allArchitectures.size());

        for (List<Integer> architecture : allArchitectures) {
            architecture.add(0, dataSet.getInputSize());
            architecture.add(dataSet.getOutputSize());

            LOG.info("Architecture: [{}]", architecture);

            MultiLayerPerceptron network = new MultiLayerPerceptron(architecture);
            LearningListener listener = new LearningListener(10, learningRule.getMaxIterations());
            learningRule.addListener(listener);
            network.setLearningRule(learningRule);
            
            errorEstimationMethod = new CrossValidation(network, dataSet, 10);
            errorEstimationMethod.run();
            // FIX
            ClassificationMetrics[] result = ClassificationMetrics.createFromMatrix(errorEstimationMethod.getEvaluator(ClassifierEvaluator.MultiClass.class).getResult());

            // nadji onaj sa najmanjim f measure
            if (optimalResult == null || optimalResult.getFMeasure()< result[0].getFMeasure()) {
                LOG.info("Architecture [{}] became optimal architecture  with metrics {}", architecture, result);
                optimalResult = result[0];
                optimalClassifier = network;
                optimalArchitecure = architecture;
            }

            LOG.info("#################################################################");
        }


        LOG.info("Optimal Architecture: {}", optimalArchitecure);
        return optimalClassifier;
    }

    private void findArchitectures(int currentLayer, int lastLayerNeuronCount, List<Integer> nerons) {
        allArchitectures.add(new ArrayList<>(nerons));

        if (lastLayerNeuronCount + neuronIncrement <= maxNeuronsPerLayer) {
            int indexOfLastElement = nerons.size() - 1;
            List<Integer> newList = new ArrayList<>(nerons);
            newList.set(indexOfLastElement, lastLayerNeuronCount + neuronIncrement);
            findArchitectures(currentLayer, lastLayerNeuronCount + neuronIncrement, newList);
        }
        if (currentLayer + 1 <= maxLayers) {
            List<Integer> newList = new ArrayList<>(nerons);
            newList.add(1);
            findArchitectures(currentLayer + 1, minNeuronsPerLayer, newList);
        }
    }


    static class LearningListener implements LearningEventListener {

        private double[] foldErrors;
        private int foldSize;

        public LearningListener(int foldSize, int maxIterations) {

            this.foldSize = foldSize;
            this.foldErrors = new double[maxIterations];
        }

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();
            foldErrors[bp.getCurrentIteration() - 1] += bp.getTotalNetworkError() / foldSize;
        }
    }

}
