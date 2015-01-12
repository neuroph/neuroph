/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.properties;

import java.util.List;

/**
 *
 * @author Mihailo Stupar
 */
public class LetterInformation {

    private int cropWidth;
    private int cropHeight;
    private int smallestSizeLetter;
    private int letterSize;
    private int trashSize;
    private int spaceGap;

    private int scanQuality;
    private int fontSize;

    public LetterInformation(int scanQuality, int fontSize) {
        this.scanQuality = scanQuality;
        this.fontSize = fontSize;

        calculateDimensions();
        calculateSmallestSizeLetter();
        calculateLetterSize();
        calculateTrashsize();
        calculateSpaceGap();
    }

    private void calculateDimensions() {
        cropWidth = 45;
        cropHeight = 45;
    }

    private void calculateSmallestSizeLetter() {
        smallestSizeLetter = 9;
    }

    private void calculateLetterSize() {
        letterSize = 42;
    }

    private void calculateTrashsize() {
        trashSize = 35;
    }

    private void calculateSpaceGap() {
        spaceGap = 12;
    }

    /**
     * If you want to recognize small characters as dots and comas. Otherwise
     * the algorithm will ignore them.
     */
    public void recognizeDots() {
        trashSize = 9;
    }

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
     * which is not recognized as row. Size is actually height
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
    
    

}
