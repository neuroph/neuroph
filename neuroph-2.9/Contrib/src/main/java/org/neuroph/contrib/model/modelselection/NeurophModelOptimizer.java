package org.neuroph.contrib.model.modelselection;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

/**
 * Contract specification which all concrete optimizers should implement
 */
public interface NeurophModelOptimizer {

    NeuralNetwork createOptimalModel(final DataSet trainSet);

}
