/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.filter.impl;

import imagepreprocessing.helper.PreprocessingHelper;
import imagepreprocessing.filter.IParametersPanel;
import imagepreprocessing.filter.ImageFilter;
import imagepreprocessing.view.panels.MedianPanel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Arrays;
import javax.swing.JPanel;

/**
 * Median filter is used for noise reduction on the grayscale image. The filter 
 * works on way that for each pixel in the image one window is set around it. 
 * Radius of the window by default is set to 4. Then all the values of the pixels 
 * belonging to the window are being sorted and values are used to calculate new 
 * value that represents the median. The value of that pixels in filtered image 
 * is replaced with one that is obtained as the median.
 * @param radius radius of the window
 * 
 * @author Mihailo Stupar
 */
public class MedianFilter implements ImageFilter,IParametersPanel ,Serializable{

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
	
    private int radius;
	
    public MedianFilter () {
	radius = 1;
    }
	
	
    @Override
    public BufferedImage processImage(BufferedImage image) {
		
        originalImage = image;
		
	int width = originalImage.getWidth();
	int height = originalImage.getHeight();
		
	filteredImage = new BufferedImage(width, height, originalImage.getType());
		
	int [] arrayOfPixels;
	int median;
	int alpha;
	int newColor;
		
	for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
				
		arrayOfPixels = getArrayOfPixels(i, j);
		median = findMedian(arrayOfPixels);
		alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
				
		newColor = PreprocessingHelper.colorToRGB(alpha, median, median, median);
		filteredImage.setRGB(i, j, newColor);
            }
        }
		
	return filteredImage;
    }
	
    public int[] getArrayOfPixels (int i, int j) {
		
        int startX = i - radius;
	int goalX = i + radius;
	int startY = j - radius;
	int goalY = j + radius;
		
	if (startX < 0)
            startX = 0;
	if (goalX > originalImage.getWidth() - 1)
            goalX = originalImage.getWidth() - 1;
        if (startY < 0)
            startY = 0;
	if (goalY > originalImage.getHeight() - 1)
            goalY = originalImage.getHeight() - 1;
		
	int arraySize = (goalX - startX + 1)*(goalY - startY +1);
	int [] pixels = new int [arraySize];
		
	int position = 0;
	int color;
        for (int p = startX; p <= goalX; p++) {
            for (int q = startY; q <= goalY; q++) {
		color = new Color(originalImage.getRGB(p, q)).getRed();
		pixels[position] = color;
		position++;
            }
	}
		
	return pixels;
    }
	
    public int findMedian (int [] arrayOfPixels) {
	Arrays.sort(arrayOfPixels);
	int middle = arrayOfPixels.length/2;
	return arrayOfPixels[middle];
    }

    /**
     * 
     * @param radius radius of the window. Current pixel is in center of this 
     * window 
     */
    
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Median Filter";
    }

    @Override
    public JPanel getPanel() {
        return new MedianPanel();
    }
    
    
    
}