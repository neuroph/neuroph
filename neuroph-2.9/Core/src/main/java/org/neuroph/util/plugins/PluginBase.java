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

package org.neuroph.util.plugins;

import java.io.Serializable;

import org.neuroph.core.NeuralNetwork;

/**
 * Base class for all neural network plugins.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class PluginBase implements Serializable {

	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
	private static final long serialVersionUID = 1L;	
	
	/**
	 * Name for this plugin
	 */
	private String name;
	
	/**
	 * Reference to parent neural network
	 */
	private NeuralNetwork<?> parentNetwork;

        
        public PluginBase() {
        }
            
        
	/**
	 * Creates an instance of plugin for neural network
	 */	
	public PluginBase(String name) {
		this.name=name;
	}
	
	/**
	 * Returns the name of this plugin
	 * @return name of this plugin
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the parent network for this plugin
	 * @return parent network for this plugin
	 */
	public NeuralNetwork<?> getParentNetwork() {
		return parentNetwork;
	}

	/**
	 * Sets the parent network for this plugin
	 * @param parentNetwork parent network for this plugin
	 */
	public void setParentNetwork(NeuralNetwork parentNetwork) {
		this.parentNetwork = parentNetwork;
	}

}
