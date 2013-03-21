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
package org.neuroph.nnet.flat;

/**
 * These are the training methods provided by the flat network.  All methods can be trained
 * either single threaded, or multithreaded.  GPU support is provided as well.
 * @author Jeff Heaton (http://www.jeffheaton.com)
 * 
 * @see FlatNetworkPlugin
 */
public enum FlatLearningType {
	/**
	 * Classic momentum based propagation.  The learning rate and momentum are specified by using the
	 * setLearningRate and setMomentum on the FlatNetworkLearning class.
	 */
	BackPropagation,
	
	/**
	 * Resilient Propagation.  No parameters are needed for this learning method.  RPROP is the best
	 * general purpose training method.
	 */
	ResilientPropagation,
	
	/**
	 * Manhattan update rule.  Not at all a good general purpose learning method, only useful in 
	 * some situations.  A learning rate must be specified by using the setLearningRate method on
	 * the FlatNetworkLearning class.
	 */
	ManhattanUpdateRule
}
