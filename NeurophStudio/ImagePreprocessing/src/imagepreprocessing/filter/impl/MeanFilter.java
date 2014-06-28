/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.helper.PreprocessingHelper;
import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Mihailo Stupar
 */
public class MeanFilter implements ImageFilter,Serializable{

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
    
    private int radius;

    public MeanFilter() {
        radius = 4;
    }
    
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
    
        originalImage = image;
        
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        filteredImage = new BufferedImage(width, height, originalImage.getType());
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                
                int color = findMean(i, j);
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                
                int rgb = PreprocessingHelper.colorToRGB(alpha, color, color, color);
                filteredImage.setRGB(i, j, rgb);
                
            }
        }
        
        
        return filteredImage;
        
        
    }
    
    public int findMean (int x, int y) {       
        double sum = 0;
        int n = 0;  
        for (int i = x-radius; i <= x+radius; i++) {
            for (int j = y-radius; j <= y+radius; j++) {              
                if (i>0 && i<originalImage.getWidth() && j>0 && j<originalImage.getHeight()) {                
                    int color = new Color(originalImage.getRGB(i, j)).getRed();
                    sum = sum + color;
                    n++;
                }   
            }
        }
        return (int) Math.round(sum/n);      
    }

    @Override
    public String toString() {
        return "Mean Filter";
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    
}
