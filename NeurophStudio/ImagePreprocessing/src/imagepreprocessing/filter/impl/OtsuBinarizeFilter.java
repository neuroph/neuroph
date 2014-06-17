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
 * Otsu binarize filter serves to dynamically determine the threshold based on 
 * the whole image and for later binarization on black (0) and white (255) pixels. 
 * In determining threshold a image histogram is created in way that the value of 
 * each pixel of image affects on the histogram appearance. Then, depending upon 
 * the look of the histogram threshold counts and based on that, the real image 
 * which is binarized is made.The image before this filter must be grayscale and 
 * at the end image will contain only two colors - black and white. 
 *
 * reference to: http://zerocool.is-a-geek.net/?p=376
 * 
 * @author Mihailo Stupar
 */
public class OtsuBinarizeFilter implements ImageFilter,Serializable {

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
    @Override
	
    public BufferedImage processImage(BufferedImage image) {
		
        originalImage = image;
		
	int width = originalImage.getWidth();
	int height = originalImage.getHeight();
		
	filteredImage = new BufferedImage(width, height, originalImage.getType());
		
	int [] histogram = imageHistogram(originalImage);
		
	int totalNumberOfpixels = height*width;
		
	int treshold = treshold(histogram, totalNumberOfpixels);
		
	int black = 0;
	int white = 255;
		
	int alpha;
	int gray;
	int newColor;
		
	for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
		gray = new Color(originalImage.getRGB(i, j)).getRed();
		alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
				
		if (gray > treshold)
                    newColor = white;
		else
                    newColor = black;
				
		newColor = PreprocessingHelper.colorToRGB(alpha, newColor, newColor, newColor);
		filteredImage.setRGB(i, j, newColor);
            }
	}
		
	return filteredImage;
    }
	
    public int[] imageHistogram(BufferedImage image) {

	int[] histogram = new int[256];

	for (int i = 0; i < histogram.length; i++)
            histogram[i] = 0;

            for (int i = 0; i < image.getWidth(); i++) {
		for (int j = 0; j < image.getHeight(); j++) {
                    int gray = new Color(image.getRGB(i, j)).getRed();
                    histogram[gray]++;
		}
            }
        return histogram;
    }
	
    private int treshold(int [] histogram, int total) {
	float sum = 0;
	for (int i = 0; i < 256; i++)
            sum += i * histogram[i];

            float sumB = 0;
            int wB = 0;
            int wF = 0;

            float varMax = 0;
            int threshold = 0;

            for (int i = 0; i < 256; i++) {
		wB += histogram[i];
		if (wB == 0)
                    continue;
		wF = total - wB;

		if (wF == 0)
                    break;

		sumB += (float) (i * histogram[i]);
                float mB = sumB / wB;
                float mF = (sum - sumB) / wF;

                float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

                if (varBetween > varMax) {
                    varMax = varBetween;
                    threshold = i;
                }
            }
	return threshold;
    }
    
    @Override
    public String toString() {
        return "Otsu Binarize Filter";
    }

 
}