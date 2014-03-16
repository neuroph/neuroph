/**
 * Copyright 2013 Neuroph Project http://neuroph.sourceforge.net
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

package org.neuroph.nnet.comp;

import org.neuroph.nnet.comp.layer.Layer2D;
import org.neuroph.nnet.comp.layer.PoolingLayer;
import org.neuroph.nnet.comp.layer.ConvolutionalLayer;
import org.neuroph.nnet.comp.layer.FeatureMapsLayer;

/**
 * Utility functions for convolutional networks 
 * 
 * @author Boris Fulurija
 * @author Zorn Sevarac
 */
public class ConvolutionalUtils {
             
        /**
         * Creates full connectivity between feature maps in two layers
         * @param fromLayer from feature maps layer
         * @param toLayer to feature maps layer
         */
	public static void fullConectMapLayers(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer) {
		// convolutional and pooling layers use different connectivity patterns
                // povezuje svaku mapu sa svakom u dva susedna feature map layera
                if (toLayer instanceof ConvolutionalLayer) {
            		for (int i = 0; i < fromLayer.getNumberOfMaps(); i++) {
				for (int j = 0; j < toLayer.getNumberOfMaps(); j++) {
                                    Layer2D fromMap = fromLayer.getFeatureMap(i);
                                    Layer2D toMap = toLayer.getFeatureMap(j);
                                    toLayer.connectMaps(fromMap, toMap);
				}
			}
                  // direct connectivity pattern - povezuje svaku feature mapu sa njoj odgovarajucom featre mapom u sledecem layeru      
		} else if (toLayer instanceof PoolingLayer) { // we're connecting to Pooling layer
			for (int i = 0; i < toLayer.getNumberOfMaps(); i++) {
                            Layer2D fromMap = fromLayer.getFeatureMap(i);
                            Layer2D toMap = toLayer.getFeatureMap(i);
                            toLayer.connectMaps(fromMap, toMap);
			}
		}
	}
        
   
	/**
	 * Creates connections between two feature maps
	 * 
	 * @param fromLayer parent layer for from feature map
	 * @param toLayer parent layer for to feature map
	 * @param fromFeatureMapIndex index of from feature map
	 * @param toFeatureMapIndex index of to feature map
	 */
	public static void connectFeatureMaps(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer,
		int fromFeatureMapIndex, int toFeatureMapIndex) {
		Layer2D fromMap = fromLayer.getFeatureMap(fromFeatureMapIndex);
		Layer2D toMap = toLayer.getFeatureMap(toFeatureMapIndex);
		toLayer.connectMaps(fromMap, toMap);
	}
        
}
