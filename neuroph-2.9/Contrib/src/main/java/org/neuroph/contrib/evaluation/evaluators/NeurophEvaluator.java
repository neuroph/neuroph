package org.neuroph.contrib.evaluation.evaluators;

/**
 * Interface used to define a contract which all concrete Evaluators must obey
 *
 * @param <T> Generic type used to define return type of some concrete Evaluator
 */
public interface NeurophEvaluator<T> {

    void processResult(double[] predictedOutput, double[] actualOutput);

    T getEvaluationResult();

}
