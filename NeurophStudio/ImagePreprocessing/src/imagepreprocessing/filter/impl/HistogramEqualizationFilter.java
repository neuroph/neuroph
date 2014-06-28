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
 * Histogram equalization filter serves to reduce the contrast of the grayscale 
 * image.For example, if the image before histogram equalization filter has too 
 * many dark pixels and a little light pixels,after this filter the difference 
 * will alleviate. If the plan is, after this filter, to use Otsu binarized filter, 
 * this filter will not influence on him.
 * 
 * @author Mihailo Stupar
 */
public class HistogramEqualizationFilter implements ImageFilter,Serializable {

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
	
    @Override
    public BufferedImage processImage(BufferedImage image) {
		
	originalImage = image;
		
	int width = originalImage.getWidth();
	int height = originalImage.getHeight();
		
	filteredImage = new BufferedImage(width, height, originalImage.getType());
		
	int [] histogram = imageHistogram(originalImage);
		
	int [] histogramCumulative = new int[histogram.length];
		
	histogramCumulative[0] = histogram[0];
	for (int i = 1; i < histogramCumulative.length; i++) {
            histogramCumulative[i] = histogramCumulative[i-1] + histogram[i];
	}
		
	int G = 256;
	int gray;
	int alpha;
		
	int newColor;
		
	for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
				
		gray = new Color(originalImage.getRGB(i, j)).getRed();
		alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
				
		newColor = (G-1)*histogramCumulative[gray]/(width*height); //zaokruziti izbeci celobrojno deljenje

				
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
    @Override
    public String toString() {
        return "Histogram Equalization Filter";
    }
}
