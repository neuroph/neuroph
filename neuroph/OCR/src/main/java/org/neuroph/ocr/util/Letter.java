/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.util;

import java.awt.image.BufferedImage;
import java.util.List;
import org.neuroph.ocr.util.histogram.Histogram;

/**
 *
 * @author Mihailo Stupar
 */
public class Letter {

    private int cropWidth;
    private int cropHeight;
    private int smallestSizeLetter;
    private int letterSize;
    private int trashSize;
    private int spaceGap;

    private int scanQuality;
    private int fontSize;
    private BufferedImage image;
    private int[] heightHistogram;
    private int[] gradient;

//    public Letter(int scanQuality, int fontSize) {
//        this.scanQuality = scanQuality;
//        this.fontSize = fontSize;
//
//        calculateDimensions();
//        calculateSmallestSizeLetter();
//        calculateLetterSize();
//        calculateTrashsize();
//        calculateSpaceGap();
//    }
    
    public Letter(int scanQuality, BufferedImage image) {
        this.scanQuality = scanQuality;
        this.image = image;    
        heightHistogram = Histogram.heightHistogram(image);
        gradient = Histogram.gradient(heightHistogram);
        calculateSmallestSizeLetter();
        List<Integer> rowHeights = OCRUtilities.rowHeights(gradient, smallestSizeLetter);
        int meanHeight = (int) caluclateMean(rowHeights);
        calculateDimensions(meanHeight);  
        calculateLetterSize(meanHeight);
        calculateSpaceGap(meanHeight);
        
        
    }
    

    private void calculateDimensions(int meanHeight) {
        int offset = (int) (0.1*meanHeight);
        cropWidth = meanHeight+offset;
        cropHeight = meanHeight+offset;
    }

    private void calculateSmallestSizeLetter() {
        if (scanQuality == 300) {
            smallestSizeLetter = 9;
        }
        if (scanQuality == 600) {
            smallestSizeLetter = 18;
        }
        if (scanQuality == 1200) {
            smallestSizeLetter = 36;
        }
    }
    
   
    
    
    

    private void calculateLetterSize(int meanHeight) {
        letterSize = meanHeight;
    }

    private void calculateTrashsize(int meanHeight) {
        int offset = (int) (0.1*meanHeight);
        trashSize = meanHeight-offset;
    }

    private void calculateSpaceGap(int meanHeight) {
        spaceGap = (int) (0.3*meanHeight);
    }

//    /**
//     * If you want to recognize small characters as dots and comas. Otherwise
//     * the algorithm will ignore them.
//     */
//    public void recognizeDots() {
//        trashSize = 9;
//    }

    /**
     * @return height of the image with single character.
     */
    public int getCropHeight() {
        return cropHeight;
    }

    /**
     * @return width of the image with single character.
     */
    public int getCropWidth() {
        return cropWidth;
    }

    /**
     *
     * @return predicted letter size (height) based on the scanQuality and
     * fontSize
     */
    public int getLetterSize() {
        return letterSize;
    }

    /**
     * Used for finding rows in text. Smaller value is probably dot or coma
     * which is not recognized as row. Size is actually height of letter
     *
     * @return predicted smallest size of letter.
     */
    public int getSmallestSizeLetter() {
        return smallestSizeLetter;
    }

    /**
     * Avoid to recognize the trash. size is actually number of pixels
     *
     * @return predicted smallest size of trash
     */
    public int getTrashSize() {
        return trashSize;
    }

    /**
     *
     * @return the space(measured in pixels) that should represent the space
     * typed on keyboard
     */
    public int getSpaceGap() {
        return spaceGap;
    }

    /**
     * 
     * @return scan quality measured in dpi 
     */
    public int getScanQuality() {
        return scanQuality;
    }

    /**
     * 
     * @return font size measured in pt
     */
    public int getFontSize() {
        return fontSize;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setLetterSize(int letterSize) {
        this.letterSize = letterSize;
    }

    public void setSmallestSizeLetter(int smallestSizeLetter) {
        this.smallestSizeLetter = smallestSizeLetter;
    }

    public void setSpaceGap(int spaceGap) {
        this.spaceGap = spaceGap;
    }

    public void setTrashSize(int trashSize) {
        this.trashSize = trashSize;
    }

    private double caluclateMean(List<Integer> list) {
        double sum = 0;
        for (Integer element : list) {
            sum+=element;
        }
        return sum/list.size();
    }

    
    
    

}
