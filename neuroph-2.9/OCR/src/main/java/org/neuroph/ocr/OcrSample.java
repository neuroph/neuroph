/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;

/**
 *
 * @author zoran
 */
public class OcrSample {
    
    public static void main(String[]args) {
        NeuralNetwork nnet = NeuralNetwork.load("C:\\Users\\zoran\\Desktop\\nn.nnet");
        OcrPlugin ocrPlugin = (OcrPlugin) nnet.getPlugin(OcrPlugin.class);
        
        // load letter images
        Image charImage = ImageFactory.getImage("C:\\Users\\zoran\\Desktop\\Letters\\A.png");
        Character ch = ocrPlugin.recognizeCharacter(charImage);
        System.out.println(ch);
    }
    
}
