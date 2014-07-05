package org.neuroph.training;

/**
 *
 * @author zoran
 */
public class TrainingResult {
    private double totalError;
    private int iterations;

    public TrainingResult(double totalError, int iterations) {
        this.totalError = totalError;
        this.iterations = iterations;
    }

    public double getTotalError() {
        return totalError;
    }

    public void setTotalError(double totalError) {
        this.totalError = totalError;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
    
    
    
}
