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

package org.neuroph.imgrec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.plugins.PluginBase;

/**
 * Provides methods to create neural network and training set for image recognition.
 * This class is mostly based on the code from tileclassification utility by Jon Tait
 * @author Jon Tait
 * @author Zoran Sevarac
 */
public class ImageRecognitionHelper {
		
	/**
         * Creates and returns new neural network for image recognition.
	 * Assumes that all of the FractionRgbData objects in the given map have identical 
	 * length arrays in them so that the input layer of the neural network can be 
	 * created here.
	 *
         * @param label neural network label
         * @param samplingResolution sampling resolution (image size)
	 * @param imageLabels image labels
         * @param layersNeuronsCount neuron counts in hidden layers
	 * @param transferFunctionType type of transfer function to use for neurons in network
         * @param colorMode color mode
     * @return 
	 */
	public static NeuralNetwork createNewNeuralNetwork(String label, Dimension samplingResolution, ColorMode colorMode, List<String> imageLabels,  List<Integer> layersNeuronsCount, TransferFunctionType transferFunctionType) {

                int numberOfInputNeurons;
                if ((colorMode == ColorMode.COLOR_RGB) || (colorMode == ColorMode.COLOR_HSL) ){ // for full color rgb or hsl
                    numberOfInputNeurons = 3 * samplingResolution.getWidth() * samplingResolution.getHeight();
                } else { // for black n white network
                    numberOfInputNeurons = samplingResolution.getWidth() * samplingResolution.getHeight();
                }

                int numberOfOuputNeurons = imageLabels.size();

		layersNeuronsCount.add(0, numberOfInputNeurons);
		layersNeuronsCount.add(numberOfOuputNeurons);
		
		System.out.println("Neuron layer size counts vector = " + layersNeuronsCount);
		
		NeuralNetwork neuralNetwork = new MultiLayerPerceptron(layersNeuronsCount, transferFunctionType);

		neuralNetwork.setLabel(label);
		PluginBase imageRecognitionPlugin = new ImageRecognitionPlugin(samplingResolution, colorMode);
		neuralNetwork.addPlugin(imageRecognitionPlugin);

		assignLabelsToOutputNeurons(neuralNetwork, imageLabels);
                neuralNetwork.setLearningRule(new MomentumBackpropagation());

            return neuralNetwork;
	}

        /**
         * Assign labels to output neurons
         * @param neuralNetwork neural network
         * @param imageLabels image labels
         */
	private static void assignLabelsToOutputNeurons(NeuralNetwork neuralNetwork, List<String> imageLabels) {
		Neuron[] outputNeurons = neuralNetwork.getOutputNeurons();
		
		for(int i=0; i<outputNeurons.length; i++) {
			Neuron neuron = outputNeurons[i];
			String label = imageLabels.get(i);
			neuron.setLabel(label);
		}
	}

        
        /**
         * Creates training set for the specified image labels and rgb data. Thi method is now forwarded to createRGBTrainingSet
         * @param imageLabels image labels
         * @param rgbDataMap map collection of rgb data
         * @return training set for the specified image data
         * @deprecated Use createRGBTrainingSet instead
         */        
        public static DataSet createTrainingSet(List<String> imageLabels, Map<String, FractionRgbData> rgbDataMap) 	{  
            return createRGBTrainingSet(imageLabels, rgbDataMap);
        }      
        
        /**
         * Creates training set for the specified image labels and rgb data
         * @param imageLabels image labels
         * @param rgbDataMap map collection of rgb data
         * @return training set for the specified image data
         */
	public static DataSet createRGBTrainingSet(List<String> imageLabels, Map<String, FractionRgbData> rgbDataMap) 	{	
                int inputCount = rgbDataMap.values().iterator().next().getFlattenedRgbValues().length;
                int outputCount = imageLabels.size();
		DataSet trainingSet = new DataSet(inputCount, outputCount);

		for (Entry<String, FractionRgbData> entry : rgbDataMap.entrySet()) {
			double[] input = entry.getValue().getFlattenedRgbValues();
			double[] response = createResponse(entry.getKey(), imageLabels);
			trainingSet.addRow(new DataSetRow(input, response));
		}
                
                // set labels for output columns
                int inputSize = trainingSet.getInputSize();
                for (int c= 0; c<trainingSet.getOutputSize() ; c++) {
                    trainingSet.setColumnName(inputSize+c, imageLabels.get(c));
                }

                return trainingSet;
	}
        
        /**
         * Creates training set for the specified image labels and hsl data
         * @param imageLabels image labels
         * @param hslDataMap map colletction of hsl data
         * @return training set for the specified image data
         */        
        public static DataSet createHSLTrainingSet(List<String> imageLabels, Map<String, FractionHSLData> hslDataMap) 	{	
                int inputCount = hslDataMap.values().iterator().next().getFlattenedHSLValues().length;
                int outputCount = imageLabels.size();
		DataSet trainingSet = new DataSet(inputCount, outputCount);

		for (Entry<String, FractionHSLData> entry : hslDataMap.entrySet()) {
			double[] input = entry.getValue().getFlattenedHSLValues();
			double[] response = createResponse(entry.getKey(), imageLabels);
			trainingSet.addRow(new DataSetRow(input, response));
		}

                // set labels for output columns
                int inputSize = trainingSet.getInputSize();
                for (int c= 0; c<trainingSet.getOutputSize() ; c++) {
                    trainingSet.setColumnName(inputSize+c, imageLabels.get(c));
                }
                
                
                return trainingSet;
	}
        

        /**
         * Creates binary black and white training set for the specified image labels and rgb data
         * white = 0 black = 1
         * @param imageLabels image labels
         * @param rgbDataMap map collection of rgb data
         * @return binary black and white training set for the specified image data
         */
        public static DataSet createBlackAndWhiteTrainingSet(List<String> imageLabels, Map<String, FractionRgbData> rgbDataMap) throws VectorSizeMismatchException
	{
            // TODO: Use some binarization image filter to do this; currently it works  with averaging RGB values
                int inputCount = rgbDataMap.values().iterator().next().getFlattenedRgbValues().length / 3;
                int outputCount = imageLabels.size();
		DataSet trainingSet = new DataSet(inputCount, outputCount);

		for (Entry<String, FractionRgbData> entry : rgbDataMap.entrySet()) {
			double[] inputRGB = entry.getValue().getFlattenedRgbValues();
                        double[] inputBW = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(inputRGB);
                        double[] response = createResponse(entry.getKey(), imageLabels);
			trainingSet.addRow(new DataSetRow(inputBW, response));
		}
                
                // set labels for output columns
                int inputSize = trainingSet.getInputSize();
                for (int c= 0; c<trainingSet.getOutputSize() ; c++) {
                    trainingSet.setColumnName(inputSize+c, imageLabels.get(c));
                }
              
            return trainingSet;
	}
        
    /**
     * Loads images from the specified dir, scales to specified resolution and creates RGB data for each image
     * Puts HSL data in a Map using filenames as keys, and returns that map
     * @param imgDir
     * @param samplingResolution
     * @throws java.io.IOException
     */
	public static Map<String, FractionRgbData> getFractionRgbDataForDirectory(File imgDir, Dimension samplingResolution) throws IOException
	{
		if(!imgDir.isDirectory()) {
			throw new IllegalArgumentException("The given file must be a directory.  Argument is: " + imgDir);
		}
		
		Map<String, FractionRgbData> rgbDataMap = new HashMap<>();
		
		ImageFilesIterator imagesIterator = new ImageFilesIterator(imgDir);
		while (imagesIterator.hasNext()) {
			File imgFile = imagesIterator.next();
                        Image img = ImageFactory.getImage(imgFile);
			img = ImageSampler.downSampleImage(samplingResolution, img);
			String filenameOfCurrentImage = imagesIterator.getFilenameOfCurrentImage();
			StringTokenizer st = new StringTokenizer(filenameOfCurrentImage, ".");
			rgbDataMap.put(st.nextToken(), new FractionRgbData(img));
		}
		return rgbDataMap;
	}      
        
     // creates hsl map from given image files - params should be files List<File> - or even better image files
     public static Map<String, FractionHSLData> getFractionHSLDataForDirectory ( File imgDir, Dimension samplingResolution) throws IOException {
        
		if(!imgDir.isDirectory()) {
			throw new IllegalArgumentException("The given file must be a directory.  Argument is: " + imgDir);
		}         
         
       Map <String, FractionHSLData> map = new HashMap<>();
       ImageFilesIterator imagesIterator = new ImageFilesIterator(imgDir);
       
        try {
            while (imagesIterator.hasNext()) {
                File imgFile = imagesIterator.next();
                BufferedImage img = ImageIO.read(imgFile);
                BufferedImage image = ImageUtilities.resizeImage(img, samplingResolution.getWidth(), samplingResolution.getHeight());
                
                String filenameOfCurrentImage = imgFile.getName();
                //String filenameOfCurrentImage = imagesIterator.getFilenameOfCurrentImage();
                StringTokenizer st = new StringTokenizer(filenameOfCurrentImage, ".");
                map.put(st.nextToken(), new FractionHSLData(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;             
     }        

        /**
         * Creates binary network output vector (response) for the specified list of images
         * Each network output (neuron) corresponds to one image.
         * @param inputLabel label of the input image
         * @param imageLabels labels used for output neurons
         * @return network response for the specified input
         */
	private static double[] createResponse(String inputLabel, List<String> imageLabels)
	{
		double[] response = new double[imageLabels.size()];
		int i=0;
		for(String imageLabel : imageLabels) {
			if(inputLabel.startsWith(imageLabel)) {
				response[i] = 1d;
			} else {
				response[i] = 0d;
			}
			i++;
		}
		return response;
	}
}