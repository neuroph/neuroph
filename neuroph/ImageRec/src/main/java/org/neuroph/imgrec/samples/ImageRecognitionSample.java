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

package org.neuroph.imgrec.samples;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.imgrec.ImageRecognitionPlugin;

/**
 * This sample shows how to use the image recognition neural network in your applications.
 * IMPORTANT NOTE: specify filenames for neural network and test image, or you'll get IOException
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ImageRecognitionSample {  

    public static void main(String[] args) {
          // load trained neural network saved with NeurophStudio (specify existing neural network file here)
          NeuralNetwork nnet = NeuralNetwork.createFromFile("MyImageRecognition.nnet");
          // get the image recognition plugin from neural network
          ImageRecognitionPlugin imageRecognition = (ImageRecognitionPlugin)nnet.getPlugin(ImageRecognitionPlugin.class);

          try {
                // image recognition is done here
                HashMap<String, Double> output = imageRecognition.recognizeImage(new File("someImage.jpg")); // specify some existing image file here
                System.out.println(output.toString());
          } catch(IOException ioe) {
              System.out.println("Error: could not read file!");
          } catch (VectorSizeMismatchException vsme) {
              System.out.println("Error: Image dimensions dont !");
          }
    }
}
