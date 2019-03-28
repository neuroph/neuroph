package org.neuroph.eval;

/**
 * Generic interface for all types of evaluators
 *
 * @param <T> Return type of final evaluation result
 */
public interface Evaluator<T> {
    
    /**
     * This method should handle processing of a single network output within an evaluation procedure
     * 
     * @param networkOutput actual network output
     * @param desiredOutput desired/target network output
     */
    public void processNetworkResult(double[] networkOutput, double[] desiredOutput);

    /**
     * This method should return final evaluation result
     * 
     * @return final evaluation result
     */
    public T getResult();

    
    /**
     * 
     */    
    public void reset();

}