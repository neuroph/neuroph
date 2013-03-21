package org.neuroph.core.learning.stop;

import java.io.Serializable;
import org.neuroph.core.learning.IterativeLearning;

/**
 *
 * @author zoran
 */
public class MaxIterationsStop implements StopCondition, Serializable {

    private IterativeLearning learningRule;    
    
    public MaxIterationsStop(IterativeLearning learningRule) {
        this.learningRule = learningRule;
    }
        
    @Override
    public boolean isReached() {
        
        
        if (learningRule.getCurrentIteration() >= learningRule.getMaxIterations()) {
            return true;
        }
        
        return false;
    }
    
    
}