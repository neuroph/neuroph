/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.properties;

import java.awt.image.BufferedImage;

/**
 *
 * @author Mihailo Stupar
 */
public abstract class Properties {

    protected BufferedImage image;
    protected int scanQuality;
    protected int fontSize;

    protected LetterInformation letterInformation;
    protected TextInformation textInformation;
   
    
    public Properties(LetterInformation letterInformation, TextInformation textInformation) {
        this.letterInformation = letterInformation;
        this.textInformation = textInformation;
        this.image = textInformation.getImage();
        this.scanQuality = letterInformation.getScanQuality();
        this.fontSize = letterInformation.getScanQuality();
    }

    /**
     * dimensions of letter, of spacing, of cropped image...
     * @return Information about letter 
     */
    public LetterInformation getLetterInformation() {
        return letterInformation;
    }

    /**
     * informations about line positions, word positions...
     * @return informations about text 
     */
    public TextInformation getTextInformation() {
        return textInformation;
    }

    /**
     * 
     * @return scanned document
     */
    public BufferedImage getImage() {
        return image;
    }

    
    
}
