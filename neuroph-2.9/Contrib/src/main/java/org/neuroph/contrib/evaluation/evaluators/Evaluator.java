package org.neuroph.contrib.evaluation.evaluators;


public interface Evaluator<T> {

    void processResult(double[] predictedOutput, double[] actualOutput);

    T getEvaluationResult();

}
