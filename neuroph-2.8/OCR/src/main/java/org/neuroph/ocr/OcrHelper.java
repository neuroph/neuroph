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

package org.neuroph.ocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.ImageJ2SE;
import org.neuroph.util.TransferFunctionType;


/**
 * Provides methods to create Neural Network and Training set for OCR.
 * @author zoran
 */
public class OcrHelper extends ImageRecognitionHelper {

    /**
     * Creates neural network for OCR, which contains OCR plugin. OCR plugin provides interface for character recognition.
     * @param label neural network label
     * @param samplingResolution character size in pixels (all characters will be scaled to this dimensions during recognition)
     * @param colorMode color mode used fr recognition
     * @param characterLabels character labels for output neurons
     * @param layersNeuronsCount number of neurons ih hidden layers
     * @param transferFunctionType neurons transfer function type
     * @return returns NeuralNetwork with the OCR plugin
     */
    public static NeuralNetwork createNewNeuralNetwork(String label, Dimension samplingResolution, ColorMode colorMode, List<String> characterLabels,  List<Integer> layersNeuronsCount, TransferFunctionType transferFunctionType) {
        NeuralNetwork neuralNetwork = ImageRecognitionHelper.createNewNeuralNetwork(label, samplingResolution, colorMode, characterLabels, layersNeuronsCount, transferFunctionType);
        neuralNetwork.addPlugin(new OcrPlugin(samplingResolution, colorMode));

        return neuralNetwork;
    }
    
   /**
     * Create training set 
     * 
     * @param imageWithChars
     * @param chars
     * @param scaleToDim
     * @param trainingSetName 
     */
    public static DataSet createTrainingSet (String trainingSetName, BufferedImage imageWithChars, String chars, Dimension scaleToDim, List<String> imageLabels){

        // convert chars from string to list 
        List<String> charList = Arrays.asList(chars.split(" "));        // izgleda da ovo zeza...
        // extract individual char images which will be used to create training set
        CharExtractor charExtractor = new CharExtractor(imageWithChars);
        HashMap <String, BufferedImage> charImageMap = charExtractor.extractCharImagesToLearn(imageWithChars, charList, scaleToDim);
        
       
       // prepare image labels (we need them to label output neurons...)
       // ArrayList<String> imageLabels = new ArrayList<String>();   
        for (String imgName : charImageMap.keySet()) {
            StringTokenizer st = new StringTokenizer(imgName, "._");
            String imgLabel = st.nextToken();
            if (!imageLabels.contains(imgLabel)) { // check for duplicates ...
                imageLabels.add(imgLabel);
            }
        }
        Collections.sort(imageLabels);
        
        // get RGB image data - map chars and their their rgb data
        Map<String, FractionRgbData> imageRgbData = ImageUtilities.getFractionRgbDataForImages(charImageMap);
                       
        // also put junk all black and white image in training set (for black n whit emode)
        BufferedImage allWhite = new BufferedImage(scaleToDim.getWidth(), scaleToDim.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = allWhite.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, allWhite.getWidth(), allWhite.getHeight());
        imageRgbData.put("allWhite", new FractionRgbData(allWhite));
        
//        BufferedImage allBlack = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
//        g = allBlack.getGraphics();
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, allBlack.getWidth(), allBlack.getHeight());
//        imageRgbData.put("allBlack", new FractionRgbData(allBlack));        
                      
        // put junk images (all red, blue, green) to avoid errors (used for full color mode)
//        BufferedImage allRed = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
//        Graphics g = allRed.getGraphics();
//        g.setColor(Color.RED);
//        g.fillRect(0, 0, allRed.getWidth(), allRed.getHeight());
//        imageRgbData.put("allRed", new FractionRgbData(allRed));        
//        
//        BufferedImage allBlue = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
//        g = allBlue.getGraphics(); 
//        g.setColor(Color.BLUE);
//        g.fillRect(0, 0, allBlue.getWidth(), allBlue.getHeight());
//        imageRgbData.put("allBlue", new FractionRgbData(allBlue));        
//        
//        BufferedImage allGreen = new BufferedImage(charDimension.getWidth(), charDimension.getHeight(), BufferedImage.TYPE_INT_RGB);
//        g = allGreen.getGraphics(); 
//        g.setColor(Color.GREEN);
//        g.fillRect(0, 0, allGreen.getWidth(), allGreen.getHeight());
//        imageRgbData.put("allGreen", new FractionRgbData(allGreen));                
                
        // create training set using image rgb data
        DataSet dataSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(imageLabels, imageRgbData);
         //DataSet dataSet = ImageRecognitionHelper.createTrainingSet(this.imageLabels, imageRgbData);
        dataSet.setLabel(trainingSetName); 
                
        return dataSet;   
    }  
    
    /**
     * Recognize characters in given text images and returns character list
     * @param neuralNet
     * @param image
     * @param charDimension
     * @return 
     */
    public static List<Character> recognizeText(NeuralNetwork neuralNet, BufferedImage image, Dimension charDimension){
        CharExtractor charExtractor = new CharExtractor(image);
        List<BufferedImage> charImages = charExtractor.extractCharImagesToRecognize();
        List<Character> characters = recognize(neuralNet, charImages, charDimension);
        return characters;
    }
    
    
    public static List<Character> recognize (NeuralNetwork nnet, List<BufferedImage> images, Dimension charDimension){
        OcrPlugin  ocrPlugin = (OcrPlugin) nnet.getPlugin(OcrPlugin.class);      
        List<Character> letters = new ArrayList<Character>();

        for (BufferedImage img : images) {
            Character letter = ocrPlugin.recognizeCharacter(new ImageJ2SE(img), charDimension);
            letters.add(letter);
        }
        return letters;
    }      
    
    
}