/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.samples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.filter.ImageFilterChain;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.OtsuBinarizeFilter;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.ocr.util.Letter;
import org.neuroph.ocr.util.Text;
import org.neuroph.ocr.OCRTraining;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mihailo Stupar
 */
public class TrainingSample {

    public static void main(String[] args) throws IOException {

        //     User input parameteres       
//*******************************************************************************************************************************       
        String imagePath = "C:/Users/Mihailo/Desktop/OCR/slova.png"; //path to the image with letters                        *
        String folderPath = "C:/Users/Mihailo/Desktop/OCR/ImagesDir/"; // loaction folder for storing segmented letters           *
        String textPath = "C:/Users/Mihailo/Desktop/OCR/slova.txt"; // path to the .txt file with text on the image          *
        String networkPath = "C:/Users/Mihailo/Desktop/OCR/network.nnet"; // location where the network will be stored     *
        int fontSize = 12; // fontSize, predicted by height of the letters, minimum font size is 12 pt                          *
        int scanQuality = 300; // scan quality, minimum quality is 300 dpi                                                      *
//*******************************************************************************************************************************

        BufferedImage image = ImageIO.read(new File(imagePath));
        ImageFilterChain chain = new ImageFilterChain();
        chain.addFilter(new GrayscaleFilter());
        chain.addFilter(new OtsuBinarizeFilter());
        BufferedImage binarizedImage = chain.processImage(image);

        
        
        
        
        Letter letterInfo = new Letter(scanQuality, binarizedImage);
//        letterInfo.recognizeDots(); // call this method only if you want to recognize dots and other litle characters, TODO

        Text texTInfo = new Text(binarizedImage, letterInfo);

        OCRTraining ocrTraining = new OCRTraining(letterInfo, texTInfo);
        ocrTraining.setFolderPath(folderPath);
        ocrTraining.setTrainingTextPath(textPath);
        ocrTraining.prepareTrainingSet();
        
  
        
        List<String> characterLabels = ocrTraining.getCharacterLabels();

        Map<String, FractionRgbData> map = ImageRecognitionHelper.getFractionRgbDataForDirectory(new File(folderPath), new Dimension(20, 20));
        DataSet dataSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(characterLabels, map);
        
        
        dataSet.setFilePath("C:/Users/Mihailo/Desktop/OCR/DataSet1.tset");
        dataSet.save();
        
        
        List<Integer> hiddenLayers = new ArrayList<Integer>();
        hiddenLayers.add(12);

        NeuralNetwork nnet = ImageRecognitionHelper.createNewNeuralNetwork("someNetworkName", new Dimension(20, 20), ColorMode.BLACK_AND_WHITE, characterLabels, hiddenLayers, TransferFunctionType.SIGMOID);
        BackPropagation bp = (BackPropagation) nnet.getLearningRule();
        bp.setLearningRate(0.3);
        bp.setMaxError(0.1);

        
//        MultiLayerPerceptron mlp = new MultiLayerPerceptron(12,13);
//        mlp.setOutputNeurons(null);
        
        System.out.println("Start learning...");
        nnet.learn(dataSet);
        System.out.println("NNet learned");

        nnet.save(networkPath);

    }

}
