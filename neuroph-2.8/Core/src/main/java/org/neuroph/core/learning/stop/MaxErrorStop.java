package org.neuroph.core.learning.stop;

import java.io.Serializable;
import org.neuroph.core.learning.SupervisedLearning;

/**
 *
 * @author zoran
 */
public class MaxErrorStop implements StopCondition, Serializable {

    private SupervisedLearning learningRule;
    
    public MaxErrorStop(SupervisedLearning learningRule) {
        this.learningRule = learningRule;
    }
    
    @Override
    public boolean isReached() {
        if (learningRule.getTotalNetworkError() < learningRule.getMaxError()) {
            return true;
        }
        
        return false;
    }
           
}
