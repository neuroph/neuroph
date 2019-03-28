/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neuroph.ocr.util.OCRUtilities;
import org.neuroph.ocr.util.WordPosition;

/**
 *
 * @author Mihailo
 */
public class Text {
    
    private List<Integer> linePositions;
    private Map<Integer, List<WordPosition>> map;
    private Letter letterInfo;
    
    private BufferedImage image;

    public Text(BufferedImage image, Letter letterInformation) {
        this.letterInfo = letterInformation;
        this.image = image;
        linePositions = OCRUtilities.rowPositions(image, letterInformation.getSmallestSizeLetter());
        createMap();
        populateMap();
    }
    
    
    private void populateMap() {
        for (Integer row : linePositions) {
            map.put(row, OCRUtilities.wordsPositions(image, row, letterInfo.getLetterSize(), letterInfo.getSpaceGap()));
        }  
    }
    
    private void createMap() {
        map = new HashMap<Integer, List<WordPosition>>();
        for (Integer row : linePositions) {
            map.put(row, new ArrayList<WordPosition>());
        }
    }
    
    /**
     * 
     * @return number of rows 
     */
    public int numberOfRows() {
        return linePositions.size();
    }
    
    /**
     * pixel position of the row
     * @param index start from 0
     * @return pixel position of the row at the index
     */
    public int getRowAt(int index) {
        return linePositions.get(index);
    }
    
    /**
     * return objects of class Word as List 
     * @param index index of row, start from 0
     * @return list of Word
     * @see WordPosition
     */
    public List<WordPosition> getWordsAtRow(int index) {
        int key = linePositions.get(index);
        return map.get(key);
    }
    
    /**
     * return objects of class Word as List 
     * @param pixel pixel position of the row
     * @return list of Word
     * @see WordPosition
     * @throws NullPointerException if the pixel is not result of method 
 getRowAt(index)
     */
    public List<WordPosition> getWordsAtPixel(int pixel) {
        return map.get(pixel);
    }

    /**
     * 
     * @return Image (document)
     */
    public BufferedImage getImage() {
        return image;
    }
    
    
}
