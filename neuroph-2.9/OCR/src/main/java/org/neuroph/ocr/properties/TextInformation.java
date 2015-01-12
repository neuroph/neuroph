/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.properties;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neuroph.ocr.util.OCRUtilities;
import org.neuroph.ocr.util.Word;

/**
 *
 * @author Mihailo
 */
public class TextInformation {
    
    private List<Integer> rowPositions;
    private Map<Integer, List<Word>> map;
    private LetterInformation letterInfo;
    
    private BufferedImage image;

    public TextInformation(BufferedImage image, LetterInformation letterInformation) {
        this.letterInfo = letterInformation;
        this.image = image;
        rowPositions = OCRUtilities.rowPositions(image, letterInformation.getSmallestSizeLetter());
        createMap();
        populateMap();
    }
    
    
    private void populateMap() {
        for (Integer row : rowPositions) {
            map.put(row, OCRUtilities.wordsPositions(image, row, letterInfo.getLetterSize(), letterInfo.getSpaceGap()));
        }  
    }
    
    private void createMap() {
        map = new HashMap<Integer, List<Word>>();
        for (Integer row : rowPositions) {
            map.put(row, new ArrayList<Word>());
        }
    }
    
    /**
     * 
     * @return number of rows 
     */
    public int numberOfRows() {
        return rowPositions.size();
    }
    
    /**
     * pixel position of the row
     * @param index start from 0
     * @return pixel position of the row at the index
     */
    public int getRowAt(int index) {
        return rowPositions.get(index);
    }
    
    /**
     * return objects of class Word as List 
     * @param index index of row, start from 0
     * @return list of Word
     * @see Word
     */
    public List<Word> getWordsAtRow(int index) {
        int key = rowPositions.get(index);
        return map.get(key);
    }
    
    /**
     * return objects of class Word as List 
     * @param pixel pixel position of the row
     * @return list of Word
     * @see Word
     * @throws NullPointerException if the pixel is not result of method 
 getRowAt(index)
     */
    public List<Word> getWordsAtPixel(int pixel) {
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
