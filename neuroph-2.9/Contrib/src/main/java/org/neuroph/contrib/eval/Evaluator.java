package org.neuroph.contrib.eval;

/**
 * Interface for all Evaluators
 *
 * @param <T> Generic type used to define the return type of final evaluation result
 */
public interface Evaluator<T> {
    
    /**
     * This method should handle processing of a single network output within an evaluation procedure
     * 
     * @param networkOutput
     * @param desiredOutput 
     */
    public void processNetworkResult(double[] networkOutput, double[] desiredOutput);

    /**
     * This method should return final evaluation result
     * @return 
     */
    public T getResult();

    
    
    public void reset();

}