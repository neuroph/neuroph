package org.neuroph.samples.evaluation;


public interface BaseEvaluator<T> {

    void processResult(double[] predictedOutputs, double[] actualOutputs);

    T getEvaluationResult();

}
