package org.neuroph.contrib.evaluation.evaluators;


public interface NeurophEvaluator<T> {

    void processResult(double[] predictedOutput, double[] actualOutput);

    T getEvaluationResult();

}
