package org.neuroph.contrib.model.selection.optimizer;

import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.contrib.model.selection.ErrorEstimationMethod;
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

public class MultilayerPerceptronOptimazer<T extends BackPropagation> implements NeurophModelOptimizer {

    private static Logger LOG = LoggerFactory.getLogger(MultilayerPerceptronOptimazer.class);


    private Set<List<Integer>> allArchitectures = new HashSet<>();
    private NeuralNetwork<BackPropagation> optimalClassifier;
    private MetricResult optimalResult;

    private ErrorEstimationMethod errorEstimationMethod;
    private BackPropagation learningRule;

    private int maxLayers = 1;
    private int minNeuronsPerLayer = 1;
    private int maxNeuronsPerLayer = 30;
    private int neuronIncrement = 1;

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


    public MultilayerPerceptronOptimazer withErrorEstimationMethod(ErrorEstimationMethod errorEstimationMethod) {
        this.errorEstimationMethod = errorEstimationMethod;
        return this;
    }

    public MultilayerPerceptronOptimazer withLearningRule(BackPropagation learningRule) {
        this.learningRule = learningRule;
        return this;
    }


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


            long start = System.currentTimeMillis();
            MultiLayerPerceptron network = new MultiLayerPerceptron(architecture);
            LearningListener listener = new LearningListener(network, architecture.toString(), 10, learningRule.getMaxIterations());
            learningRule.addListener(listener);
            network.setLearningRule(learningRule);
            MetricResult result = errorEstimationMethod.computeErrorEstimate(network, dataSet);


            LOG.info(result.toString());
            LOG.info("Average epoch errors: [{}]", listener.foldErrors);

            LOG.info("Execution time: [{}] seconds", (System.currentTimeMillis() - start) / 1000.0);

            if (optimalResult == null || optimalResult.getFScore() < result.getFScore()) {
                LOG.info("Architecture [{}] became optimal architecture.", architecture);
                optimalResult = result;
                optimalClassifier = network;
            }

            LOG.info("#################################################################");
        }

        return optimalClassifier;
    }


    public void findArchitectures(int currentLayer, int lastLayerNeuronCount, List<Integer> nerons) {
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

        private final NeuralNetwork neuralNetwork;
        private String architecture;
        private double[] foldErrors;
        private int foldSize;

        public LearningListener(NeuralNetwork neuralNetwork, String architecture, int foldSize, int maxIterations) {
            this.neuralNetwork = neuralNetwork;
            this.architecture = architecture;
            this.foldSize = foldSize;
            this.foldErrors = new double[maxIterations];
        }


        long start = System.currentTimeMillis();

        public void handleLearningEvent(LearningEvent event) {
            BackPropagation bp = (BackPropagation) event.getSource();

                foldErrors[bp.getCurrentIteration() - 1] += bp.getTotalNetworkError() / foldSize;
            if (bp.getCurrentIteration() % 5 == 0)
                neuralNetwork.save(architecture + "_" + bp.getCurrentIteration() + "_MNIST_MLP.nnet");

            LOG.info("Epoch execution time: {} sec" , (System.currentTimeMillis() - start) / 1000.0);
            start = System.currentTimeMillis();
        }


    }

}
