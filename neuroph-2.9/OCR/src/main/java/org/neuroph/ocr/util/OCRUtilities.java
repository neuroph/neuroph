/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Mihailo Stupar
 */
public class OCRUtilities {

    /**
     * Output contains all character labels with probabilities. This method
     * finds the biggest probability and returns character with that
     * probability.
     *
     * @param output output from network
     * @return character as string
     */
    public static String getCharacter(Map<String, Double> output) {
        double maxValue = -1;
        Map.Entry<String, Double> maxElement = null;
        for (Map.Entry<String, Double> element : output.entrySet()) {
            if (maxValue < element.getValue()) {
                maxElement = element;
                maxValue = element.getValue();
            }
        }
        return maxElement.getKey();
    }

    /**
     * Find the center of each row measured in pixels.
     *
     * @param image input image, should be black-white image;
     * @param ignoreSize the height of the sign (dots or trash) that should not
     * be recognized as letter
     * @return list with pixel position of each row
     */
    public static List<Integer> rowPositions(BufferedImage image, int ignoreSize) {
        HistogramUtilities histUtil = new HistogramUtilities();
        int[] histogram = histUtil.colorHistogramHeight(image);
        int[] gradient = histUtil.gradient(histogram);
        return histUtil.linePositions(gradient, ignoreSize);
    }

    /**
     * Word is class with two parameters, startPixel and endPixel. This method
     * calculates these pixels for given row and return them as List of Word
     *
     * @param image input image, should be black-white
     * @param row given row
     * @param letterSize predicted letter size
     * @param spaceGap predicted space size, spaces smaller that spaceGap are
     * not spaces between word, they are spaces between letter. Ignore spaces
     * between letters.
     * @return
     */
    public static List<WordPosition> wordsPositions(BufferedImage image, int row, int letterSize, int spaceGap) {
        List<WordPosition> words = new ArrayList<WordPosition>();
        HistogramUtilities histUtil = new HistogramUtilities();
        int[] histogram = histUtil.horizontalHistogram(image, row, letterSize);
        int[] histogramIMS = histUtil.histogramIgnoreMiniSpaces(histogram, spaceGap);

        int count = 0;
        for (int i = 0; i < histogramIMS.length; i++) {
            if (histogramIMS[i] != 0) {
                count++;
            } else { //(histogram[i] == 0) drugim recima vece je od nule
                if (count > 0) {
                    int start = i - count;
                    int end = i - 1;
                    WordPosition w = new WordPosition(start, end);
                    words.add(w);
                }
                count = 0;
            }

        }
        return words;
    }



    /**
     * Save the image to the file
     * @param image should be cropped before the saving. Use OCRCropImage class
     * @param path path to the folder, ie C:/Users/.../ it should ended with /
     * @param letterName letter of the name
     * @param extension some of .png .jpg ...
     */
    public static void saveToFile(BufferedImage image, String path, String letterName, String extension) {
        String imagePath = path + letterName +"."+ extension;
        File outputfile = new File(imagePath);   
        try {
            ImageIO.write(image, extension, outputfile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String createImageName(String character) {
        int number = character.hashCode()*(new Random().nextInt(100));
        return character+"_"+number;
    }
    



}
