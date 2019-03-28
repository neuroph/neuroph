/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.core.learning.stop;

import java.io.Serializable;
import org.neuroph.core.learning.SupervisedLearning;

/**
 * Stops learning rule if total network error is below some specified value
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MaxErrorStop implements StopCondition, Serializable {

    private final SupervisedLearning learningRule;
    
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
