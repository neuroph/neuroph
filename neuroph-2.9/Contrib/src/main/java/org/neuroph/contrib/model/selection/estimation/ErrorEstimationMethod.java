package org.neuroph.contrib.model.selection.estimation;

import org.neuroph.contrib.evaluation.domain.MetricResult;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *  Defines clear contract for all concrete error estimation methods
 */
public interface ErrorEstimationMethod {

    public MetricResult computeErrorEstimate(NeuralNetwork<BackPropagation> neuralNetwork, DataSet dataSet);

}
