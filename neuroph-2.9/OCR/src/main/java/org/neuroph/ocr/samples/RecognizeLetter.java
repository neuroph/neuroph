/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.samples;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ImageRecognitionPlugin;
import org.neuroph.ocr.util.OCRUtilities;

/**
 *
 * @author Mihailo Stupar
 */
public class RecognizeLetter {

    public static void main(String[] args) throws IOException {

        // User input parameters
//***********************************************************************************************************************************
        String networkPath = "C:/Users/Mihailo/Desktop/OCR/nnet/nnet-12-0.01.nnet"; // path to the trained network                *
        String letterPath = "C:/Users/Mihailo/Desktop/OCR/letters/259.png"; // path to the letter for recognition                   *
//***********************************************************************************************************************************
        
        NeuralNetwork nnet = NeuralNetwork.createFromFile(networkPath);
        ImageRecognitionPlugin imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
        Map<String, Double> output = imageRecognition.recognizeImage(new File(letterPath));
        System.out.println("Recognized letter: "+OCRUtilities.getCharacter(output));

    }

}
