package org.neuroph.samples.evaluation.evaluators;


public interface NeurophEvaluator<T> {

    void processResult(double[] predictedOutputs, double[] actualOutputs);

    T getEvaluationResult();

}
