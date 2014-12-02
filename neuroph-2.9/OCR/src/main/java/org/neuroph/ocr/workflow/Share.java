/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.workflow;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Mihailo
 */
public class Share {
    
    private String text;
    private BufferedImage image;
    private ArrayList<String> letterLabels;

    public String getText() {
        return text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public ArrayList<String> getLetterLabels() {
        return letterLabels;
    }

    public void setLetterLabels(ArrayList<String> letterLabels) {
        this.letterLabels = letterLabels;
    }
    
    
    
    
    
    

    private static Share instance = null;
    
    public static Share getInstance() {
        if (instance == null)
            instance = new Share();
        return instance;
    }
    
    
}
