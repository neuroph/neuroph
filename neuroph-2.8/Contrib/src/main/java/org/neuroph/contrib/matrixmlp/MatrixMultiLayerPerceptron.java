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

package org.neuroph.contrib.matrixmlp;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * Matrix based implementation of Multi LAyer Perceptron
 * @author Zoran Sevarac
 */
public class MatrixMultiLayerPerceptron extends NeuralNetwork {

    MultiLayerPerceptron sourceNetwork;
    MatrixLayer[] matrixLayers;

    public MatrixMultiLayerPerceptron(MultiLayerPerceptron sourceNetwork) {
        this.sourceNetwork = sourceNetwork;
        // copy layers, input and output neurons

        createMatrixLayers();
        this.setLearningRule(new MatrixMomentumBackpropagation());
    }

    public MatrixLayer[] getMatrixLayers() {
        return matrixLayers;
    }



    private void createMatrixLayers() {
        matrixLayers = new MatrixLayer[sourceNetwork.getLayersCount()];
        matrixLayers[0] = new MatrixInputLayer(sourceNetwork.getLayers()[0].getNeuronsCount());

        MatrixLayer prevLayer = matrixLayers[0];
        
         for(int i =1; i < sourceNetwork.getLayersCount(); i++  ) {
            Layer layer = sourceNetwork.getLayerAt(i);
            MatrixMlpLayer newBpLayer = new MatrixMlpLayer(layer, prevLayer, new Tanh());
            matrixLayers[i] = newBpLayer;
            prevLayer = newBpLayer;
        }
    }

        @Override
         public void calculate() {
             for(int i = 1; i < matrixLayers.length; i++) {
                 matrixLayers[i].calculate();                 
             }
         }

         @Override
         public void setInput(double ... inputs) {
             matrixLayers[0].setInputs(inputs);
         }

         @Override
         public double[] getOutput() {
            return  matrixLayers[matrixLayers.length-1].getOutputs();
         }

         @Override
         public int getLayersCount() {
             return matrixLayers.length;
         }


}



