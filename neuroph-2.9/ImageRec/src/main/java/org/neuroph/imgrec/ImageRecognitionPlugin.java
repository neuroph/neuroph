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
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;

import org.neuroph.core.Neuron;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.imgrec.image.ImageJ2SE;
import org.neuroph.util.plugins.PluginBase;

/**
 * Provides image recognition specific properties like sampling resolution, and easy to
 * use image recognition interface for neural network.
 *
 * @author Jon Tait
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ImageRecognitionPlugin extends PluginBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String IMG_REC_PLUGIN_NAME = "Image Recognition Plugin";

	/**
	 * Image sampling resolution (image dimensions)
	 */
	private Dimension samplingResolution;

        /**
         * Color mode used for recognition (full color or black and white)
         */
        private ColorMode colorMode;

	/**
	 * Constructor
	 * 
	 * @param samplingResolution
	 *            image sampling resolution (dimensions)
	 */
	public ImageRecognitionPlugin(Dimension samplingResolution) {
		super(IMG_REC_PLUGIN_NAME);
		this.samplingResolution = samplingResolution;
                this.colorMode = ColorMode.COLOR_RGB;
	}

	/**
	 * Constructor
	 *
	 * @param samplingResolution
	 *            image sampling resolution (dimensions)
         * @param colorMode recognition color mode 
	 */
	public ImageRecognitionPlugin(Dimension samplingResolution, ColorMode colorMode) {
		super(IMG_REC_PLUGIN_NAME);
		this.samplingResolution = samplingResolution;
                this.colorMode = colorMode;
	}

	/**
	 * Returns image sampling resolution (dimensions)
	 * 
	 * @return image sampling resolution (dimensions)
	 */
	public Dimension getSamplingResolution() {
		return samplingResolution;
	}

        /**
         * Returns color mode used for image recognition
         * @return color mode used for image recognition
         */
        public ColorMode getColorMode() {
            return this.colorMode;
        }

	/**
	 * Sets network input (image to recognize) from the specified BufferedImage
	 * object
	 * 
	 * @param img
	 *            image to recognize
	 */
	public void setInput(Image img) throws ImageSizeMismatchException {

		double input[];

		if (this.colorMode == ColorMode.COLOR_RGB) {
                    FractionRgbData imgRgb = new FractionRgbData(ImageSampler
				.downSampleImage(samplingResolution, img, img.getType()));                    
			input = imgRgb.getFlattenedRgbValues();                        
                } else if (this.colorMode == ColorMode.COLOR_HSL) {
                    FractionHSLData imgHsl = new FractionHSLData(ImageSampler
				.downSampleImage(samplingResolution, img, img.getType()));                    
			input = imgHsl.getFlattenedHSLValues();                                        
                } else if (this.colorMode == ColorMode.BLACK_AND_WHITE) {
                    FractionRgbData imgRgb = new FractionRgbData(ImageSampler
				.downSampleImage(samplingResolution, img, img.getType()));                                        
			input = FractionRgbData.convertRgbInputToBinaryBlackAndWhite(imgRgb
					.getFlattenedRgbValues());
                } else
			throw new RuntimeException("Unknown color mode!");

                try {
                    this.getParentNetwork().setInput(input);
                } catch (VectorSizeMismatchException vsme) {
                    throw new ImageSizeMismatchException(vsme);
                }
	}

	/**
	 * Sets network input (image to recognize) from the specified File object
	 * 
	 * @param imgFile
	 *            file of the image to recognize
	 */
	public void setInput(File imgFile) throws IOException, ImageSizeMismatchException {
		this.setInput(ImageFactory.getImage(imgFile));
	}

	/**
	 * Sets network input (image to recognize) from the specified URL object
	 * 
	 * @param imgURL
	 *            url of the image
	 */
	public void setInput(URL imgURL) throws IOException, ImageSizeMismatchException{
		this.setInput(ImageFactory.getImage(imgURL));
	}

        public void processInput() {
                getParentNetwork().calculate();
        }

	/**
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 * 
	 * @return image recognition result
	 */
	public HashMap<String, Double> getOutput() {
		HashMap<String, Double> networkOutput = new HashMap<String, Double>();

		for (Neuron neuron : this.getParentNetwork().getOutputNeurons()) {
			String neuronLabel = neuron.getLabel();
			networkOutput.put(neuronLabel, neuron.getOutput());
		}

		return networkOutput;
	}


	/**
	 * This method performs the image recognition for specified image.
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 *
	 * @return image recognition result
	 */
        public HashMap<String, Double> recognizeImage(Image img) throws ImageSizeMismatchException {
		setInput(img);
		processInput();
                return getOutput();
        }
        
        public HashMap<String, Double> recognizeImage(BufferedImage img) throws ImageSizeMismatchException {
            return recognizeImage(new ImageJ2SE(img));
        }        
        
//        public HashMap<String, Double> recognizeImage(Bitmap img) throws ImageSizeMismatchException {
//            return recognizeImage(ImageFactory. new ImageAndroid(img));
//        }
        

	/**
	 * This method performs the image recognition for specified image file.
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 *
	 * @return image recognition result
	 */
        public HashMap<String, Double> recognizeImage(File imgFile)  throws IOException, ImageSizeMismatchException {
		setInput(imgFile);
		processInput();
                return getOutput();
        }

	/**
	 * This method performs the image recognition for specified image URL.
	 * Returns image recognition result as map with image labels as keys and
	 * recogition result as value
	 *
	 * @return image recognition result
	 */
        public HashMap<String, Double> recognizeImage(URL imgURL)  throws IOException, ImageSizeMismatchException {
		setInput(imgURL);
		processInput();
                return getOutput();
        }

	/**
	 * Returns one or more image labels with the maximum output - recognized
	 * images
	 * 
	 * @return one or more image labels with the maximum output
	 */
	public HashMap<String, Neuron> getMaxOutput() {
		HashMap<String, Neuron> maxOutput = new HashMap<String, Neuron>();
		Neuron maxNeuron = this.getParentNetwork().getOutputNeurons()[0];

		for (Neuron neuron : this.getParentNetwork().getOutputNeurons()) {
			if (neuron.getOutput() > maxNeuron.getOutput())
				maxNeuron = neuron;
		}


		maxOutput.put(maxNeuron.getLabel(), maxNeuron);

		for (Neuron neuron : this.getParentNetwork().getOutputNeurons()) {
			if (neuron.getOutput() == maxNeuron.getOutput()) {
				maxOutput.put(neuron.getLabel(), neuron);
			}
		}

		return maxOutput;
	}

}