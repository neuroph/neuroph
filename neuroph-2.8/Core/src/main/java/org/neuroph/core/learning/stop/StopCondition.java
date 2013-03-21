package org.neuroph.core.learning.stop;

/**
 *
 * @author zoran
 */
             /* abstract class treba da se zna za koje learning rules adi */
// ima smisla koristiti samo za kompleksnije uslove u metode hasReachedStopCondition
// naselditi i napraviti jednu za iteracije i jednu za va
public interface StopCondition {
    
    public boolean isReached();    
     
    // maxIterations
    // maxError
    // error change for specific num of iterations
    // MSE value for specified data set (test)
    // time running
    // for unsupervised - while it gets stable (changes are low)
    
    // how do we send data from learning rule to stopCondition - stopCondition must be able to get them
    
    // if this is an interface, we could use a list of stop conditions as combination
    
    //----------------------------
    // use cases
    //----------------------------
    /*
     * 1. max iterations i za supervised i za unsupervised
     * 2. max error i error change samo za supervised 
     * 
     * MaxIterationsStop
     * MaxErrorStop
     * StopOnMaxIterationsOrError
     * CustomStopCondition
     * 
     * 
     * 
     */
    
    


}
