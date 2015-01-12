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
import org.neuroph.imgrec.filter.impl.Dilation;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.OtsuBinarizeFilter;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.ocr.properties.LetterInformation;
import org.neuroph.ocr.properties.TextInformation;
import org.neuroph.ocr.properties.TrainingProperties;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author Mihailo Stupar
 */
public class TrainingSample {

    public static void main(String[] args) throws IOException {

        //     User input parameteres       
//******************************************************************************************************************************       
        String imagePath = "C:/Users/Mihailo/Desktop/OCR/training.png"; //path to the image with letters                        *
        String folderPath = "C:/Users/Mihailo/Desktop/OCR/letters/"; // loaction folder for storing segmented lettres           *
        String textPath = "C:/Users/Mihailo/Desktop/OCR/training.txt"; // path to the .txt file with text on the image          *
        String networkPath = "C:/Users/Mihailo/Desktop/OCR/nnet/network.nnet"; // location where the network will be stored     *
        int fontSize = 12; // fontSize, predicted by height of the letters, minimum font size is 12 pt                          *
        int scanQuality = 300; // scan quality, minimum quality is 300 dpi                                                      *
//*******************************************************************************************************************************
       
        
        BufferedImage image = ImageIO.read(new File(imagePath));
        ImageFilterChain chain = new ImageFilterChain();
        chain.addFilter(new GrayscaleFilter());
        chain.addFilter(new OtsuBinarizeFilter());
        BufferedImage binarizedImage = chain.processImage(image);

        LetterInformation letterInfo = new LetterInformation(scanQuality, fontSize);
//        letterInfo.recognizeDots(); // call this method only if you want to recognize dots and other litle characters, in progress

        TextInformation texTInfo = new TextInformation(binarizedImage, letterInfo);

        TrainingProperties properties = new TrainingProperties(letterInfo, texTInfo);
        properties.setFolderPath(folderPath);
        properties.setTrainingTextPath(textPath);
        properties.prepareTrainingSet();

        List<String> characterLabels = properties.getCharacterLabels();
       
        Map<String, FractionRgbData> map = ImageRecognitionHelper.getFractionRgbDataForDirectory(new File(folderPath), new Dimension(20, 20));
        DataSet dataSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(characterLabels, map);

        List<Integer> hiddenLayers = new ArrayList<Integer>();
//        hiddenLayers.add(12);

        NeuralNetwork nnet = ImageRecognitionHelper.createNewNeuralNetwork("someNetworkName", new Dimension(20, 20), ColorMode.BLACK_AND_WHITE, characterLabels, hiddenLayers, TransferFunctionType.SIGMOID);
        BackPropagation bp = (BackPropagation) nnet.getLearningRule();
        bp.setLearningRate(0.3);
        bp.setMaxError(0.005);

        System.out.println("Start learning...");
        nnet.learn(dataSet);
        System.out.println("NNet learned");

        nnet.save(networkPath);

    }

}
