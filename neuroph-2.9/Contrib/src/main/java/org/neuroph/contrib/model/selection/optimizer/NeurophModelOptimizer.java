package org.neuroph.contrib.model.selection.optimizer;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

public interface NeurophModelOptimizer {

    NeuralNetwork createOptimalModel(final DataSet trainSet);

//    NeuralNetwork createOptimalModel(final DataSet trainSet, final DataSet testSet);

}
