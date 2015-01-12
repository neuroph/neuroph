/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.util;

/**
 * 
 * @author Mihailo Stupar
 */
public class Word {
   
    private int startPixel;
    private int endPixel;

    public Word(int startPixel, int endPixel) {
        this.startPixel = startPixel;
        this.endPixel = endPixel;
    }

    public int getStartPixel() {
        return startPixel;
    }

    public int getEndPixel() {
        return endPixel;
    }
    
    
}
