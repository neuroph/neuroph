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

/**
 * This interface is implemented by classes who are listening to learning events (iterations, error etc.)
 * LearningEvent class holds the information about event.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public interface LearningEventListener extends  java.util.EventListener {
    
    /**
     * This method gets executed when LearningRule fires LearningEvent which some class is listening to.
     * For example, if you want to print current iteration, error etc.
     * @param event holds the information about event tha occured
     */
    public void handleLearningEvent(LearningEvent event);
}
