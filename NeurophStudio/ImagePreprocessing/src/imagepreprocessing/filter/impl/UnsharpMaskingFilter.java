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
public class UnsharpMaskingFilter implements ImageFilter,Serializable{

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
    
    
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        originalImage = image;
        
        
        
        
        BufferedImage bluredImage = getBluredImage();
        
        BufferedImage unsharpMask = getUnsharpMask(originalImage, bluredImage);
        
        filteredImage = getSharpImage(originalImage, unsharpMask);
  
        return filteredImage;
    }
    
    
    public BufferedImage getBluredImage() {
        
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        BufferedImage bluredImage = new BufferedImage(width, height, originalImage.getType());
        int alpha;
        int newColor;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newColor = getAverageBluring(i, j);
                alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                int rgb = PreprocessingHelper.colorToRGB(alpha, newColor, newColor, newColor);
                bluredImage.setRGB(i, j, rgb);
            }
        }
        
        return bluredImage;
    }
    
    public int getAverageBluring (int i, int j) {
        
        double sum = 0;
        int n = 0;
        
        for (int x = i-1; x <= i+1; x++) {
            for (int y = j-1; y <= j+1; y++) {
                if (x>=0 && x<originalImage.getWidth() && y>=0 && y<originalImage.getHeight()) {
                    int color = new Color(originalImage.getRGB(x, y)).getRed();
                    sum = sum+color;
                    n++;
                }
            }
        }
        
        int average = (int) Math.round(sum/n);
        return average;
    }
    
    public BufferedImage getUnsharpMask (BufferedImage originalImage, BufferedImage bluredImage) {
        
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        BufferedImage unsharpMask = 
                new BufferedImage(width, height, originalImage.getType());
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int originalColor = new Color(originalImage.getRGB(i, j)).getRed();
                int blurColor = new Color(bluredImage.getRGB(i, j)).getRed();
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha(); 
                int newColor = originalColor - blurColor;
                int rgb = PreprocessingHelper.colorToRGB(alpha, newColor, newColor, newColor);
                unsharpMask.setRGB(i, j, rgb);
            }
        } 
        return unsharpMask;
    }
    
    public BufferedImage getSharpImage (BufferedImage originalImage, BufferedImage unsharpMask) {
        
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        BufferedImage sharpImage = 
                new BufferedImage(width, height, originalImage.getType());
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int originalColor = new Color(originalImage.getRGB(i, j)).getRed();
                int unsharpColor = new Color(unsharpMask.getRGB(i, j)).getRed();
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha(); 
                int newColor = originalColor + unsharpColor;
                int rgb = PreprocessingHelper.colorToRGB(alpha, newColor, newColor, newColor);
                sharpImage.setRGB(i, j, rgb);
            }
        } 
        return sharpImage;
  
    } 

    @Override
    public String toString() {
        return "Unsharp Masking Filter";
    }
    
    
}
