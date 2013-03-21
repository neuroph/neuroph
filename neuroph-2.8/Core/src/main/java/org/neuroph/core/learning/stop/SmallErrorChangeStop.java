/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.core.learning.stop;

import java.io.Serializable;
import org.neuroph.core.learning.SupervisedLearning;

/**
 *
 * @author zoran
 */
public class SmallErrorChangeStop implements StopCondition, Serializable {

    private SupervisedLearning learningRule;
    
    public SmallErrorChangeStop(SupervisedLearning learningRule) {
        this.learningRule = learningRule;
    }
    
    @Override
    public boolean isReached() {

            if (learningRule.getMinErrorChangeIterationsCount() >= learningRule.getMinErrorChangeIterationsLimit()) {
                return true;
            }

        return false;
    }
    
}
