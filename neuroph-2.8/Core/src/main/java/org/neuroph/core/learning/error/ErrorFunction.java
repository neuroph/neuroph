package org.neuroph.core.learning.error;

/**
 * interface for all errors
 * @author zoran
 */
    /* interface / abstract ? */
public interface ErrorFunction {
    public double getTotalError();
    public void addOutputError(double [] outputError);
    public void reset();
}
