/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.samples;

import org.neuroph.ocr.LinePositions;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.ImageRecognitionPlugin;
import org.neuroph.imgrec.filter.ImageFilterChain;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.LetterSeparationFilter;
import org.neuroph.imgrec.filter.impl.OCRSeparationFilter;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.ocr.OcrPlugin;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mihailo
 */
public class OCRNeuroph {

    public static void main(String[] args) throws IOException {

        // Image with text
        String pathImage = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.png";
        BufferedImage image = ImageIO.read(new File(pathImage));

        // File with text that corresponds with text in image
        String pathText = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.txt";
        Path path = FileSystems.getDefault().getPath(pathText);
        String text = new String(Files.readAllBytes(path));

        ImageFilterChain filterChain = new ImageFilterChain();
        filterChain.addFilter(new GrayscaleFilter());
        filterChain.addFilter(new LetterSeparationFilter());
        BufferedImage binarizedImage = filterChain.processImage(image); // output: binrizovna slka, sa belom izmedju slova

        // Positions of each line measured in pixels
        LinePositions linePos = new LinePositions(binarizedImage);
        linePos.setLineHeightThresh(9);  // eacl letter should not be smaller than 9px
        int[] linePositions = linePos.findLinePositions();

        // In locationFolder will be segmented letters as separated images
        String locationFolder = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\letters\\";
        OCRSeparationFilter separation = new OCRSeparationFilter();
        separation.setDimension(30, 30); // each letter will be in single image, dimension of the image
        separation.setLinePositions(linePositions);
        separation.setLocationFolder(locationFolder);
        separation.setText(text);
        separation.processImage(binarizedImage);

        //create letter labels, in this version letters A-Za-z
        List<String> letterLabels = new ArrayList<String>();
        for (char x = 65; x <= 90; x++) {
            letterLabels.add(x + "");
        }  // A-Z [65-90]
        for (char x = 97; x <= 122; x++) {
            letterLabels.add(x + "");
        } // a-z [97-122]

        // Create dataset
        Map<String, FractionRgbData> map = ImageRecognitionHelper.getFractionRgbDataForDirectory(new File(locationFolder), new Dimension(20, 20));
        DataSet dataSet = ImageRecognitionHelper.createRGBTrainingSet(letterLabels, map);

        // Hidden layers
        List<Integer> hiddenLayers = new ArrayList<Integer>();
        hiddenLayers.add(12);
        hiddenLayers.add(8);

        // Create neural network
        NeuralNetwork nnet = ImageRecognitionHelper.createNewNeuralNetwork("someNetworkName", new Dimension(20, 20), ColorMode.FULL_COLOR, letterLabels, hiddenLayers, TransferFunctionType.SIGMOID);

        // Learning parameters
        MomentumBackpropagation mb = (MomentumBackpropagation) nnet.getLearningRule();
        mb.setLearningRate(0.3);
        mb.setMaxError(0.1);
        mb.setMomentum(0.9);

        // Learning the nerwork
        System.out.println("NNet start learning...");
        nnet.learn(dataSet);
        System.out.println("NNet learned");
        
        //save neural network
        String networkPath = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\networks\\network1.nnet";
        nnet.save(networkPath);
        
//        // Recognize letter
//        NeuralNetwork nnet = NeuralNetwork.createFromFile(networkPath);
//        
//        String pathToLetter = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\test\\g.png";
//        ImageRecognitionPlugin imgRecPlugin = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
//        HashMap<String, Double> output = imgRecPlugin.recognizeImage(new File(pathToLetter)); 
//        System.out.println(getMax(output));
//
//       

    }
    
//    private static String getMax(Map<String, Double> map) {
//        double maxValue = -1;
//        Map.Entry<String,Double> maxElement = null;
//        for (Map.Entry<String, Double> element : map.entrySet()) {
//            if (maxValue < element.getValue()) {
//                maxElement = element;
//                maxValue = element.getValue();
//            }
//        }
//        return maxElement.getKey()+" = "+maxElement.getValue();
//    }

}
