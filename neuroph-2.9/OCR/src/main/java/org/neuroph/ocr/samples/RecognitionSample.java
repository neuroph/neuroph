/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.neuroph.imgrec.filter.ImageFilterChain;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.OtsuBinarizeFilter;
import org.neuroph.ocr.util.Letter;
import org.neuroph.ocr.OCRTextRecognition;
import org.neuroph.ocr.util.Text;

/**
 *
 * @author Mihailo
 */
public class RecognitionSample {

    public static void main(String[] args) throws IOException {

        // User input parameters
//*******************************************************************************************************************************************       
        String imagePath = "C:/Users/Mihailo/Desktop/OCR/tekst.png"; //path to the image with letters (document) for recognition   
        String textPath = "C:/Users/Mihailo/Desktop/OCR/tekst.txt"; // path to the .txt file where the recognized text will be stored          
        String networkPath = "C:/Users/Mihailo/Desktop/OCR/network.nnet"; // locatoin of the trained network                        
        int fontSize = 12; // fontSize, predicted by height of the letters, minimum font size is 12 pt                                      
        int scanQuality = 300; // scan quality, minimum quality is 300 dpi                                                                  
//*******************************************************************************************************************************************

        BufferedImage image = ImageIO.read(new File(imagePath));
        ImageFilterChain chain = new ImageFilterChain();
        chain.addFilter(new GrayscaleFilter());
        chain.addFilter(new OtsuBinarizeFilter());
        BufferedImage binarizedImage = chain.processImage(image);

        // Information about letters and text
        Letter letterInfo = new Letter(scanQuality, binarizedImage);
//        letterInfo.recognizeDots(); // call this method only if you want to recognize dots and other litle characters, TODO
        Text textInfo = new Text(binarizedImage, letterInfo);

        OCRTextRecognition recognition = new OCRTextRecognition(letterInfo, textInfo);

        recognition.setNetworkPath(networkPath);

        recognition.recognize();

        //if you want to save recognized text
//        recognition.setRecognizedTextPath(textPath); 
//        recognition.saveText();

        System.out.println(recognition.getRecognizedText());
    }

}
