package org.neuroph.samples.evaluation;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.samples.evaluation.evaluators.NeurophEvaluator;

import java.util.*;

public class NeuralNetworkEvaluationService {

    private Map<Class<?>, NeurophEvaluator> evaluators = new HashMap<>();


    public void evaluate(NeuralNetwork neuralNetwork, DataSet dataSet) {
        for (DataSetRow dataRow : dataSet.getRows()) {
            forwardPass(neuralNetwork, dataRow);
            for (NeurophEvaluator evaluator : evaluators.values()) {
                evaluator.processResult(neuralNetwork.getOutput(), dataRow.getDesiredOutput());
            }
        }
    }

    private void forwardPass(NeuralNetwork neuralNetwork, DataSetRow dataRow) {
        neuralNetwork.setInput(dataRow.getInput());
        neuralNetwork.calculate();
    }


    public <T extends NeurophEvaluator> void add(Class<T> type, T instance) {
        if (type == null)
            throw new NullPointerException("Type is null. Please make sure that you pass correct class.");
        evaluators.put(type, instance);

    }

    public <T extends NeurophEvaluator> T resultFor(Class<T> type) {
        return type.cast(evaluators.get(type));
    }


}
