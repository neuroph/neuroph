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

package org.neuroph.core.events;

import org.neuroph.core.learning.LearningRule;

/**
 * This class holds information about the source of some learning event.
  * @author Zoran Sevarac
 */

public class LearningEvent extends java.util.EventObject {
    
    LearningEvent.Type eventType;
    
    public LearningEvent(LearningRule source, LearningEvent.Type eventType) {
        super(source);
        this.eventType = eventType;
    }

    public LearningEvent.Type getEventType() {
        return eventType;
    }
    
    // Add types of learning events you want to listen to to this enum
    public static enum Type {
        EPOCH_ENDED, LEARNING_STOPPED;
    }
    
                
}