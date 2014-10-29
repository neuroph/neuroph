package org.neuroph.contrib.evaluation.evaluators;


public interface NeurophEvaluator<T> {

    void processResult(double[] predictedOutputs, double[] actualOutputs);

    T getEvaluationResult();

}
